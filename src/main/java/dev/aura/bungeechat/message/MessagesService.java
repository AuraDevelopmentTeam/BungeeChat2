package dev.aura.bungeechat.message;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.chatlog.ChatLoggingManager;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.IgnoringModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

@UtilityClass
public class MessagesService {
  @Setter private static List<List<String>> multiCastServerGroups = null;

  public static void unsetMultiCastServerGroups() {
    setMultiCastServerGroups(null);
  }

  public static void sendPrivateMessage(CommandSender sender, CommandSender target, String message)
      throws InvalidContextError {
    sendPrivateMessage(new Context(sender, target, message));
  }

  public static void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(
        BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_TARGET, BungeeChatContext.HAS_MESSAGE);

    Optional<BungeeChatAccount> account = context.getSender();
    BungeeChatAccount senderAccount = account.get();
    BungeeChatAccount targetAccount = context.getTarget().get();
    CommandSender sender = BungeecordAccountManager.getCommandSender(senderAccount).get();
    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount).get();
    boolean filterPrivateMessages =
        BungeecordModuleManager.MESSENGER_MODULE
            .getModuleSection()
            .getBoolean("filterPrivateMessages");

    if (targetAccount.hasIgnored(senderAccount)
        && !PermissionManager.hasPermission(sender, Permission.BYPASS_IGNORE)) {
      MessagesService.sendMessage(sender, Messages.HAS_IGNORED.get(context));

      return;
    }

    Optional<String> messageSender =
        preProcessMessage(context, account, Format.MESSAGE_SENDER, filterPrivateMessages);

    if (messageSender.isPresent()) {
      MessagesService.sendMessage(sender, messageSender.get());

      String messageTarget =
          preProcessMessage(context, account, Format.MESSAGE_TARGET, filterPrivateMessages, true)
              .get();
      MessagesService.sendMessage(target, messageTarget);

      if (ModuleManager.isModuleActive(BungeecordModuleManager.SPY_MODULE)
          && !PermissionManager.hasPermission(account.get(), Permission.COMMAND_SOCIALSPY_EXEMPT)) {
        String socialSpyMessage =
            preProcessMessage(context, account, Format.SOCIAL_SPY, false).get();

        sendToMatchingPlayers(
            socialSpyMessage,
            acc ->
                (!acc.getUniqueId().equals(senderAccount.getUniqueId()))
                    && (!acc.getUniqueId().equals(targetAccount.getUniqueId()))
                    && acc.hasSocialSpyEnabled());
      }
    }

    if (BungeecordModuleManager.CHAT_LOGGING_MODULE
        .getModuleSection()
        .getBoolean("privateMessages")) {
      ChatLoggingManager.logMessage("PM to " + targetAccount.getName(), context);
    }
  }

  public static void sendChannelMessage(CommandSender sender, ChannelType channel, String message)
      throws InvalidContextError {
    sendChannelMessage(new Context(sender, message), channel);
  }

  public static void sendChannelMessage(BungeeChatContext context, ChannelType channel)
      throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    switch (channel) {
      case GLOBAL:
        sendGlobalMessage(context);
        break;
      case LOCAL:
        sendLocalMessage(context);
        break;
      case STAFF:
        sendStaffMessage(context);
        break;
      case HELP:
        sendHelpMessage(context);
        break;
      default:
        // Ignore
        break;
    }
  }

  public static void sendGlobalMessage(CommandSender sender, String message)
      throws InvalidContextError {
    sendGlobalMessage(new Context(sender, message));
  }

  public static void sendGlobalMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    Optional<BungeeChatAccount> account = context.getSender();
    Optional<String> finalMessage = preProcessMessage(context, Format.GLOBAL_CHAT);

    sendToMatchingPlayers(finalMessage, getGlobalPredicate(), getNotIgnoredPredicate(account));

    ChatLoggingManager.logMessage(ChannelType.GLOBAL, context);
  }

  public static void sendLocalMessage(CommandSender sender, String message)
      throws InvalidContextError {
    sendLocalMessage(new Context(sender, message));
  }

  public static void sendLocalMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    Optional<BungeeChatAccount> account = context.getSender();
    Optional<String> finalMessage = preProcessMessage(context, Format.LOCAL_CHAT);
    String localServerName =
        context.hasServer() ? context.getServer().get() : context.getSender().get().getServerName();
    Predicate<BungeeChatAccount> isLocal = getLocalPredicate(localServerName);
    Predicate<BungeeChatAccount> notIgnored = getNotIgnoredPredicate(account);

    sendToMatchingPlayers(finalMessage, isLocal, notIgnored);

    ChatLoggingManager.logMessage(ChannelType.LOCAL, context);

    if (ModuleManager.isModuleActive(BungeecordModuleManager.SPY_MODULE)) {
      String localSpyMessage = preProcessMessage(context, account, Format.LOCAL_SPY, false).get();
      Predicate<BungeeChatAccount> isNotLocal = isLocal.negate();

      sendToMatchingPlayers(
          localSpyMessage, BungeeChatAccount::hasLocalSpyEnabled, isNotLocal, notIgnored);
    }
  }

  public static void sendTransparentMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    Optional<BungeeChatAccount> account = context.getSender();
    String localServerName =
        context.hasServer() ? context.getServer().get() : context.getSender().get().getServerName();
    Predicate<BungeeChatAccount> isLocal = getLocalPredicate(localServerName);

    ChatLoggingManager.logMessage(ChannelType.LOCAL, context);

    if (ModuleManager.isModuleActive(BungeecordModuleManager.SPY_MODULE)
        && !PermissionManager.hasPermission(account.get(), Permission.COMMAND_LOCALSPY_EXEMPT)) {
      String localSpyMessage = preProcessMessage(context, account, Format.LOCAL_SPY, false).get();
      Predicate<BungeeChatAccount> isNotLocal = isLocal.negate();

      sendToMatchingPlayers(localSpyMessage, BungeeChatAccount::hasLocalSpyEnabled, isNotLocal);
    }
  }

  public static void sendStaffMessage(CommandSender sender, String message)
      throws InvalidContextError {
    sendStaffMessage(new Context(sender, message));
  }

  public static void sendStaffMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    Optional<String> finalMessage = preProcessMessage(context, Format.STAFF_CHAT);

    sendToMatchingPlayers(
        finalMessage, pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_STAFFCHAT_VIEW));

    ChatLoggingManager.logMessage(ChannelType.STAFF, context);
  }

  public static void sendHelpMessage(CommandSender sender, String message)
      throws InvalidContextError {
    sendHelpMessage(new Context(sender, message));
  }

  public static void sendHelpMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

    Optional<String> finalMessage = preProcessMessage(context, Format.HELP_OP);
    BungeeChatAccount sender = context.getSender().get();

    sendToMatchingPlayers(
        finalMessage,
        pp ->
            PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP_VIEW)
                || sender.equals(pp));

    ChatLoggingManager.logMessage(ChannelType.HELP, context);
  }

  public static void sendJoinMessage(CommandSender sender) throws InvalidContextError {
    sendJoinMessage(new Context(sender));
  }

  public static void sendJoinMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER);

    String finalMessage = Format.JOIN_MESSAGE.get(context);
    Predicate<BungeeChatAccount> predicate = getPermissionPredicate(Permission.MESSAGE_JOIN_VIEW);

    // This condition checks if the player is present and vanished
    if (context.getSender().filter(BungeeChatAccount::isVanished).isPresent()) {
      predicate = predicate.and(getPermissionPredicate(Permission.COMMAND_VANISH_VIEW));
    }

    sendToMatchingPlayers(finalMessage, predicate);

    context.setMessage(finalMessage);
    ChatLoggingManager.logMessage("JOIN", context);
  }

  public static void sendLeaveMessage(CommandSender sender) throws InvalidContextError {
    sendLeaveMessage(new Context(sender));
  }

  public static void sendLeaveMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER);

    String finalMessage = Format.LEAVE_MESSAGE.get(context);
    Predicate<BungeeChatAccount> predicate = getPermissionPredicate(Permission.MESSAGE_LEAVE_VIEW);

    // This condition checks if the player is present and vanished
    if (context.getSender().filter(BungeeChatAccount::isVanished).isPresent()) {
      predicate = predicate.and(getPermissionPredicate(Permission.COMMAND_VANISH_VIEW));
    }

    sendToMatchingPlayers(finalMessage, predicate);

    context.setMessage(finalMessage);
    ChatLoggingManager.logMessage("LEAVE", context);
  }

  public static void sendSwitchMessage(CommandSender sender, ServerInfo server)
      throws InvalidContextError {
    sendSwitchMessage(sender, (server == null) ? null : server.getName());
  }

  public static void sendSwitchMessage(CommandSender sender, String server)
      throws InvalidContextError {
    final Context context = new Context(sender);
    if (server != null) context.setServer(server);

    sendSwitchMessage(context);
  }

  public static void sendSwitchMessage(BungeeChatContext context) throws InvalidContextError {
    context.require(BungeeChatContext.HAS_SENDER);

    String finalMessage = Format.SERVER_SWITCH.get(context);
    Predicate<BungeeChatAccount> predicate = getPermissionPredicate(Permission.MESSAGE_SWITCH_VIEW);

    // This condition checks if the player is present and vanished
    if (context.getSender().filter(BungeeChatAccount::isVanished).isPresent()) {
      predicate = predicate.and(getPermissionPredicate(Permission.COMMAND_VANISH_VIEW));
    }

    sendToMatchingPlayers(finalMessage, predicate);

    context.setMessage(finalMessage);
    ChatLoggingManager.logMessage("SWITCH", context);
  }

  public static Optional<String> preProcessMessage(BungeeChatContext context, Format format)
      throws InvalidContextError {
    return preProcessMessage(context, context.getSender(), format, true);
  }

  public static Optional<String> preProcessMessage(
      BungeeChatContext context, Optional<BungeeChatAccount> account, Format format)
      throws InvalidContextError {
    return preProcessMessage(context, account, format, true);
  }

  public static Optional<String> preProcessMessage(
      BungeeChatContext context,
      Optional<BungeeChatAccount> account,
      Format format,
      boolean runFilters) {
    return preProcessMessage(context, account, format, runFilters, false);
  }

  public static Optional<String> preProcessMessage(
      BungeeChatContext context,
      Optional<BungeeChatAccount> account,
      Format format,
      boolean runFilters,
      boolean ignoreBlockMessageExceptions)
      throws InvalidContextError {
    context.require(BungeeChatContext.HAS_MESSAGE);

    BungeeChatAccount playerAccount = account.get();
    CommandSender player = BungeecordAccountManager.getCommandSender(playerAccount).get();
    String message = PlaceHolderUtil.transformAltColorCodes(context.getMessage().get(), account);

    if (runFilters) {
      try {
        message = FilterManager.applyFilters(playerAccount, message);
      } catch (BlockMessageException e) {
        if (!ignoreBlockMessageExceptions) {
          MessagesService.sendMessage(player, e.getMessage());

          return Optional.empty();
        }
      }
    }

    context.setMessage(message);

    return Optional.of(PlaceHolderUtil.getFullFormatMessage(format, context));
  }

  @SafeVarargs
  public static void sendToMatchingPlayers(
      Optional<String> finalMessage, Predicate<BungeeChatAccount>... playerFilters) {
    if (finalMessage.isPresent()) {
      sendToMatchingPlayers(finalMessage.get(), playerFilters);
    }
  }

  @SafeVarargs
  public static void sendToMatchingPlayers(
      String finalMessage, Predicate<BungeeChatAccount>... playerFilters) {
    Predicate<BungeeChatAccount> playerFiler =
        Arrays.stream(playerFilters).reduce(Predicate::and).orElse(acc -> true);

    AccountManager.getPlayerAccounts().stream()
        .filter(playerFiler)
        .forEach(
            account ->
                MessagesService.sendMessage(
                    BungeecordAccountManager.getCommandSender(account).get(), finalMessage));
  }

  public static Predicate<BungeeChatAccount> getServerListPredicate(Config section) {
    if (!section.getBoolean("enabled")) return account -> true;
    else {
      // TODO: Use wildcard string
      List<String> allowedServers = section.getStringList("list");

      return account -> allowedServers.contains(account.getServerName());
    }
  }

  public static Predicate<BungeeChatAccount> getGlobalPredicate() {
    return getServerListPredicate(
        BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getConfig("serverList"));
  }

  public static Predicate<BungeeChatAccount> getServerPredicate(List<String> servers) {
    return account -> servers.contains(account.getServerName());
  }

  public static Predicate<BungeeChatAccount> getLocalPredicate(String serverName) {
    if (multiCastServerGroups == null) {
      return account -> serverName.equals(account.getServerName());
    } else {
      return account -> {
        final String accountServerName = account.getServerName();

        for (List<String> group : multiCastServerGroups) {
          if (group.contains(accountServerName)) {
            return group.contains(serverName);
          }
        }

        return serverName.equals(accountServerName);
      };
    }
  }

  public static Predicate<BungeeChatAccount> getLocalPredicate() {
    final Config serverList =
        BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection().getConfig("serverList");
    final Config passThruServerList =
        BungeecordModuleManager.LOCAL_CHAT_MODULE
            .getModuleSection()
            .getConfig("passThruServerList");

    return Stream.of(serverList, passThruServerList)
        .flatMap(MessagesService::serverListToPredicate)
        .collect(() -> account -> true, Predicate::and, Predicate::and);
  }

  private static Stream<Predicate<BungeeChatAccount>> serverListToPredicate(Config section) {
    if (section.getBoolean("enabled")) {
      // TODO: Use wildcard string
      List<String> allowedServers = section.getStringList("list");

      return Stream.of(account -> allowedServers.contains(account.getServerName()));
    } else {
      return Stream.empty();
    }
  }

  public static Predicate<BungeeChatAccount> getPermissionPredicate(Permission permission) {
    return account -> PermissionManager.hasPermission(account, permission);
  }

  public static Predicate<BungeeChatAccount> getNotIgnoredPredicate(
      Optional<BungeeChatAccount> sender) {
    return getNotIgnoredPredicate(sender.get());
  }

  public static Predicate<BungeeChatAccount> getNotIgnoredPredicate(BungeeChatAccount sender) {
    final IgnoringModule ignoringModule = BungeecordModuleManager.IGNORING_MODULE;

    return (ignoringModule.isEnabled()
            && ignoringModule.getModuleSection().getBoolean("ignoreChatMessages")
            && !PermissionManager.hasPermission(sender, Permission.BYPASS_IGNORE))
        ? account -> !account.hasIgnored(sender)
        : account -> true;
  }

  public static void sendMessage(CommandSender recipient, String message) {
    if ((message == null) || message.isEmpty()) return;

    recipient.sendMessage(TextComponent.fromLegacyText(message));
  }
}

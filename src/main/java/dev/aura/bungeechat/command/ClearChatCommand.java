package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ClearChatModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.ServerNameUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClearChatCommand extends BaseCommand {
  // package because only neighboring class needs it
  static final List<String> arg1Completions = Arrays.asList("local", "global");

  private static final String USAGE = "/clearchat <local [server]|global>";
  private static final String EMPTY_LINE = " ";

  public ClearChatCommand(ClearChatModule clearChatModule) {
    super(
        "clearchat",
        Permission.COMMAND_CLEAR_CHAT,
        clearChatModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_CLEAR_CHAT)) return;

    if ((args.length < 1) || (args.length > 3)) {
      MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(sender, USAGE));
    } else {

      final int lines =
          BungeecordModuleManager.CLEAR_CHAT_MODULE.getModuleSection().getInt("emptyLines");
      final BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(sender).get();

      if (args[0].equalsIgnoreCase("local")) {
        boolean serverSpecified = args.length == 2;

        if (!serverSpecified && !(sender instanceof ProxiedPlayer)) {
          MessagesService.sendMessage(
              sender, Messages.INCORRECT_USAGE.get(bungeeChatAccount, USAGE));
          return;
        }

        Optional<String> optServerName =
            ServerNameUtil.verifyServerName(
                serverSpecified ? args[1] : bungeeChatAccount.getServerName(), sender);

        if (!optServerName.isPresent()) return;

        String serverName = optServerName.get();

        clearLocalChat(serverName, lines);

        MessagesService.sendToMatchingPlayers(
            Messages.CLEARED_LOCAL.get(sender), MessagesService.getLocalPredicate(serverName));
      } else if (args[0].equalsIgnoreCase("global")) {
        clearGlobalChat(lines);

        MessagesService.sendToMatchingPlayers(
            Messages.CLEARED_GLOBAL.get(sender), MessagesService.getGlobalPredicate());
      } else {
        MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(sender, USAGE));
      }
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    final String location = args[0];

    if (args.length == 1) {
      return arg1Completions.stream()
          .filter(completion -> completion.startsWith(location))
          .collect(Collectors.toList());
    } else if ((args.length == 2) && ("local".equals(location))) {
      final String serverName = args[1];

      return ServerNameUtil.getMatchingServerNames(serverName);
    }

    return super.tabComplete(sender, args);
  }

  public static void clearGlobalChat(int emptyLines) {
    clearChat(emptyLines, MessagesService.getGlobalPredicate());
  }

  public static void clearLocalChat(String serverName, int emptyLines) {
    clearChat(emptyLines, MessagesService.getLocalPredicate(serverName));
  }

  private static void clearChat(int emptyLines, Predicate<BungeeChatAccount> predicate) {
    for (int i = 0; i < emptyLines; i++) {
      MessagesService.sendToMatchingPlayers(EMPTY_LINE, predicate);
    }
  }
}

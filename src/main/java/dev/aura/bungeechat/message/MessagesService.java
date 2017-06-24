package dev.aura.bungeechat.message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.chatlog.ChatLoggingManager;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;

@UtilityClass
public class MessagesService {
    public static void sendPrivateMessage(CommandSender sender, CommandSender target, String message)
            throws InvalidContextError {
        sendPrivateMessage(new Context(sender, target, message));
    }

    public static void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_TARGET, BungeeChatContext.HAS_MESSAGE);

        Optional<BungeeChatAccount> account = context.getSender();
        BungeeChatAccount senderAcconut = account.get();
        BungeeChatAccount targetAcconut = context.getTarget().get();
        CommandSender sender = BungeecordAccountManager.getCommandSender(senderAcconut).get();
        CommandSender target = BungeecordAccountManager.getCommandSender(targetAcconut).get();
        boolean filterPrivateMessages = BungeecordModuleManager.MESSENGER_MODULE.getModuleSection()
                .getBoolean("filterMessages");

        if (targetAcconut.hasIgnored(senderAcconut)
                && !PermissionManager.hasPermission(sender, Permission.BYPASS_IGNORE)) {
            MessagesService.sendMessage(sender, Message.HAS_INGORED.get(context));

            return;
        }

        Optional<String> messageSender = preProcessMessage(context, account, Format.MESSAGE_SENDER,
                filterPrivateMessages);

        if (messageSender.isPresent()) {
            MessagesService.sendMessage(sender, messageSender.get());

            String messageTarget = preProcessMessage(context, account, Format.MESSAGE_TARGET, filterPrivateMessages,
                    true).get();
            MessagesService.sendMessage(target, messageTarget);
        }

        if (ModuleManager.isModuleActive(BungeecordModuleManager.SPY_MODULE)) {
            String socialSpyMessage = preProcessMessage(context, account, Format.SOCIAL_SPY, false).get();

            sendToMatchingPlayers(socialSpyMessage, acc -> (!acc.getUniqueId().equals(senderAcconut.getUniqueId()))
                    && (!acc.getUniqueId().equals(targetAcconut.getUniqueId())) && acc.hasSocialSpyEnabled());
        }

        if (BungeecordModuleManager.CHAT_LOGGING_MODULE.getModuleSection().getBoolean("privateMessages")) {
            ChatLoggingManager.logMessage("PM to " + targetAcconut.getName(), context);
        }
    }

    public static void sendChannelMessage(CommandSender sender, ChannelType channel, String message)
            throws InvalidContextError {
        sendChannelMessage(new Context(sender, message), channel);
    }

    public static void sendChannelMessage(BungeeChatContext context, ChannelType channel) throws InvalidContextError {
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
        case GROUP:
            // TODO: Feature comes with 2.1.0
            break;
        default:
            // TODO? Maybe do nothing? Throw an Exception?
            break;
        }
    }

    public static void sendGlobalMessage(CommandSender sender, String message) throws InvalidContextError {
        sendGlobalMessage(new Context(sender, message));
    }

    public static void sendGlobalMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, Format.GLOBAL_CHAT);

        sendToMatchingPlayers(finalMessage, getGlobalPredicate());

        ChatLoggingManager.logMessage(ChannelType.GLOBAL, context);
    }

    public static void sendLocalMessage(CommandSender sender, String message) throws InvalidContextError {
        sendLocalMessage(new Context(sender, message));
    }

    public static void sendLocalMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<BungeeChatAccount> account = context.getSender();
        Optional<String> finalMessage = preProcessMessage(context, Format.LOCAL_CHAT);
        String localServerName = context.getSender().get().getServerName();
        Predicate<BungeeChatAccount> isLocal = getLocalPredicate(localServerName);

        sendToMatchingPlayers(finalMessage, isLocal);

        ChatLoggingManager.logMessage(ChannelType.LOCAL, context);

        if (ModuleManager.isModuleActive(BungeecordModuleManager.SPY_MODULE)) {
            String localSpyMessage = preProcessMessage(context, account, Format.LOCAL_SPY, false).get();
            Predicate<BungeeChatAccount> isNotLocal = isLocal.negate();

            sendToMatchingPlayers(localSpyMessage, BungeeChatAccount::hasLocalSpyEnabled, isNotLocal);
        }
    }

    public static void sendStaffMessage(CommandSender sender, String message) throws InvalidContextError {
        sendStaffMessage(new Context(sender, message));
    }

    public static void sendStaffMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, Format.STAFF_CHAT);

        sendToMatchingPlayers(finalMessage,
                pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_STAFFCHAT_VIEW));

        ChatLoggingManager.logMessage(ChannelType.STAFF, context);
    }

    public static void sendHelpMessage(CommandSender sender, String message) throws InvalidContextError {
        sendHelpMessage(new Context(sender, message));
    }

    public static void sendHelpMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, Format.HELP_OP);
        BungeeChatAccount sender = context.getSender().get();

        sendToMatchingPlayers(finalMessage,
                pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP_VIEW) || sender.equals(pp));

        ChatLoggingManager.logMessage(ChannelType.HELP, context);
    }

    public static void sendJoinMessage(CommandSender sender) throws InvalidContextError {
        sendJoinMessage(new Context(sender));
    }

    public static void sendJoinMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = Format.JOIN_MESSAGE.get(context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("JOIN", context);
    }

    public static void sendLeaveMessage(CommandSender sender) throws InvalidContextError {
        sendLeaveMessage(new Context(sender));
    }

    public static void sendLeaveMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = Format.LEAVE_MESSAGE.get(context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("LEAVE", context);
    }

    public static void sendSwitchMessage(CommandSender sender) throws InvalidContextError {
        sendSwitchMessage(new Context(sender));
    }

    public static void sendSwitchMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = Format.SERVER_SWITCH.get(context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("SWITCH", context);
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, Format format)
            throws InvalidContextError {
        return preProcessMessage(context, context.getSender(), format, true);
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, Optional<BungeeChatAccount> account,
            Format format) throws InvalidContextError {
        return preProcessMessage(context, account, format, true);
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, Optional<BungeeChatAccount> account,
            Format format, boolean runFilters) {
        return preProcessMessage(context, account, format, runFilters, false);
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, Optional<BungeeChatAccount> account,
            Format format, boolean runFilters, boolean ignoreBlockMessageExceptions) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_MESSAGE);

        BungeeChatAccount playerAccount = account.get();
        CommandSender player = BungeecordAccountManager.getCommandSender(playerAccount).get();
        String message = context.getMessage().get();

        if (PermissionManager.hasPermission(player, Permission.USE_COLORED_CHAT)) {
            message = PlaceHolderUtil.transformAltColorCodes(message);
        }

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
    public static void sendToMatchingPlayers(Optional<String> finalMessage,
            Predicate<BungeeChatAccount>... playerFilters) {
        if (finalMessage.isPresent()) {
            sendToMatchingPlayers(finalMessage.get(), playerFilters);
        }
    }

    @SafeVarargs
    public static void sendToMatchingPlayers(String finalMessage, Predicate<BungeeChatAccount>... playerFilters) {
        Predicate<BungeeChatAccount> playerFiler = Arrays.stream(playerFilters).reduce(Predicate::and)
                .orElse(acc -> true);

        AccountManager.getPlayerAccounts().stream().filter(playerFiler).forEach(account -> MessagesService
                .sendMessage(BungeecordAccountManager.getCommandSender(account).get(), finalMessage));
    }

    public static Predicate<BungeeChatAccount> getGlobalPredicate() {
        final Configuration section = BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection()
                .getSection("serverList");

        if (!section.getBoolean("enabled"))
            return account -> true;
        else {
            List<String> allowedServers = section.getStringList("list");

            return account -> allowedServers.contains(account.getServerName());
        }
    }

    public static Predicate<BungeeChatAccount> getLocalPredicate(String serverName) {
        return account -> serverName.equals(account.getServerName());
    }

    @SuppressWarnings("deprecation")
    public static void sendMessage(CommandSender sender, String message) {
        if ((message == null) || message.isEmpty())
            return;

        sender.sendMessage(message);
    }
}

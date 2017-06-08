package dev.aura.bungeechat.message;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;

@UtilityClass
public class MessagesService {
    public static void sendPrivateMessage(CommandSender sender, CommandSender target, String message)
            throws InvalidContextError {
        sendPrivateMessage(new Context(sender, target, message));
    }

    @SuppressWarnings("deprecation")
    public static void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_TARGET, BungeeChatContext.HAS_MESSAGE);

        Optional<BungeeChatAccount> account = context.getSender();
        BungeeChatAccount senderAcconut = account.get();
        BungeeChatAccount targetAcconut = context.getTarget().get();
        CommandSender sender = BungeecordAccountManager.getCommandSender(senderAcconut).get();
        CommandSender target = BungeecordAccountManager.getCommandSender(targetAcconut).get();

        String messageSender = preProcessMessage(context, account, "message-sender", false).get();
        sender.sendMessage(messageSender);

        String messageTarget = preProcessMessage(context, account, "message-target", false).get();
        target.sendMessage(messageTarget);

        if (ModuleManager.isModuleActive(BungeecordModuleManager.SOCIAL_SPY_MODULE)) {
            String socialSpyMessage = preProcessMessage(context, account, "socialspy", false).get();

            sendToMatchingPlayers(socialSpyMessage, acc -> (!acc.getUniqueId().equals(senderAcconut.getUniqueId()))
                    && (!acc.getUniqueId().equals(targetAcconut.getUniqueId())) && acc.hasSocialSpyEnabled());
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
            // TODO
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

        Optional<String> finalMessage = preProcessMessage(context, "global-chat");

        if (!BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("Server-list.enabled")) {
            sendToMatchingPlayers(finalMessage);
        } else {
            sendToMatchingPlayers(finalMessage, account -> BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getStringList("Server-list.list")
                    .contains(account.getServerName()));
        }

        ChatLoggingManager.logMessage(ChannelType.GLOBAL, context);
    }

    public static void sendLocalMessage(CommandSender sender, String message) throws InvalidContextError {
        sendLocalMessage(new Context(sender, message));
    }

    public static void sendLocalMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "local-chat");
        String localServerName = context.getSender().get().getServerName();

        sendToMatchingPlayers(finalMessage, account -> account.getServerName().equals(localServerName));

        ChatLoggingManager.logMessage(ChannelType.LOCAL, context);
    }

    public static void sendStaffMessage(CommandSender sender, String message) throws InvalidContextError {
        sendStaffMessage(new Context(sender, message));
    }

    public static void sendStaffMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "staff-chat");

        sendToMatchingPlayers(finalMessage,
                pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_STAFFCHAT_VIEW));

        ChatLoggingManager.logMessage(ChannelType.STAFF, context);
    }

    public static void sendHelpMessage(CommandSender sender, String message) throws InvalidContextError {
        sendHelpMessage(new Context(sender, message));
    }

    public static void sendHelpMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "helpop");

        sendToMatchingPlayers(finalMessage, pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP_VIEW));

        ChatLoggingManager.logMessage(ChannelType.HELP, context);
    }

    public static void sendJoinMessage(CommandSender sender) throws InvalidContextError {
        sendJoinMessage(new Context(sender));
    }

    public static void sendJoinMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("joinmessage", context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("JOIN", context);
    }

    public static void sendLeaveMessage(CommandSender sender) throws InvalidContextError {
        sendLeaveMessage(new Context(sender));
    }

    public static void sendLeaveMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("leavemessage", context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("LEAVE", context);
    }

    public static void sendSwitchMessage(CommandSender sender) throws InvalidContextError {
        sendSwitchMessage(new Context(sender));
    }

    public static void sendSwitchMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("server-switch", context);

        sendToMatchingPlayers(finalMessage);

        context.setMessage(finalMessage);
        ChatLoggingManager.logMessage("SWITCH", context);
    }

    public static void sendIngoreList(CommandSender sender, String message) throws InvalidContextError {
        sendIngoreList(new Context(sender, message));
    }

    public static void sendIngoreList(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("ignore-list", context);

        sendToMatchingPlayers(finalMessage, pp -> BungeecordAccountManager.getAccount(pp.getUniqueId()).equals(context.getSender()));
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, String format)
            throws InvalidContextError {
        return preProcessMessage(context, context.getSender(), format, true);
    }

    public static Optional<String> preProcessMessage(BungeeChatContext context, Optional<BungeeChatAccount> account,
            String format) throws InvalidContextError {
        return preProcessMessage(context, account, format, true);
    }

    @SuppressWarnings("deprecation")
    public static Optional<String> preProcessMessage(BungeeChatContext context, Optional<BungeeChatAccount> account,
            String format, boolean runFilters) throws InvalidContextError {
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
                player.sendMessage(e.getMessage());

                return Optional.empty();
            }
        }

        context.setMessage(message);

        return Optional.of(PlaceHolderUtil.getFullFormatMessage(format, context));
    }

    @SafeVarargs
    public static void sendToMatchingPlayers(Optional<String> finalMessage,
            Predicate<? super BungeeChatAccount>... playerFilters) {
        if (finalMessage.isPresent()) {
            sendToMatchingPlayers(finalMessage.get(), playerFilters);
        }
    }

    @SafeVarargs
    @SuppressWarnings("deprecation")
    public static void sendToMatchingPlayers(String finalMessage,
            Predicate<? super BungeeChatAccount>... playerFilters) {
        Stream<BungeeChatAccount> stream = AccountManager.getPlayerAccounts().stream();

        for (Predicate<? super BungeeChatAccount> playerFilter : playerFilters) {
            stream = stream.filter(playerFilter);
        }

        stream.forEach(account -> BungeecordAccountManager.getCommandSender(account).get().sendMessage(finalMessage));
    }
}

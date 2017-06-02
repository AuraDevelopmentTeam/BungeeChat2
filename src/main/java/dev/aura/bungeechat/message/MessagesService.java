package dev.aura.bungeechat.message;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.chatlog.ChatLoggingManager;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class MessagesService {
    public static void sendPrivateMessage(CommandSender sender, ProxiedPlayer target, String message)
            throws InvalidContextError {
        sendPrivateMessage(new Context(sender, target, message));
    }

    @SuppressWarnings("deprecation")
    public static void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_TARGET, BungeeChatContext.HAS_MESSAGE);

        Optional<BungeeChatAccount> account = context.getSender();
        ProxiedPlayer sender = Account.toProxiedPlayer(context.getSender().get());
        ProxiedPlayer target = Account.toProxiedPlayer(context.getTarget().get());

        String messageSender = preProcessMessage(context, account, "message-sender", false).get();
        sender.sendMessage(messageSender);

        String messageTarget = preProcessMessage(context, account, "message-target", false).get();
        target.sendMessage(messageTarget);

        if (ModuleManager.isModuleActive(ModuleManager.SOCIAL_SPY_MODULE)) {
            String socialSpyMessage = preProcessMessage(context, account, "socialspy", false).get();

            sendToMatchingPlayers(socialSpyMessage,
                    pp -> (pp != target) && (pp != sender) && AccountManager.getUserAccount(pp).hasSocialSpyEnabled());
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
        case NONE:
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

        sendToMatchingPlayers(finalMessage);

        ChatLoggingManager.logMessage(context, ChannelType.GLOBAL);
    }

    public static void sendLocalMessage(CommandSender sender, String message) throws InvalidContextError {
        sendLocalMessage(new Context(sender, message));
    }

    public static void sendLocalMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "local-chat");
        String localServerName = Account.toProxiedPlayer(context.getSender().get()).getServer().getInfo().getName();

        sendToMatchingPlayers(finalMessage, pp -> pp.getServer().getInfo().getName().equals(localServerName));

        ChatLoggingManager.logMessage(context, ChannelType.LOCAL);
    }

    public static void sendStaffMessage(CommandSender sender, String message) throws InvalidContextError {
        sendStaffMessage(new Context(sender, message));
    }

    public static void sendStaffMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "staff-chat");

        sendToMatchingPlayers(finalMessage,
                pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_STAFFCHAT_VIEW));

        ChatLoggingManager.logMessage(context, ChannelType.STAFF);
    }

    public static void sendHelpMessage(CommandSender sender, String message) throws InvalidContextError {
        sendHelpMessage(new Context(sender, message));
    }

    public static void sendHelpMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        Optional<String> finalMessage = preProcessMessage(context, "helpop");

        sendToMatchingPlayers(finalMessage, pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP_VIEW));

        ChatLoggingManager.logMessage(context, ChannelType.HELP);
    }

    public static void sendJoinMessage(CommandSender sender) throws InvalidContextError {
        sendJoinMessage(new Context(sender));
    }

    public static void sendJoinMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("joinmessage", context);

        sendToMatchingPlayers(finalMessage);

        ChatLoggingManager.logMessage(finalMessage);
    }

    public static void sendLeaveMessage(CommandSender sender) throws InvalidContextError {
        sendLeaveMessage(new Context(sender));
    }

    public static void sendLeaveMessage(BungeeChatContext context) throws InvalidContextError {
        context.require(BungeeChatContext.HAS_SENDER);

        String finalMessage = PlaceHolderUtil.getFullFormatMessage("leavemessage", context);

        sendToMatchingPlayers(finalMessage);

        ChatLoggingManager.logMessage(finalMessage);
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
        ProxiedPlayer player = Account.toProxiedPlayer(playerAccount);
        String message = context.getMessage().get();

        if (PermissionManager.hasPermission(player, Permission.USE_COLORED_CHAT)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
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
            Predicate<? super ProxiedPlayer>... playerFilters) {
        if (finalMessage.isPresent()) {
            sendToMatchingPlayers(finalMessage.get(), playerFilters);
        }
    }

    @SafeVarargs
    @SuppressWarnings("deprecation")
    public static void sendToMatchingPlayers(String finalMessage, Predicate<? super ProxiedPlayer>... playerFilters) {
        Stream<ProxiedPlayer> stream = ProxyServer.getInstance().getPlayers().stream();

        for (Predicate<? super ProxiedPlayer> playerFilter : playerFilters) {
            stream = stream.filter(playerFilter);
        }

        stream.forEach(pp -> pp.sendMessage(finalMessage));
    }
}

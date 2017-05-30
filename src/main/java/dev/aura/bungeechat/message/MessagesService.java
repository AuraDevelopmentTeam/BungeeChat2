package dev.aura.bungeechat.message;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.exception.InvalidContextException;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.filter.SwearWordsFilter;
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
    public static void sendPrivateMessage(CommandSender sender, ProxiedPlayer target, String message) {
        sendPrivateMessage(new Context(sender, target, message));
    }

    public static void sendPrivateMessage(BungeeChatContext context) {
        if (!context.hasSender())
            throw new InvalidContextException("Context requires a sender!");
        if (!context.hasTarget())
            throw new InvalidContextException("Context requires a target!");
        if (!context.hasMessage())
            throw new InvalidContextException("Context requires a message!");
    }

    public static void sendChannelMessage(CommandSender sender, ChannelType channel, String message) {
        sendChannelMessage(new Context(sender, message), channel);
    }

    public static void sendChannelMessage(BungeeChatContext context, ChannelType channel) {
        if (!context.hasPlayer())
            throw new InvalidContextException("Context requires a player!");
        if (!context.hasMessage())
            throw new InvalidContextException("Context requires a message!");

        switch (channel) {
        case GLOBAL:
            sendGlobalMessage(context);
            break;
        case LOCAL:
            // TODO
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
            // TODO
            break;
        }
    }

    public static String preProcessMessage(BungeeChatContext contex, Optional<BungeeChatAccount> account,
            String format) {
        ProxiedPlayer player = Account.toProxiedPlayer(account.get());
        String message = contex.getMessage().get();

        if (PermissionManager.hasPermission(player, Permission.USE_COLORED_CHAT)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        // TODO: new filter logic
        if (ModuleManager.isModuleActive(ModuleManager.ANTI_SWEAR_MODULE)
                && !PermissionManager.hasPermission(player, Permission.BYPASS_ANTI_SWEAR)) {
            message = SwearWordsFilter.replaceSwearWords(message);
        }

        contex.setMessage(message);

        return PlaceHolderUtil.getFullMessage(format, contex);
    }

    @SafeVarargs
    @SuppressWarnings("deprecation")
    public static void sendToMatchingPlayers(String finalMessage, Predicate<? super ProxiedPlayer>... filters) {
        Stream<ProxiedPlayer> stream = ProxyServer.getInstance().getPlayers().stream();

        for (Predicate<? super ProxiedPlayer> filter : filters) {
            stream = stream.filter(filter);
        }

        stream.forEach(pp -> pp.sendMessage(finalMessage));
    }

    private static void sendGlobalMessage(BungeeChatContext contex) {
        String finalMessage = preProcessMessage(contex, contex.getPlayer(), "global");

        sendToMatchingPlayers(finalMessage);
    }

    private static void sendStaffMessage(BungeeChatContext contex) {
        String finalMessage = preProcessMessage(contex, contex.getPlayer(), "staff");

        sendToMatchingPlayers(finalMessage,
                pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_STAFFCHAT_VIEW));
    }

    private static void sendHelpMessage(BungeeChatContext contex) {
        String finalMessage = preProcessMessage(contex, contex.getPlayer(), "helpop");

        sendToMatchingPlayers(finalMessage, pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP_VIEW));
    }
}

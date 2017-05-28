package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.module.AntiSwearModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GlobalChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if (AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.GLOBAL) && !ChatUtils.isCommand(message)) {

            e.setCancelled(true);

            if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                message = ChatColor.translateAlternateColorCodes('&', message);
            }

            if (ModuleManager.getActiveModules().contains(new AntiSwearModule()) &&
                    !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR))
                message = SwearWordsFilter.replaceSwearWords(message);

            String Format = Config.get().getString("Formats.global");
            Format = PlaceHolderManager.processMessage(Format, new Context(sender, message));

            ProxyServer.getInstance().broadcast(Format);

            return;
        }

        if (Config.get().getBoolean("Settings.Features.GlobalChat.Symbol.enabled")) {
            if (message.startsWith(Config.get().getString("Settings.Features.GlobalChat.Symbol.symbol")) &&
                    Config.get().getString("Settings.Features.GlobalChat.Symbol.symbol").equals("/")) {

                e.setCancelled(true);
                message = message.replaceFirst(Config.get().getString("Settings.Features.GlobalChat.Symbol.symbol"), "");

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                }

                if (ModuleManager.getActiveModules().contains(new AntiSwearModule()) &&
                        !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR))
                    message = SwearWordsFilter.replaceSwearWords(message);

                String Format = Config.get().getString("Formats.global");
                Format = PlaceHolderManager.processMessage(Format, new Context(sender, message));

                ProxyServer.getInstance().broadcast(Format);
            }
        }
    }
}

package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GlobalChatListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if (AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.GLOBAL)
                && !ChatUtils.isCommand(message)) {

            e.setCancelled(true);

            if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                message = ChatColor.translateAlternateColorCodes('&', message);
            }

            if (ModuleManager.getActiveModules().contains(ModuleManager.ANTI_SWEAR_MODULE)
                    && !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR)) {
                message = SwearWordsFilter.replaceSwearWords(message);
            }

            String Format = PlaceHolderUtil.getFullMessage("global", new Context(sender, message));

            ProxyServer.getInstance().broadcast(Format);

            return;
        }

        if (ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("Symbol.enabled")) {
            String symbol = ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getString("Symbol.symbol");

            if (message.startsWith(symbol) && symbol.equals("/")) {
                e.setCancelled(true);
                message = message.replaceFirst(symbol, "");

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                }

                if (ModuleManager.getActiveModules().contains(ModuleManager.ANTI_SWEAR_MODULE)
                        && !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR)) {
                    message = SwearWordsFilter.replaceSwearWords(message);
                }

                String Format = PlaceHolderUtil.getFullMessage("global", new Context(sender, message));

                ProxyServer.getInstance().broadcast(Format);
            }
        }
    }
}

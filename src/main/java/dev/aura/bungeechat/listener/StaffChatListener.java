package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffChatListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if (AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.STAFF)
                && !ChatUtils.isCommand(message)) {

            e.setCancelled(true);

            if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                message = ChatColor.translateAlternateColorCodes('&', message);
            }

            String Format = PlaceHolderUtil.getFullMessage("staffchat", new Context(sender, message));

            ProxyServer.getInstance().getPlayers().stream().filter(pp -> PermissionManager.hasPermission(pp,
                    Permission.COMMAND_STAFFCHAT_VIEW)).forEach(pp -> pp.sendMessage(Format));

        }
    }

}

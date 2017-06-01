package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveMessageListener implements Listener {
    @EventHandler
    public void onPlayerChat(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if (PermissionManager.hasPermission(player, Permission.MESSAGE_LEAVE)) {
            MessagesService.sendLeaveMessage(player);
        }
    }
}

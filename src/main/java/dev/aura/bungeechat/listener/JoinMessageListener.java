package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinMessageListener implements Listener {
    @EventHandler
    public void onPlayerChat(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if (PermissionManager.hasPermission(player, Permission.MESSAGE_JOIN)) {
            MessagesService.sendJoinMessage(player);
        }
    }
}

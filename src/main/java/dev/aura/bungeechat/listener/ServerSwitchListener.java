package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerSwitchListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerServerSwitch(ServerSwitchEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if (PermissionManager.hasPermission(player, Permission.MESSAGE_SWITCH)) {
            MessagesService.sendSwitchMessage(player);
        }
    }
}

package dev.aura.bungeechat.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerSwitchListener implements Listener {
    private static final List<UUID> skipPlayer = new LinkedList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerServerSwitch(ServerSwitchEvent e) {
        ProxiedPlayer player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (skipPlayer.contains(uuid)) {
            skipPlayer.remove(uuid);

            return;
        }

        if (PermissionManager.hasPermission(player, Permission.MESSAGE_SWITCH)) {
            MessagesService.sendSwitchMessage(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        skipPlayer.add(e.getPlayer().getUniqueId());
    }
}

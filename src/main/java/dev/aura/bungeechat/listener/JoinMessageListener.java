package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class JoinMessageListener implements Listener {
  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(BungeeChatJoinEvent e) {
    ProxiedPlayer player = e.getPlayer();

    if (!PermissionManager.hasPermission(player, Permission.MESSAGE_JOIN)) return;

    MessagesService.sendJoinMessage(player);
  }
}

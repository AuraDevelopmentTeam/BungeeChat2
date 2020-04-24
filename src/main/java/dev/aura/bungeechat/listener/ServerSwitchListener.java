package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.event.BungeeChatServerSwitchEvent;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {
  @EventHandler
  public void onPlayerServerSwitch(BungeeChatServerSwitchEvent event) {
    ProxiedPlayer player = event.getPlayer();

    if (PermissionManager.hasPermission(player, Permission.MESSAGE_SWITCH)) {
      MessagesService.sendSwitchMessage(player, getServerInfo(event));
    }
  }

  private static ServerInfo getServerInfo(BungeeChatServerSwitchEvent event) {
    try {
      return event.getFrom();
    } catch (NoSuchMethodError e) {
      return null;
    }
  }
}

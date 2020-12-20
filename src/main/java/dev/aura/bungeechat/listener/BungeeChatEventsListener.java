package dev.aura.bungeechat.listener;

import com.google.common.collect.MapMaker;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.event.BungeeChatLeaveEvent;
import dev.aura.bungeechat.event.BungeeChatServerSwitchEvent;
import java.util.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeChatEventsListener implements Listener {
  private static final Map<UUID, ProxiedPlayer> primaryConnections =
      new MapMaker().weakValues().makeMap();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerServerSwitch(ServerSwitchEvent e) {
    ProxiedPlayer player = e.getPlayer();
    UUID uuid = player.getUniqueId();

    boolean isServerSwitch;
    synchronized (primaryConnections) {
      ProxiedPlayer connection = primaryConnections.getOrDefault(uuid, null);
      if (connection != null && connection != player) {
        // player is already connected through another connection and join event was called
        return;
      }
      isServerSwitch = connection != null;
      if (connection == null)
        primaryConnections.put(uuid, player); // set current connection as primary for this uuid
    }

    if (isServerSwitch) {
      ProxyServer.getInstance()
          .getPluginManager()
          .callEvent(new BungeeChatServerSwitchEvent(player, e));
    } else {
      ProxyServer.getInstance().getPluginManager().callEvent(new BungeeChatJoinEvent(player));
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerLeave(PlayerDisconnectEvent e) {
    ProxiedPlayer player = e.getPlayer();
    UUID uuid = player.getUniqueId();

    synchronized (primaryConnections) {
      ProxiedPlayer connection = primaryConnections.getOrDefault(uuid, null);
      if (connection
          != player) // call BungeeChatLeaveEvent only when primary connection is disconnected
      return;
      primaryConnections.remove(uuid);
    }

    ProxyServer.getInstance().getPluginManager().callEvent(new BungeeChatLeaveEvent(player));
  }
}

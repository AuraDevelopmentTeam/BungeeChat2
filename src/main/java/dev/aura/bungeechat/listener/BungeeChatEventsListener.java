package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.event.BungeeChatLeaveEvent;
import dev.aura.bungeechat.event.BungeeChatServerSwitchEvent;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeChatEventsListener implements Listener {
  private static final Map<UUID, WeakReference<ProxiedPlayer>> primaryConnections = new HashMap<>();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerServerSwitch(ServerSwitchEvent e) {
    final boolean isNewConnection;
    final ProxiedPlayer player = e.getPlayer();
    final UUID uuid = player.getUniqueId();

    synchronized (primaryConnections) {
      final ProxiedPlayer connection =
          primaryConnections.getOrDefault(uuid, new WeakReference<>(null)).get();
      isNewConnection = connection == null;

      // player is already connected through another connection and join event was called
      if (!isNewConnection && (connection != player)) return;

      // set current connection as primary for this uuid
      if (isNewConnection) primaryConnections.put(uuid, new WeakReference<>(player));
    }

    if (isNewConnection) {
      ProxyServer.getInstance().getPluginManager().callEvent(new BungeeChatJoinEvent(player));
    } else {
      ProxyServer.getInstance()
          .getPluginManager()
          .callEvent(new BungeeChatServerSwitchEvent(player, e));
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerLeave(PlayerDisconnectEvent e) {
    final ProxiedPlayer player = e.getPlayer();
    final UUID uuid = player.getUniqueId();

    synchronized (primaryConnections) {
      final ProxiedPlayer connection =
          primaryConnections.getOrDefault(uuid, new WeakReference<>(null)).get();

      // call BungeeChatLeaveEvent only when primary connection is disconnected
      if (connection != player) return;

      primaryConnections.remove(uuid);
      // Clean any orphaned entries
      primaryConnections.values().removeIf(ref -> ref.get() == null);
    }

    ProxyServer.getInstance().getPluginManager().callEvent(new BungeeChatLeaveEvent(player));
  }
}

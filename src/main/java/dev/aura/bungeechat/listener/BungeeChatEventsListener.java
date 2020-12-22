package dev.aura.bungeechat.listener;

import com.google.common.collect.MapMaker;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.event.BungeeChatLeaveEvent;
import dev.aura.bungeechat.event.BungeeChatServerSwitchEvent;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@SuppressFBWarnings(
    value = "JLM_JSR166_UTILCONCURRENT_MONITORENTER",
    justification =
        "Synchronization is correct as is. The map produced by MapMaker is a ConcurrentMap, even"
            + "thought that concurrency is not being used. There is no way to get a non concurrent"
            + "map from MapMaker")
public class BungeeChatEventsListener implements Listener {
  private static final Map<UUID, ProxiedPlayer> primaryConnections =
      new MapMaker().weakValues().concurrencyLevel(1).makeMap();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerServerSwitch(ServerSwitchEvent e) {
    final boolean isNewConnection;
    final ProxiedPlayer player = e.getPlayer();
    final UUID uuid = player.getUniqueId();

    synchronized (primaryConnections) {
      final ProxiedPlayer connection = primaryConnections.get(uuid);
      isNewConnection = connection == null;

      // player is already connected through another connection and join event was called
      if (!isNewConnection && (connection != player)) return;

      // set current connection as primary for this uuid
      if (isNewConnection) primaryConnections.put(uuid, player);
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
      final ProxiedPlayer connection = primaryConnections.get(uuid);

      // call BungeeChatLeaveEvent only when primary connection is disconnected
      if (connection != player) return;

      primaryConnections.remove(uuid);
    }

    ProxyServer.getInstance().getPluginManager().callEvent(new BungeeChatLeaveEvent(player));
  }
}

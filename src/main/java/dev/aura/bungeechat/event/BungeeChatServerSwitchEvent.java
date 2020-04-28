package dev.aura.bungeechat.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when a player has changed servers.
 *
 * <p>Used by BungeeChat internally to make sure people joining while they are online don't cause
 * issues.
 */
@Data
@RequiredArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BungeeChatServerSwitchEvent extends Event {
  /** Player whom the server is for. */
  private final ProxiedPlayer player;
  /** Server the player is switch from. */
  private final ServerInfo from;

  public BungeeChatServerSwitchEvent(ProxiedPlayer player, ServerSwitchEvent event) {
    this(player, getServerInfo(event));
  }

  private static ServerInfo getServerInfo(ServerSwitchEvent event) {
    try {
      return event.getFrom();
    } catch (NoSuchMethodError e) {
      return null;
    }
  }
}

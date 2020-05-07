package dev.aura.bungeechat.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when a player has left the proxy, it is not safe to call any methods that perform an
 * action on the passed player instance.
 *
 * <p>Used by BungeeChat internally to make sure people joining while they are online don't cause
 * issues.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BungeeChatLeaveEvent extends Event {
  /** Player disconnecting. */
  private final ProxiedPlayer player;
}

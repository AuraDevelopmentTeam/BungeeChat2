package dev.aura.bungeechat.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;

public class BungeeChatLeaveEvent extends PlayerDisconnectEvent {
  public BungeeChatLeaveEvent(ProxiedPlayer player) {
    super(player);
  }
}

package dev.aura.bungeechat.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;

public class BungeeChatJoinEvent extends PostLoginEvent {
  public BungeeChatJoinEvent(ProxiedPlayer player) {
    super(player);
  }
}

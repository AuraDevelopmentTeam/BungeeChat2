package dev.aura.bungeechat.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;

public class BungeeChatServerSwitchEvent extends ServerSwitchEvent {
    public BungeeChatServerSwitchEvent(ProxiedPlayer player) {
        super(player);
    }
}

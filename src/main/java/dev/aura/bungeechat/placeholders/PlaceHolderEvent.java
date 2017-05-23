package dev.aura.bungeechat.placeholders;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PlaceHolderEvent extends Event {
    @Getter
    private final String message;
    @Getter
    private final ProxiedPlayer player;

    public PlaceHolderEvent(ProxiedPlayer player, String message){
        this.message = message;
        this.player = player;
    }
}

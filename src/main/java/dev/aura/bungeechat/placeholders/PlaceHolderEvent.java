package dev.aura.bungeechat.placeholders;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

public class PlaceHolderEvent extends Event {
    @Getter
    private final String message;

    public PlaceHolderEvent(String message){
        this.message = message;
    }
}

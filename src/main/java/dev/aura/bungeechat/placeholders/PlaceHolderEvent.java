package dev.aura.bungeechat.placeholders;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PlaceHolderEvent extends Event {
    @Getter
    private final String message;
    @Getter
    private final ProxiedPlayer sender;
    @Getter
    private final ProxiedPlayer target;

    public PlaceHolderEvent(ProxiedPlayer sender, ProxiedPlayer target, String message){
        this.message = message;
        this.sender = sender;
        this.target = target;
    }

    public void registerPlaceHolder(String placeholder, String replacement) {
        message.replace(placeholder, replacement);
    }
}

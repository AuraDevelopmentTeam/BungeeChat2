package dev.aura.bungeechat.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlaceHolderEvent extends Event {
    private final ProxiedPlayer sender;
    private final ProxiedPlayer target;
    private String message;

    public void registerPlaceHolder(String placeholder, String replacement) {
        message = message.replace(placeholder, replacement);
    }
}

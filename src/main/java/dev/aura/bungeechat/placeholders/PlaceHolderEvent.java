package dev.aura.bungeechat.placeholders;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlaceHolderEvent extends Event {
    private final ProxiedPlayer sender;
    private final ProxiedPlayer target;
    private final String message;

    public void registerPlaceHolder(String placeholder, String replacement) {
        message.replace(placeholder, replacement);
    }
}

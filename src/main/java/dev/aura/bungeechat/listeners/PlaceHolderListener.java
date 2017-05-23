package dev.aura.bungeechat.listeners;

import dev.aura.bungeechat.placeholders.PlaceHolderEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

public class PlaceHolderListener implements Listener {

    public void onPlaceHolerEventCall(PlaceHolderEvent event) {
        ProxiedPlayer player = event.getPlayer();
        event.getMessage().replace("%player_name%", player.getName());
    }

}

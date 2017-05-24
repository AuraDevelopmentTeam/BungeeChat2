package dev.aura.bungeechat.listeners;

import dev.aura.bungeechat.BungeeChat;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public class PlayerConnectionListeners implements Listener {

    @EventHandler
    public void onPlayerConnect(PostLoginEvent event) throws IOException, ClassNotFoundException {
        BungeeChat.loadAccount(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) throws IOException, ClassNotFoundException {
        BungeeChat.saveAccount(BungeeChat.getUserAccount(event.getPlayer().getUniqueId()));
    }

}

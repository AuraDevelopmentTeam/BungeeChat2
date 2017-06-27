package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.MOTDListener;
import net.md_5.bungee.api.ProxyServer;

public class MOTDModule extends Module {
    private MOTDListener motdListener;

    @Override
    public String getName() {
        return "MOTD";
    }

    @Override
    public void onEnable() {
        motdListener = new MOTDListener();

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), motdListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(motdListener);
    }
}

package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.ServerSwitchListener;
import net.md_5.bungee.api.ProxyServer;

public class ServerSwitchModule extends Module {
    private ServerSwitchListener serverSwitchListener;

    @Override
    public String getName() {
        return "ServerSwitchMessages";
    }

    @Override
    public void onEnable() {
        serverSwitchListener = new ServerSwitchListener();

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), serverSwitchListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(serverSwitchListener);
    }
}

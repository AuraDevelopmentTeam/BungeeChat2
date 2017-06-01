package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.LeaveMessageListener;
import net.md_5.bungee.api.ProxyServer;

public class LeaveMessageModule implements Module {
    private LeaveMessageListener LeaveMessageListener;

    @Override
    public String getName() {
        return "LeaveMessage";
    }

    @Override
    public void onEnable() {
        LeaveMessageListener = new LeaveMessageListener();

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), LeaveMessageListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(LeaveMessageListener);
    }
}

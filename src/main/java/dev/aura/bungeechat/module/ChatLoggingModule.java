package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.ChatLoggingListener;
import net.md_5.bungee.api.ProxyServer;

public class ChatLoggingModule implements Module {
    private ChatLoggingListener chatLoggingListener;

    @Override
    public String getName() {
        return "ChatLogging";
    }

    @Override
    public void onEnable() {
        chatLoggingListener = new ChatLoggingListener();

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), chatLoggingListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(chatLoggingListener);
    }
}

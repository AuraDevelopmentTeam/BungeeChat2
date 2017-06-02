package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.chatlog.ChatLoggingManager;
import dev.aura.bungeechat.chatlog.ConsoleLogger;
import dev.aura.bungeechat.listener.ChatLoggingListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

public class ChatLoggingModule implements Module {
    private ChatLoggingListener chatLoggingListener;

    private ConsoleLogger consoleLogger;

    @Override
    public String getName() {
        return "ChatLogging";
    }

    @Override
    public void onEnable() {
        Configuration section = getModuleSection();

        if (section.getBoolean("console")) {
            consoleLogger = new ConsoleLogger();

            ChatLoggingManager.addLogger(consoleLogger);
        }

        chatLoggingListener = new ChatLoggingListener();

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), chatLoggingListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(chatLoggingListener);

        if (chatLoggingListener != null)
            ChatLoggingManager.removeLogger(consoleLogger);
    }
}

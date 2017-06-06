package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.command.ChatLockCommand;
import dev.aura.bungeechat.filter.ChatLockFilter;
import net.md_5.bungee.api.ProxyServer;

public class ChatLockModule extends Module {
    private ChatLockCommand chatLockCommand;

    @Override
    public String getName() {
        return "ChatLock";
    }

    @Override
    public void onEnable() {
        chatLockCommand = new ChatLockCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), chatLockCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(chatLockCommand);

        disableChatLock();
    }

    public void enableChatLock() {
        FilterManager.addFilter(getName(), new ChatLockFilter());
    }

    public void disableChatLock() {
        FilterManager.removeFilter(getName());
    }

}

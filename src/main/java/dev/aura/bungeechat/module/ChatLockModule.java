package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.command.ChatLockCommand;
import dev.aura.bungeechat.filter.ChatLockFilter;
import lombok.experimental.Delegate;
import net.md_5.bungee.api.ProxyServer;

public class ChatLockModule extends Module {
    private ChatLockCommand chatLockCommand;
    @Delegate(excludes = BungeeChatFilter.class)
    private ChatLockFilter chatLockFilter;

    @Override
    public String getName() {
        return "ChatLock";
    }

    @Override
    public void onEnable() {
        chatLockCommand = new ChatLockCommand(this);
        chatLockFilter = new ChatLockFilter();

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), chatLockCommand);
        FilterManager.addFilter(getName(), chatLockFilter);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(chatLockCommand);
        FilterManager.removeFilter(getName());
    }
}

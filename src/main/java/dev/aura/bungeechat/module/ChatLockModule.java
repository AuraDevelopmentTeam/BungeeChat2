package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.ChatLockFilter;

public class ChatLockModule implements Module {
    @Override
    public String getName() {
        return "ChatLock";
    }

    @Override
    public void onEnable() {
        // TODO Register Command
    }

    @Override
    public void onDisable() {
        // TODO Unregister Command
        
        disableChatLock();
    }
    
    public void enableChatLock() {
        FilterManager.addFilter(getName(), new ChatLockFilter());
    }
    
    public void disableChatLock() {
        FilterManager.removeFilter(getName());
    }
}

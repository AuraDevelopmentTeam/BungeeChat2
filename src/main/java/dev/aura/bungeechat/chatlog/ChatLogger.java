package dev.aura.bungeechat.chatlog;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;

public interface ChatLogger {
    public void logMessage(BungeeChatContext context, ChannelType channel);
    
    public void logCommand(BungeeChatAccount account, String command);
}

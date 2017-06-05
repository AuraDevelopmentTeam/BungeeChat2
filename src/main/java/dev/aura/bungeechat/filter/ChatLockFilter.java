package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.permission.PermissionManager;

public class ChatLockFilter implements BungeeChatFilter {
    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK_BYPASS))
            return message;
        else
            throw new BlockMessageException(Message.CHAT_IS_DISABLED.get(sender, message));
    }

    @Override
    public int getPriority() {
        return FilterManager.LOCK_CHAT_FILTER_PRIORITY;
    }
}

package dev.aura.bungeechat.api.filter;

import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;

public interface BungeeChatFilter {
    public String applyFilter(BungeeChatAccount sender, String message);
}

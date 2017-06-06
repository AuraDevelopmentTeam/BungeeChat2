package dev.aura.bungeechat.api.account;

import java.util.UUID;

public interface BungeeChatAccountStorage {
    public void save(BungeeChatAccount account);

    public BungeeChatAccount load(UUID uuid);
}

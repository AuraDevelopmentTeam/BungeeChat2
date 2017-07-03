package dev.aura.bungeechat.api.account;

import java.util.UUID;

public interface BungeeChatAccountStorage {
    public void save(BungeeChatAccount account);

    /**
     * Load a player with the given UUID from the data source this class
     * represents.
     *
     * @param uuid
     *            UUID of the player to load.
     * @return A tuple of the player and a boolean that specifies whether it
     *         needs to be saved right after.
     */
    public AccountInfo load(UUID uuid);

    default public boolean requiresConsoleAccountSave() {
        return false;
    }
}

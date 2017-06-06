package dev.aura.bungeechat.api.account;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import dev.aura.bungeechat.api.enums.AccountType;
import dev.aura.bungeechat.api.enums.ChannelType;

public interface BungeeChatAccount {
    public UUID getUniqueId();

    default public AccountType getAccountType() {
        return AccountType.PLAYER;
    }

    public ChannelType getChannelType();

    public boolean isVanished();

    public boolean hasMessangerEnabled();

    public boolean hasSocialSpyEnabled();

    public BlockingQueue<UUID> getIgnored();

    default public boolean hasIgnored(UUID uuid) {
        return getIgnored().contains(uuid);
    }

    default public boolean hasIgnored(BungeeChatAccount account) {
        return this.hasIgnored(getUniqueId());
    }

    public String getName();

    default public String getDisplayName() {
        return getName();
    }

    public int getPing();

    public String getServerName();

    public String getServerIP();

    public Optional<String> getStoredPrefix();

    public Optional<String> getStoredSuffix();

    public void setChannelType(ChannelType channelType);

    public void setVanished(boolean vanished);

    public void setMessanger(boolean messanger);

    public void setSocialspy(boolean socialspy);

    default public void toggleVanished() {
        setVanished(!isVanished());
    }

    default public void toggleMessanger() {
        setMessanger(!hasSocialSpyEnabled());
    }

    default public void toggleSocialSpy() {
        setSocialspy(!hasSocialSpyEnabled());
    }

    public void addIgnore(UUID uuid);

    default public void addIgnore(BungeeChatAccount account) {
        this.addIgnore(getUniqueId());
    }

    public void removeIgnore(UUID uuid);

    default public void removeIgnore(BungeeChatAccount account) {
        this.removeIgnore(getUniqueId());
    }

    public void setStoredPrefix(Optional<String> newPrefix);

    public void setStoredSuffix(Optional<String> newSuffix);
}

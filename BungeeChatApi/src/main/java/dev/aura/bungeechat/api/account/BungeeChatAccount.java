package dev.aura.bungeechat.api.account;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
    
    public boolean hasLocalSpyEnabled();

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

    public Timestamp getMutedUntil();

    default public boolean isMuted() {
        return getMutedUntil().after(new Timestamp(System.currentTimeMillis()));
    }

    public Optional<String> getStoredPrefix();

    public Optional<String> getStoredSuffix();

    public void setChannelType(ChannelType channelType);

    public void setVanished(boolean vanished);

    public void setMessanger(boolean messanger);

    public void setSocialSpy(boolean socialSpy);
    
    public void setLocalSpy(boolean localSpy);

    default public void toggleVanished() {
        setVanished(!isVanished());
    }

    default public void toggleMessanger() {
        setMessanger(!hasSocialSpyEnabled());
    }

    default public void toggleSocialSpy() {
        setSocialSpy(!hasSocialSpyEnabled());
    }
    
    default public void toggleLocalSpy() {
        setLocalSpy(!hasLocalSpyEnabled());
    }

    public void addIgnore(UUID uuid);

    default public void addIgnore(BungeeChatAccount account) {
        this.addIgnore(getUniqueId());
    }

    public void removeIgnore(UUID uuid);

    default public void removeIgnore(BungeeChatAccount account) {
        this.removeIgnore(getUniqueId());
    }

    public void setMutedUntil(Timestamp mutedUntil);

    default public void setMutedUntil(long mutedUntilMillis) {
        setMutedUntil(new Timestamp(mutedUntilMillis));
    }

    default void mutePermanetly() {
        setMutedUntil(Timestamp.valueOf("9999-12-31 23:59:59"));
    }

    default void muteFor(long amount, TimeUnit timeUnit) {
        setMutedUntil(System.currentTimeMillis() + timeUnit.toMillis(amount));
    }

    default void unmute() {
        setMutedUntil(0L);
    }

    public void setStoredPrefix(Optional<String> newPrefix);

    public void setStoredSuffix(Optional<String> newSuffix);
}

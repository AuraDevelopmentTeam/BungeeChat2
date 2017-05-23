package dev.aura.bungeechat.api.interfaces;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;

public interface BungeeChatAccount {
	public UUID getUniqueId();

	public ChannelType getChannelType();

	public boolean isVanished();

	public boolean hasMessangerEnabled();

	public boolean hasSocialSpyEnabled();

	public CopyOnWriteArrayList<UUID> getIgnored();

	public boolean hasIgnored(UUID uuid);

	default public boolean hasIgnored(BungeeChatAccount account) {
		return this.hasIgnored(getUniqueId());
	}

	public void setChannelType(ChannelType channelType);

	public void toggleVanished();

	public void toggleMessanger();

	public void toggleSocialSpy();

	public void addIgnore(UUID uuid);

	default public void addIgnore(BungeeChatAccount account) {
		this.addIgnore(getUniqueId());
	}

	public void removeIgnore(UUID uuid);

	default public void removeIgnore(BungeeChatAccount account) {
		this.removeIgnore(getUniqueId());
	}
}

package dev.aura.bungeechat;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("unused")
public class Account implements BungeeChatAccount {
	private UUID uuid;
	@Getter
	@Setter
	private ChannelType channelType;
	@Getter
	@Setter
	private boolean vanished;
	@Getter
	@Setter
	private boolean messanger;
	@Getter
	@Setter
	private boolean socialspy;
	@Getter
	private CopyOnWriteArrayList<UUID> ignored;

	public Account(ProxiedPlayer player) {
		this(player.getUniqueId());
	}

	public Account(UUID uuid) {
		this.uuid = uuid;
		channelType = ChannelType.NONE;
		vanished = false;
		messanger = true;
		socialspy = false;
		ignored = new CopyOnWriteArrayList<>();
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public boolean hasMessangerEnabled() {
		return messanger;
	}

	@Override
	public boolean hasSocialSpyEnabled() {
		return socialspy;
	}

	@Override
	public boolean hasIgnored(UUID uuid) {
		return ignored.contains(uuid);
	}

	public boolean hasIgnored(ProxiedPlayer player) {
		return this.hasIgnored(player.getUniqueId());
	}

	@Override
	public void toggleVanished() {
		vanished = !vanished;
	}

	@Override
	public void toggleMessanger() {
		messanger = !messanger;
	}

	@Override
	public void toggleSocialSpy() {
		socialspy = !socialspy;
	}

	@Override
	public void addIgnore(UUID uuid) {
		ignored.add(uuid);
	}

	public void addIgnore(ProxiedPlayer player) {
		this.addIgnore(player.getUniqueId());
	}

	@Override
	public void removeIgnore(UUID uuid) {
		ignored.remove(uuid);
	}

	public void removeIgnore(ProxiedPlayer player) {
		this.removeIgnore(player.getUniqueId());
	}
}

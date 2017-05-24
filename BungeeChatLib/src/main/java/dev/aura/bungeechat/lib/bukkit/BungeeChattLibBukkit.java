package dev.aura.bungeechat.lib.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;

public class BungeeChattLibBukkit extends JavaPlugin implements BungeeChatApi {
	@Override
	public ServerType getServerType() {
		return ServerType.BUKKIT;
	}

	@Override
	public boolean hasPermission(BungeeChatAccount account, Permission permission) {
		// TODO: Get via channel messages!
		throw new UnsupportedOperationException("Not implemented!");
	}
}

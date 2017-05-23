package dev.aura.bungeechat.lib.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ServerType;

public class BungeeChattLibBukkit extends JavaPlugin implements BungeeChatApi {
	@Override
	public ServerType getServerType() {
		return ServerType.BUKKIT;
	}
}

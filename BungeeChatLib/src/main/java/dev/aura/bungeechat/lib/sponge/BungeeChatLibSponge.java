package dev.aura.bungeechat.lib.sponge;

import org.spongepowered.api.plugin.Plugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;

@Plugin(id = BungeeChatApi.ID_LIB, name = BungeeChatApi.NAME_LIB, version = BungeeChatApi.VERSION, description = BungeeChatApi.DESCRIPTION_LIB, url = BungeeChatApi.URL, authors = {
		BungeeChatApi.AUTHOR_SHAWN, BungeeChatApi.AUTHOR_BRAINSTONE })
public class BungeeChatLibSponge implements BungeeChatApi {
	@Override
	public ServerType getServerType() {
		return ServerType.SPONGE;
	}
	
	@Override
	public boolean hasPermission(BungeeChatAccount account, Permission permission) {
		// TODO: Get via channel messages!
		throw new UnsupportedOperationException("Not implemented!");
	}
}

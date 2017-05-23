package dev.aura.bungeechat.lib.sponge;

import org.spongepowered.api.plugin.Plugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ServerType;

@Plugin(id = BungeeChatApi.ID_LIB, name = BungeeChatApi.NAME_LIB, version = BungeeChatApi.VERSION, description = BungeeChatApi.DESCRIPTION_LIB, url = BungeeChatApi.URL, authors = {
		BungeeChatApi.AUTHOR_SHAWN, BungeeChatApi.AUTHOR_BRAINSTONE })
public class BungeeChatLibSponge implements BungeeChatApi {
	@Override
	public ServerType getServerType() {
		return ServerType.SPONGE;
	}
}

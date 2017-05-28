package dev.aura.bungeechat.lib.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;
import lombok.Getter;

@Plugin(id = BungeeChatApi.ID_LIB, name = BungeeChatApi.NAME_LIB, version = BungeeChatApi.VERSION, description = BungeeChatApi.DESCRIPTION_LIB, url = BungeeChatApi.URL, authors = {
        BungeeChatApi.AUTHOR_SHAWN, BungeeChatApi.AUTHOR_BRAINSTONE })
public class BungeeChatLibSponge implements BungeeChatApi {
    @Getter
    private static BungeeChatLibSponge instance;

    @Override
    public ServerType getServerType() {
        return ServerType.SPONGE;
    }

    @Override
    public boolean hasPermission(BungeeChatAccount account, Permission permission) {
        // TODO: Get via channel messages!
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        instance = this;
        BungeeChatInstaceHolder.setInstance(instance);
    }
}

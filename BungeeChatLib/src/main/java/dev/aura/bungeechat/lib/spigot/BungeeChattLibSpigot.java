package dev.aura.bungeechat.lib.spigot;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;
import lombok.Getter;

public class BungeeChattLibSpigot extends JavaPlugin implements BungeeChatApi {
    @Getter
    private static BungeeChattLibSpigot instance;

    private File configDir;

    @Override
    public ServerType getServerType() {
        return ServerType.SPIGOT;
    }

    @Override
    public File getConfigFolder() {
        if (configDir == null) {
            configDir = new File(getDataFolder().getParentFile(), "BungeeChat");
            configDir.mkdirs();
        }

        return configDir;
    }

    @Override
    public boolean hasPermission(BungeeChatAccount account, Permission permission) {
        // TODO: Get via channel messages!
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public void onEnable() {
        instance = this;
        BungeeChatInstaceHolder.setInstance(instance);
    }

    @Override
    public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
        // TODO: Send via channel messages!
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public void sendChannelMessage(BungeeChatContext context, ChannelType chanel) throws InvalidContextError {
        // TODO: Send via channel messages!
        throw new UnsupportedOperationException("Not implemented!");
    }
}

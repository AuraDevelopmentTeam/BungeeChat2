package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import net.md_5.bungee.api.ProxyServer;

public abstract class PermissionPluginModule implements BungeeChatModule {
    @Override
    public boolean isEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin(getName()) != null;
    }
}

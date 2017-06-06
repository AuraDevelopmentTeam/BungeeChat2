package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.module.Module;
import net.md_5.bungee.api.ProxyServer;

public abstract class PermissionPluginModule implements Module {
    @Override
    public boolean isEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin(getName()) != null;
    }
}

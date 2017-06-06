package dev.aura.bungeechat.module;

import net.md_5.bungee.api.ProxyServer;

public abstract class PermissionPluginModule implements Module {
    @Override
    public boolean isEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin(getName()) != null;
    }
}

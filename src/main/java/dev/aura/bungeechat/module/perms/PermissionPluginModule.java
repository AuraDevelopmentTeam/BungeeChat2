package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.config.Configuration;
import java.util.Collection;
import net.md_5.bungee.api.ProxyServer;

public abstract class PermissionPluginModule implements BungeeChatModule {
  @Override
  public boolean isEnabled() {
    return forceModule() || isPluginPresent(getName());
  }

  protected boolean forceModule() {
    return getForcedPermissionModules().contains(getName());
  }

  protected static boolean isPluginPresent(String pluginName) {
    return ProxyServer.getInstance().getPluginManager().getPlugin(pluginName) != null;
  }

  protected static Collection<String> getForcedPermissionModules() {
    return Configuration.get().getConfig("PrefixSuffixSettings").getStringList("forceEnable");
  }
}

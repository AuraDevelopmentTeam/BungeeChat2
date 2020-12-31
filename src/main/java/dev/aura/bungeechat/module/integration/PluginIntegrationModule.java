package dev.aura.bungeechat.module.integration;

import dev.aura.bungeechat.module.Module;
import net.md_5.bungee.api.ProxyServer;

public abstract class PluginIntegrationModule extends Module {
  protected static boolean isPluginPresent(String pluginName) {
    return ProxyServer.getInstance().getPluginManager().getPlugin(pluginName) != null;
  }

  @Override
  public boolean isEnabled() {
    return isPluginPresent(getName()) || super.isEnabled();
  }
}

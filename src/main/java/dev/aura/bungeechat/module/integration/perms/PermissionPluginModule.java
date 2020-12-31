package dev.aura.bungeechat.module.integration.perms;

import dev.aura.bungeechat.module.integration.PluginIntegrationModule;

public abstract class PermissionPluginModule extends PluginIntegrationModule {
  @Override
  public boolean isEnabled() {
    return isPluginPresent(getName());
  }
}

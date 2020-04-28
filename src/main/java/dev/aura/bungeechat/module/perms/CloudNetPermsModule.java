package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.CloudNetPermsHook;

public class CloudNetPermsModule extends PermissionPluginModule {
  private CloudNetPermsHook permsHook = null;

  @Override
  public String getName() {
    return "CloudNetAPI";
  }

  @Override
  public boolean isEnabled() {
    if (!super.isEnabled()) return false;

    permsHook = new CloudNetPermsHook();

    return permsHook.permissionsEnabled();
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), permsHook);
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

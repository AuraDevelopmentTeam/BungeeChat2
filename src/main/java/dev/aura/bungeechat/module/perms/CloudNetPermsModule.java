package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.CloudNetPermsHook;

public class CloudNetPermsModule extends PermissionPluginModule {
  @Override
  public String getName() {
    return "CloudNetAPI";
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), new CloudNetPermsHook());
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.CloudNetPerms3Hook;
import dev.aura.bungeechat.util.ClassUtil;

public class CloudNetPerms3Module extends PermissionPluginModule {
  @Override
  public String getName() {
    return "CloudNet3";
  }

  @Override
  public boolean isEnabled() {
    return forceModule()
        || (isPluginPresent("CloudNet-CloudPerms")
            && ClassUtil.doesClassExist(
                "de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsManagement"));
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), new CloudNetPerms3Hook());
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

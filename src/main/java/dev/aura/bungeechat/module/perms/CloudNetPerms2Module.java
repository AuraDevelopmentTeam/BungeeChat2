package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.CloudNetPerms2Hook;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.util.ClassUtil;

public class CloudNetPerms2Module extends PermissionPluginModule {
  private CloudNetPerms2Hook permsHook = null;

  @Override
  public String getName() {
    return "CloudNet2";
  }

  @Override
  public boolean isEnabled() {
    if (!isPluginPresent("CloudNetAPI")
        || !ClassUtil.doesClassExist("de.dytanic.cloudnet.api.CloudAPI")
        || BungeecordModuleManager.CLOUD_NET_PERMS3_MODULE.isEnabled()) return false;

    permsHook = new CloudNetPerms2Hook();

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

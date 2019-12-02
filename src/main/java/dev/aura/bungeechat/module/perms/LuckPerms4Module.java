package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.LuckPerms4Hook;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.util.ClassUtil;

public class LuckPerms4Module extends PermissionPluginModule {
  @Override
  public String getName() {
    return "LuckPerms";
  }

  @Override
  public boolean isEnabled() {
    return super.isEnabled()
        && ClassUtil.doesClassExist("me.lucko.luckperms.LuckPerms")
        && !BungeecordModuleManager.LUCK_PERMS_5_MODULE.isEnabled();
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), new LuckPerms4Hook());
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.config.Configuration;
import dev.aura.bungeechat.hook.LuckPerms5Hook;
import dev.aura.bungeechat.util.ClassUtil;

public class LuckPerms5Module extends PermissionPluginModule {
  @Override
  public String getName() {
    return "LuckPerms5";
  }

  @Override
  public boolean isEnabled() {
    return forceModule()
        || (isPluginPresent("LuckPerms")
            && ClassUtil.doesClassExist("net.luckperms.api.LuckPerms"));
  }

  @Override
  public void onEnable() {
    final boolean fixContext =
        Configuration.get().getBoolean("PrefixSuffixSettings.fixLuckPermsContext");

    HookManager.addHook(getName(), new LuckPerms5Hook(fixContext));
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

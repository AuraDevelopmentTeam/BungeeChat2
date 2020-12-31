package dev.aura.bungeechat.module.integration.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.perms.PowerfulPermsHook;

public class PowerfulPermsModule extends PermissionPluginModule {
  @Override
  public String getName() {
    return "PowerfulPerms";
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), new PowerfulPermsHook());
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}

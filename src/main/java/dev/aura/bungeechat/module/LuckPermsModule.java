package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.LuckPermsHook;

public class LuckPermsModule extends PermissionPluginModule {
    @Override
    public String getName() {
        return "LuckPerms";
    }

    @Override
    public void onEnable() {
        HookManager.addHook(getName(), new LuckPermsHook());
    }

    @Override
    public void onDisable() {
        HookManager.removeHook(getName());
    }
}

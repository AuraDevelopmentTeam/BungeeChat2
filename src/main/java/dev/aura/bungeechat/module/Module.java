package dev.aura.bungeechat.module;

import com.typesafe.config.Config;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.config.Configuration;

public abstract class Module implements BungeeChatModule {
    public static final String MODULE_BASE = "Modules";
    public static final String CONFIG_ENABLED = "enabled";

    public String getConfigBasePath() {
        return MODULE_BASE + '.' + getName();
    }

    public Config getModuleSection() {
        return Configuration.get().atPath(MODULE_BASE);
    }

    @Override
    public boolean isEnabled() {
        return getModuleSection().getBoolean(CONFIG_ENABLED);
    }
}

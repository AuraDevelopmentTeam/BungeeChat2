package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.config.OldConfig;
import net.md_5.bungee.config.Configuration;

public abstract class Module implements BungeeChatModule {
    public static final String MODULE_BASE = "Settings.Modules";
    public static final String CONFIG_ENABLED = "enabled";

    public String getConfigBasePath() {
        return MODULE_BASE + '.' + getName();
    }

    public Configuration getModuleSection() {
        return OldConfig.get().getSection(getConfigBasePath());
    }

    @Override
    public boolean isEnabled() {
        return getModuleSection().getBoolean(CONFIG_ENABLED, false);
    }
}

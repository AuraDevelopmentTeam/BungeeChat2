package dev.aura.bungeechat.module;

import dev.aura.bungeechat.config.Config;
import net.md_5.bungee.config.Configuration;

public interface Module {
    public static final String MODULE_BASE = "Settings.Features";
    public static final String CONFIG_ENABLED = "enabled";

    public String getName();

    default public String getConfigBasePath() {
        return MODULE_BASE + '.' + getName();
    }

    default public Configuration getModuleSection() {
        return Config.get().getSection(getConfigBasePath());
    }

    default public boolean isEnabled() {
        return getModuleSection().getBoolean(CONFIG_ENABLED);
    }

    public void onEnable();

    public void onDisable();
}

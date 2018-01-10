package dev.aura.bungeechat.module;

import com.typesafe.config.Config;
import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.config.Configuration;
import lombok.Getter;

public abstract class Module implements BungeeChatModule {
  public static final String MODULE_BASE = "Modules";
  public static final String CONFIG_ENABLED = "enabled";

  @Getter(lazy = true)
  private final Config moduleSection = generateModuleSection();

  @Override
  public boolean isEnabled() {
    return getModuleSection().getBoolean(CONFIG_ENABLED);
  }

  private Config generateModuleSection() {
    return Configuration.get().getConfig(MODULE_BASE).getConfig(getName());
  }
}

package dev.aura.bungeechat.module;

import com.typesafe.config.Config;
import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.config.Configuration;
import lombok.Setter;

public abstract class Module implements BungeeChatModule {
  @Setter private static boolean test_mode = false;

  public static final String MODULE_BASE = "Modules";
  public static final String CONFIG_ENABLED = "enabled";

  @Override
  public boolean isEnabled() {
    return test_mode ? true : getModuleSection().getBoolean(CONFIG_ENABLED);
  }

  public Config getModuleSection() {
    return Configuration.get().getConfig(MODULE_BASE).getConfig(getName());
  }
}

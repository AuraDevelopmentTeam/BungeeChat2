package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.CapslockFilter;

public class AntiCapslockModule extends Module {
  @Override
  public String getName() {
    return "AntiCapslock";
  }

  @Override
  public void onEnable() {
    FilterManager.addFilter(
        getName(),
        new CapslockFilter(
            getModuleSection().getInt("minimumLetterCount"),
            getModuleSection().getInt("maximumCapsPercentage")));
  }

  @Override
  public void onDisable() {
    FilterManager.removeFilter(getName());
  }
}

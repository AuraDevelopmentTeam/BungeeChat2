package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.SpamFilter;

public class AntiSpamModule extends Module {
  @Override
  public String getName() {
    return "AntiSpam";
  }

  @Override
  public void onEnable() {
    FilterManager.addFilter(
        getName(), new SpamFilter(getModuleSection().getInt("messagesPerMinute")));
  }

  @Override
  public void onDisable() {
    FilterManager.removeFilter(getName());
  }
}

package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.SymbolSubstitutionFilter;
import java.util.Map;
import java.util.stream.Collectors;

public class SymbolSubstitutionModule extends Module {
  @Override
  public String getName() {
    return "SymbolSubstitution";
  }

  @Override
  public void onEnable() {
    Map<String, String> replacementMapping =
        getModuleSection().getObject("replacements").entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, entry -> entry.getValue().unwrapped().toString()));

    FilterManager.addFilter(getName(), new SymbolSubstitutionFilter(replacementMapping));
  }

  @Override
  public void onDisable() {
    FilterManager.removeFilter(getName());
  }
}

package dev.aura.bungeechat.module;

import com.typesafe.config.Config;
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
    Config replacements = getModuleSection().getConfig("replacements");
    Map<String, String> replacementMapping =
        replacements.root().keySet().stream()
            .collect(Collectors.toMap(key -> key, replacements::getString));

    System.out.println(replacements.root().keySet());

    FilterManager.addFilter(getName(), new SymbolSubstitutionFilter(replacementMapping));
  }

  @Override
  public void onDisable() {
    FilterManager.removeFilter(getName());
  }
}

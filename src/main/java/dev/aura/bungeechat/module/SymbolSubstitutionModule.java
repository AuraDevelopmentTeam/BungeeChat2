package dev.aura.bungeechat.module;


import com.typesafe.config.Config;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.module.Module;
import dev.aura.bungeechat.filter.SymbolSubstitutionFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SymbolSubstitutionModule extends Module {

    private static Map<String, String> replacementMapping = new HashMap<>();

    @Override
    public String getName() {
        return "SymbolSubstitution";
    }

    @Override
    public void onEnable() {
        Config section = getModuleSection().getConfig("replacements");
        replacementMapping =
                section.root().keySet().stream().collect(Collectors.toMap(key -> key, section::getString));
        SymbolSubstitutionFilter symbolSubstitutionFilter = new SymbolSubstitutionFilter(replacementMapping);
        FilterManager.addFilter(getName(), symbolSubstitutionFilter);
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
    }
}

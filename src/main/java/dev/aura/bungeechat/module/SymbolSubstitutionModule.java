package dev.aura.bungeechat.module;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
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
        Config section = getModuleSection();
        replacementMapping =
                section.getObject("replacements").keySet().stream().collect(Collectors.toMap(key -> key, section::getString));
        System.out.println(section.getObject("replacements").keySet());
        SymbolSubstitutionFilter symbolSubstitutionFilter = new SymbolSubstitutionFilter(replacementMapping);
        FilterManager.addFilter(getName(), symbolSubstitutionFilter);
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
    }
}

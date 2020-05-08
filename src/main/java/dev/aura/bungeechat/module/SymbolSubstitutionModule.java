package dev.aura.bungeechat.module;


import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.module.Module;
import dev.aura.bungeechat.filter.SymbolSubstitutionFilter;

public class SymbolSubstitutionModule extends Module {
    @Override
    public String getName() {
        return "SymbolSubstitution";
    }

    @Override
    public void onEnable() {
        System.out.println("SymbolSubstitution is enabled!");
        SymbolSubstitutionFilter symbolSubstitutionFilter = new SymbolSubstitutionFilter();
        FilterManager.addFilter(getName(), symbolSubstitutionFilter);
    }

    @Override
    public void onDisable() {

    }
}

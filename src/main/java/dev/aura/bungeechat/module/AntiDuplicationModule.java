package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.DuplicationFilter;

public class AntiDuplicationModule extends Module {
    @Override
    public String getName() {
        return "AntiDuplication";
    }

    @Override
    public void onEnable() {
        FilterManager.addFilter(getName(), new DuplicationFilter(getModuleSection().getInt("checkPastMessages")));
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
    }
}

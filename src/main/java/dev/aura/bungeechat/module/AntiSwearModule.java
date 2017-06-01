package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.SwearWordsFilter;

public class AntiSwearModule implements Module {
    private SwearWordsFilter filter;

    @Override
    public String getName() {
        return "AntiSwear";
    }

    @Override
    public void onEnable() {
        filter = new SwearWordsFilter();

        filter.load();
        FilterManager.addFilter(getName(), filter);
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
        filter.unload();
    }
}

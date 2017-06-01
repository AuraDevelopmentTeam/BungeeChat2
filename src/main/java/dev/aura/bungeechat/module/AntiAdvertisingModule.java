package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.AdvertisingFilter;

public class AntiAdvertisingModule implements Module {
    private AdvertisingFilter filter;

    @Override
    public String getName() {
        return "AntiAdvertising";
    }

    @Override
    public void onEnable() {
        filter = new AdvertisingFilter();

        filter.load();
        FilterManager.addFilter(getName(), filter);
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
        filter.unload();
    }
}

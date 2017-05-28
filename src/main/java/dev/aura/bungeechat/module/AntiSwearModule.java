package dev.aura.bungeechat.module;

import dev.aura.bungeechat.filter.SwearWordsFilter;

public class AntiSwearModule implements Module {
    @Override
    public String getName() {
        return "AntiSwear";
    }

    @Override
    public void onEnable() {
        SwearWordsFilter.loadSwearWords();
    }

    @Override
    public void onDisable() {
        SwearWordsFilter.unloadSwearWords();
    }
}

package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import net.md_5.bungee.config.Configuration;

public class AntiSwearModule implements Module {
    @Override
    public String getName() {
        return "AntiSwear";
    }

    @Override
    public void onEnable() {
        Configuration section = getModuleSection();

        FilterManager.addFilter(getName(),
                new SwearWordsFilter(section.getStringList("words"), section.getString("replacement"),
                        section.getBoolean("freeMatching"), section.getBoolean("leetSpeak"),
                        section.getBoolean("ignoreSpaces"), section.getBoolean("ignoreDuplicateLetters")));
    }

    @Override
    public void onDisable() {
        FilterManager.removeFilter(getName());
    }
}

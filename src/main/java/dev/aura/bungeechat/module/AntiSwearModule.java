package dev.aura.bungeechat.module;

import com.typesafe.config.Config;

import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.SwearWordsFilter;

public class AntiSwearModule extends Module {
    @Override
    public String getName() {
        return "AntiSwear";
    }

    @Override
    public void onEnable() {
        Config section = getModuleSection();

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

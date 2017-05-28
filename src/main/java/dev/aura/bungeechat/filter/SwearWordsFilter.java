package dev.aura.bungeechat.filter;

import java.util.ArrayList;
import java.util.List;

import dev.aura.bungeechat.config.Config;
import lombok.Getter;

public class SwearWordsFilter {
    @Getter
    private static List<String> swearWords;

    public static void loadSwearWords() {
        swearWords = new ArrayList<>();
        swearWords = Config.get().getStringList("Settings.Features.AntiSwear.words");
    }

    public static void unloadSwearWords() {
        swearWords = null;
    }

    public static String replaceSwearWords(String rawMessage) {
        for (String s : swearWords) {
            rawMessage = rawMessage.replace(s, Config.get().getString("Settings.Features.AntiSwear.replacement"));
        }
        return rawMessage;
    }

}

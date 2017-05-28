package dev.aura.bungeechat.filter;

import java.util.List;

import dev.aura.bungeechat.module.ModuleManager;
import lombok.Getter;

public class SwearWordsFilter {
    @Getter
    private static List<String> swearWords;

    public static void loadSwearWords() {
        swearWords = ModuleManager.ANTI_SWEAR_MODULE.getModuleSection().getStringList("words");
    }

    public static void unloadSwearWords() {
        swearWords = null;
    }

    public static String replaceSwearWords(String rawMessage) {
        for (String s : swearWords) {
            rawMessage = rawMessage.replace(s,
                    ModuleManager.ANTI_SWEAR_MODULE.getModuleSection().getString("replacement"));
        }
        
        return rawMessage;
    }

}

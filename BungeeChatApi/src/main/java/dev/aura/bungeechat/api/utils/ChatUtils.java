package dev.aura.bungeechat.api.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatUtils {
    public static boolean isCommand(String message) {
        return message.startsWith("/");
    }
}

package dev.aura.bungeechat.api.utils;

public class ChatUtils {

    public static boolean isCommand(String message) {
        return message.startsWith("/");
    }

}

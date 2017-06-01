package dev.aura.bungeechat.util;

import java.util.logging.Logger;

import dev.aura.bungeechat.BungeeChat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class LoggerHelper {
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private static final Logger logger = BungeeChat.getInstance().getLogger();

    public static void error(String text) {
        getLogger().severe(ChatColor.RED + text);
    }

    public static void warning(String text) {
        getLogger().warning(ChatColor.YELLOW + text);
    }

    public static void info(String text) {
        getLogger().info(text);
    }
}

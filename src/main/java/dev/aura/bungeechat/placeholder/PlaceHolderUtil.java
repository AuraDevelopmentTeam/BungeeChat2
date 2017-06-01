package dev.aura.bungeechat.placeholder;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

@UtilityClass
public class PlaceHolderUtil {
    private static final String FORMATS = "Formats";
    private static final String MESSAGES = "Messages";
    private static Configuration formatsBase;
    private static Configuration messageBase;

    public static String getFormat(String format) {
        if (formatsBase == null) {
            formatsBase = Config.get().getSection(FORMATS);
        }

        return formatsBase.getString(format);
    }

    public static String getMessage(String message) {
        if (messageBase == null) {
            messageBase = Config.get().getSection(MESSAGES);
        }

        return messageBase.getString(message);
    }

    public static String getFullFormatMessage(String format, BungeeChatContext context) {
        return ChatColor.translateAlternateColorCodes('&',
                PlaceHolderManager.processMessage(getFormat(format), context));
    }

    public static String getFullMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', getMessage(message));
    }
}

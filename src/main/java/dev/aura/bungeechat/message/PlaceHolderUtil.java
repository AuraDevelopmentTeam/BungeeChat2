package dev.aura.bungeechat.message;

import java.util.regex.Pattern;

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

    private static final char altColorChar = '&';
    private static final String altColorString = String.valueOf(altColorChar);
    private static final Pattern colorCodeDetection = Pattern
            .compile("(?i)(?<!" + altColorChar + ')' + altColorChar + "([0-9A-FK-OR])");
    private static final Pattern duplicateDection = Pattern.compile(altColorString + altColorString);

    public static String getFormat(Format format) {
        try {
            if (formatsBase == null) {
                formatsBase = Config.get().getSection(FORMATS);
            }

            return formatsBase.getString(format.getStringPath());
        } catch (RuntimeException e) {
            return format.getStringPath();
        }
    }

    public static String getMessage(Message message) {
        try {
            if (messageBase == null) {
                messageBase = Config.get().getSection(MESSAGES);
            }

            return messageBase.getString(message.getStringPath());
        } catch (RuntimeException e) {
            return message.getStringPath();
        }
    }

    public static String getFullFormatMessage(Format format, BungeeChatContext context) {
        return formatMessage(getFormat(format), context);
    }

    public static String getFullMessage(Message message) {
        return formatMessage(getMessage(message), new BungeeChatContext());
    }

    public static String getFullMessage(Message message, BungeeChatContext context) {
        return formatMessage(getMessage(message), context);
    }

    public static String formatMessage(String message, BungeeChatContext context) {
        return transformAltColorCodes(PlaceHolderManager.processMessage(message, context));
    }

    public static String transformAltColorCodes(String message) {
        message = colorCodeDetection.matcher(message).replaceAll(ChatColor.COLOR_CHAR + "$1");
        message = duplicateDection.matcher(message).replaceAll(altColorString);

        return message;
    }

    public static String escapeAltColorCodes(String message) {
        return message.replace(altColorString, altColorString + altColorString);
    }
}

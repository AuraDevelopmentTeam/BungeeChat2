package dev.aura.bungeechat.message;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Configuration;
import dev.aura.bungeechat.config.MessagesTranslator;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class PlaceHolderUtil {
    private static final String FORMATS = "Formats";
    private static final String LANGUAGE = "Language";
    private static Config formatsBase;
    private static MessagesTranslator messageBase;

    private static final char altColorChar = '&';
    private static final String altColorString = String.valueOf(altColorChar);
    private static final String colorCodeReplacement = ChatColor.COLOR_CHAR + "$1";
    private static final Pattern duplicateDection = Pattern.compile(altColorString + altColorString);
    private static final ImmutableMap<Permission, Character> colorCodeMap = ImmutableMap
            .<Permission, Character>builder().put(Permission.USE_CHAT_COLOR_BLACK, '0')
            .put(Permission.USE_CHAT_COLOR_DARK_BLUE, '1').put(Permission.USE_CHAT_COLOR_DARK_GREEN, '2')
            .put(Permission.USE_CHAT_COLOR_DARK_AQUA, '3').put(Permission.USE_CHAT_COLOR_DARK_RED, '4')
            .put(Permission.USE_CHAT_COLOR_DARK_PURPLE, '5').put(Permission.USE_CHAT_COLOR_GOLD, '6')
            .put(Permission.USE_CHAT_COLOR_GRAY, '7').put(Permission.USE_CHAT_COLOR_DARK_GRAY, '8')
            .put(Permission.USE_CHAT_COLOR_BLUE, '9').put(Permission.USE_CHAT_COLOR_GREEN, 'a')
            .put(Permission.USE_CHAT_COLOR_AQUA, 'b').put(Permission.USE_CHAT_COLOR_RED, 'c')
            .put(Permission.USE_CHAT_COLOR_LIGHT_PURPLE, 'd').put(Permission.USE_CHAT_COLOR_YELLOW, 'e')
            .put(Permission.USE_CHAT_COLOR_WHITE, 'f').put(Permission.USE_CHAT_FORMAT_OBFUSCATED, 'k')
            .put(Permission.USE_CHAT_FORMAT_BOLD, 'l').put(Permission.USE_CHAT_FORMAT_STRIKETHROUGH, 'm')
            .put(Permission.USE_CHAT_FORMAT_UNDERLINE, 'n').put(Permission.USE_CHAT_FORMAT_ITALIC, 'o')
            .put(Permission.USE_CHAT_FORMAT_RESET, 'r').build();
    private static final Map<Integer, Optional<Pattern>> patternCache = new HashMap<>();

    public static void clearConfigSections() {
        formatsBase = null;
        messageBase = null;
    }

    public static void loadConfigSections() {
        loadFormatsBase();
        loadMessageBase();
    }

    public static void loadFormatsBase() {
        formatsBase = Configuration.get().getConfig(FORMATS);
    }

    public static void loadMessageBase() {
        File dir = BungeeChat.getInstance().getLangFolder();
        String language = Configuration.get().getString(LANGUAGE);

        messageBase = new MessagesTranslator(dir, language);
    }

    public static String getFormat(Format format) {
        try {
            if (formatsBase == null) {
                loadFormatsBase();
            }

            return formatsBase.getString(format.getStringPath());
        } catch (RuntimeException e) {
            return format.getStringPath();
        }
    }

    public static String getMessage(Message message) {
        try {
            if (messageBase == null) {
                loadMessageBase();
            }

            return messageBase.translateWithFallback(message);
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
        return transformAltColorCodes(message, Optional.empty());
    }

    public static String transformAltColorCodes(String message, Optional<BungeeChatAccount> account) {
        BungeeChatAccount permsAccount = account.orElseGet(() -> AccountManager.getConsoleAccount());

        Integer key = colorCodeMap.keySet().stream().map(perm -> PermissionManager.hasPermission(permsAccount, perm))
                .reduce(0, (in, bool) -> (in << 1) | (bool.booleanValue() ? 1 : 0), (a, b) -> b);

        if (!patternCache.containsKey(key)) {
            if (key.intValue() == 0) {
                patternCache.put(key, Optional.empty());
            } else {
                Pattern pattern = Pattern.compile(
                        colorCodeMap.entrySet().stream()
                                .filter(entry -> PermissionManager.hasPermission(permsAccount, entry.getKey()))
                                .map(entry -> entry.getValue().toString()).collect(Collectors.joining("",
                                        "(?<!" + altColorChar + ')' + altColorChar + "([", "])")),
                        Pattern.CASE_INSENSITIVE);

                patternCache.put(key, Optional.of(pattern));
            }
        }

        Optional<Pattern> pattern = patternCache.get(key);

        if (pattern.isPresent()) {
            message = pattern.get().matcher(message).replaceAll(colorCodeReplacement);
        }

        message = duplicateDection.matcher(message).replaceAll(altColorString);

        return message;
    }

    public static String escapeAltColorCodes(String message) {
        return message.replace(altColorString, altColorString + altColorString);
    }
}

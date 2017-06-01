package dev.aura.bungeechat.filter;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.config.Configuration;

public class AdvertisingFilter implements BungeeChatFilter {
    /**
     * Regex from <a href=
     * "https://gist.github.com/dperini/729294">https://gist.github.com/dperini/729294</a>.
     * <br />
     * Slightly modified. Allowed dropping of the protocol. So
     * <code>google.com</code> still matches!
     */
    private Pattern url = Pattern.compile(
            "(?:(?:https?|ftp):\\/\\/)?(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?");
    private Predicate<String> whitelisted;

    public void load() {
        Configuration section = ModuleManager.ANTI_ADVERTISING_MODULE.getModuleSection();

        /*
         * First turn the string list into a String Stream. That gets mapped to
         * a Pattern Stream. Normal strings get turned into literal regexes and
         * then get their "\*" replaced to ".*" so that the original "*"s in the
         * string become actual wildcards. Strings starting with "R=" are
         * interpreted as regexes. The "R=" gets stripped and then turned into
         * an Pattern. Then the patterns get turned into predicates and finally
         * they get combined into one predicate through an or, as an element is
         * on the list when one or more regexes match. Reduce will return an
         * optional that is empty if the original list was empty too. If that is
         * the case it becomes an always false predicate since nothing will
         * match the whitelist.
         */
        whitelisted = section.getStringList("whitelisted").stream().map(whitelist -> {
            if (whitelist.startsWith("R=")) {
                // Interpret the string as regex and remove the first two
                // characters (which are "R=")
                return Pattern.compile(whitelist.substring(2));
            } else {
                // Turn string into literal Regex and replace all "\*" with ".*"
                // (means all original "*" become wildcards)
                return Pattern.compile(Pattern.quote(whitelist).replaceAll("\\\\\\*", ".*"));
            }
        }).map(Pattern::asPredicate).reduce(Predicate::or).orElse(x -> false);
    }

    public void unload() {
        whitelisted = null;
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        if (PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_ADVERTISEMENT))
            return message;

        Matcher matches = url.matcher(message);
        boolean matchOk;
        String match;

        while (matches.find()) {
            match = matches.group();
            matchOk = whitelisted.test(match);

            if (!matchOk)
                throw new BlockMessageException(PlaceHolderUtil.getFullMessage("anti-advertise"));
        }

        return message;
    }

}

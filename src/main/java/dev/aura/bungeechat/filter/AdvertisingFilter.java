package dev.aura.bungeechat.filter;

import java.util.List;
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
            "^(?:(?:https?|ftp):\\/\\/)?(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");
    private List<String> whitelisted;

    public void load() {
        Configuration section = ModuleManager.ANTI_ADVERTISING_MODULE.getModuleSection();

        whitelisted = section.getStringList("whitelisted");
    }

    public void unload() {
        whitelisted = null;
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        if (PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_ADVERTISEMENT))
            return message;

        boolean matchOk;
        Matcher matches = url.matcher(message);

        while (matches.find()) {
            matchOk = false;

            for (String whitelist : whitelisted) {
                if (matches.group().contains(whitelist)) {
                    matchOk = true;

                    break;
                }
            }

            if (!matchOk)
                throw new BlockMessageException(PlaceHolderUtil.getFullMessage("anti-advertise"));
        }

        return message;
    }

}

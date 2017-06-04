package dev.aura.bungeechat.filter;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.utils.RegexUtil;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.config.Configuration;

public class SwearWordsFilter implements BungeeChatFilter {
    private List<Pattern> swearWords;
    private String replacement;

    public void load() {
        Configuration section = ModuleManager.ANTI_SWEAR_MODULE.getModuleSection();

        swearWords = section.getStringList("words").stream().map(RegexUtil::parseWildcardToPattern)
                .collect(Collectors.toList());
        replacement = section.getString("replacement");
    }

    public void unload() {
        swearWords = null;
        replacement = null;
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) {
        if (PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR))
            return message;

        for (Pattern p : swearWords) {
            message = p.matcher(message).replaceAll(replacement);
        }

        return message;
    }

    @Override
    public int getPriority() {
        return FilterManager.SWEAR_FILTER_PRIORITY;
    }
}

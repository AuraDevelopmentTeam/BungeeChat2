package dev.aura.bungeechat.filter;

import java.util.List;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.config.Configuration;

public class SwearWordsFilter implements BungeeChatFilter {
    private List<String> swearWords;
    private String replacement;

    public void load() {
        Configuration section = ModuleManager.ANTI_SWEAR_MODULE.getModuleSection();

        swearWords = section.getStringList("words");
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

        for (String s : swearWords) {
            message = message.replace(s, replacement);
        }

        return message;
    }

}

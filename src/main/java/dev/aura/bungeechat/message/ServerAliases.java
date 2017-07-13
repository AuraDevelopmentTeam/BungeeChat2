package dev.aura.bungeechat.message;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import dev.aura.bungeechat.config.Config;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.config.Configuration;

@UtilityClass
public class ServerAliases {
    private static Map<String, String> aliasMapping = new HashMap<>();

    public static String getServerAlias(String name) {
        if (aliasMapping.containsKey(name))
            return aliasMapping.get(name);
        else
            return name;
    }

    public static void loadAliases() {
        Configuration section = Config.get().getSection("Settings.ServerAlias");

        aliasMapping = section.getKeys().stream().collect(Collectors.toMap(key -> key, key -> section.getString(key)));
    }
}

package dev.aura.bungeechat.message;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.typesafe.config.Config;

import dev.aura.bungeechat.config.Configuration;
import lombok.experimental.UtilityClass;

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
        Config section = Configuration.get().getConfig("ServerAlias");

        aliasMapping = section.root().keySet().stream().collect(Collectors.toMap(key -> key, section::getString));
    }
}

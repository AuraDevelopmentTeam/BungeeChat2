package dev.aura.bungeechat.api.hook;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HookManager {
    public static final int DEFAULT_PREFIX_PRIORITY = 100;
    public static final int PERMISSION_PLUGIN_PREFIX_PRIORITY = 200;
    public static final int ACCOUNT_PREFIX_PRIORITY = 300;

    private static Map<String, BungeeChatHook> hooks = new LinkedHashMap<>();

    public static void addHook(String name, BungeeChatHook hook) throws UnsupportedOperationException {
        hooks.put(name, hook);

        sortHooks();
    }

    public static BungeeChatHook removeHook(String name) throws UnsupportedOperationException {
        BungeeChatHook out = hooks.remove(name);

        sortHooks();

        return out;
    }

    public String getPrefix(BungeeChatAccount account) {
        Optional<String> out;

        for (BungeeChatHook hook : hooks.values()) {
            out = hook.getPrefix(account);

            if (out.isPresent())
                return out.get();
        }

        return "";
    }

    public String getSuffix(BungeeChatAccount account) {
        Optional<String> out;

        for (BungeeChatHook hook : hooks.values()) {
            out = hook.getSuffix(account);

            if (out.isPresent())
                return out.get();
        }

        return "";
    }

    public String getFullname(BungeeChatAccount account) {
        return getPrefix(account) + account.getName() + getSuffix(account);
    }

    private static void sortHooks() {
        hooks = hooks.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}

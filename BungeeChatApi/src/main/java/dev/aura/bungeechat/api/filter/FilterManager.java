package dev.aura.bungeechat.api.filter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FilterManager {
    public static final int SWEAR_FILTER_PRIORITY = 100;
    public static final int ADVERTISING_FILTER_PRIORITY = 200;
    public static final int LOCK_CHAT_FILTER_PRIORITY = 300;
    
    private static Map<String, BungeeChatFilter> filters = new LinkedHashMap<>();
    private static final boolean validSide = BungeeChatApi.getInstance().getServerType() == ServerType.BUNGEECORD;

    public static void addFilter(String name, BungeeChatFilter filter) throws UnsupportedOperationException {
        checkSide();

        filters.put(name, filter);
        
        sortFilters();
    }

    public static BungeeChatFilter removeFilter(String name) throws UnsupportedOperationException {
        checkSide();
        
        BungeeChatFilter out = filters.remove(name);
        
        sortFilters();

        return out;
    }

    public static String applyFilters(BungeeChatAccount sender, String message)
            throws UnsupportedOperationException, BlockMessageException {
        checkSide();

        for (BungeeChatFilter filter : filters.values()) {
            message = filter.applyFilter(sender, message);
        }

        return message;
    }

    private static void checkSide() throws UnsupportedOperationException {
        if (!validSide)
            throw new UnsupportedOperationException("This operation is only allowed on the BungeeCord!");
    }

    private static void sortFilters() {
        filters = filters.entrySet().stream().sorted(Entry.comparingByValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}

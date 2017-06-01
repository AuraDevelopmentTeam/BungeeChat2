package dev.aura.bungeechat.api.filter;

import java.util.HashMap;
import java.util.Map;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FilterManager {
    private static final Map<String, BungeeChatFilter> filters = new HashMap<>();
    private static final boolean validSide = BungeeChatApi.getInstance().getServerType() == ServerType.BUNGEECORD;

    public static void addFilter(String name, BungeeChatFilter filter) throws UnsupportedOperationException {
        checkSide();

        filters.put(name, filter);
    }

    public static BungeeChatFilter removeFilter(String name) throws UnsupportedOperationException {
        checkSide();

        return filters.remove(name);
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
}

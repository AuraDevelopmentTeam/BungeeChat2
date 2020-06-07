package dev.aura.bungeechat.api.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FilterManager {
  public static final int SWEAR_FILTER_PRIORITY = 100;
  public static final int ADVERTISING_FILTER_PRIORITY = 200;
  public static final int CAPSLOCK_FILTER_PRIORITY = 300;
  public static final int DUPLICATION_FILTER_PRIORITY = 400;
  public static final int SPAM_FILTER_PRIORITY = 500;
  public static final int LOCK_CHAT_FILTER_PRIORITY = 600;

  private static Map<String, BungeeChatFilter> filters = new LinkedHashMap<>();

  public static void addFilter(String name, BungeeChatFilter filter)
      throws UnsupportedOperationException {
    filters.put(name, filter);

    sortFilters();
  }

  public static BungeeChatFilter removeFilter(String name) throws UnsupportedOperationException {
    BungeeChatFilter out = filters.remove(name);

    sortFilters();

    return out;
  }

  public static String applyFilters(BungeeChatAccount sender, String message)
      throws UnsupportedOperationException, BlockMessageException {
    for (BungeeChatFilter filter : filters.values()) {
      message = filter.applyFilter(sender, message);
    }

    return message;
  }

  private static void sortFilters() {
    filters =
        filters.entrySet().stream()
            .sorted(Collections.reverseOrder(Entry.comparingByValue()))
            .collect(
                Collectors.toMap(
                    Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }
}

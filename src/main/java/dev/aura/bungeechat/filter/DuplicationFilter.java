package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DuplicationFilter implements BungeeChatFilter {
  private final ConcurrentMap<UUID, Queue<String>> playerMessagesStorage;
  private final int checkPastMessages;
  private final boolean noPermissions;

  public DuplicationFilter(int checkPastMessages) {
    this(checkPastMessages, false);
  }

  public DuplicationFilter(int checkPastMessages, boolean noPermissions) {
    playerMessagesStorage = new ConcurrentHashMap<>();
    this.checkPastMessages = checkPastMessages;
    this.noPermissions = noPermissions;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
    if (!noPermissions && PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_DUPLICATE))
      return message;

    UUID uuid = sender.getUniqueId();

    if (!playerMessagesStorage.containsKey(uuid)) {
      playerMessagesStorage.put(uuid, new ArrayDeque<>(checkPastMessages));
    }

    Queue<String> playerMessages = playerMessagesStorage.get(uuid);

    if (playerMessages.contains(message))
      throw new ExtendedBlockMessageException(Messages.ANTI_DUPLICATION, sender, message);
    else {
      if (playerMessages.size() == checkPastMessages) {
        playerMessages.remove();
      }

      playerMessages.add(message);
    }

    return message;
  }

  @Override
  public int getPriority() {
    return FilterManager.DUPLICATION_FILTER_PRIORITY;
  }
}

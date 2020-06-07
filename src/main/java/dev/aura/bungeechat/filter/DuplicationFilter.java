package dev.aura.bungeechat.filter;

import com.google.common.annotations.VisibleForTesting;
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
import java.util.concurrent.TimeUnit;
import lombok.Value;

public class DuplicationFilter implements BungeeChatFilter {
  private final ConcurrentMap<UUID, Queue<TimePointMessage>> playerMessagesStorage;
  private final int checkPastMessages;
  private final long expiryTimer;
  private final boolean noPermissions;

  public DuplicationFilter(int checkPastMessages, int expireAfter) {
    this(checkPastMessages, expireAfter, false);
  }

  @VisibleForTesting
  DuplicationFilter(int checkPastMessages, int expireAfter, boolean noPermissions) {
    playerMessagesStorage = new ConcurrentHashMap<>();
    this.checkPastMessages = checkPastMessages;
    expiryTimer = TimeUnit.SECONDS.toNanos(expireAfter);
    this.noPermissions = noPermissions;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
    if (!noPermissions && PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_DUPLICATE))
      return message;

    final UUID uuid = sender.getUniqueId();

    if (!playerMessagesStorage.containsKey(uuid)) {
      playerMessagesStorage.put(uuid, new ArrayDeque<>(checkPastMessages));
    }

    final Queue<TimePointMessage> playerMessages = playerMessagesStorage.get(uuid);
    final long now = System.nanoTime();
    final long expiry = now - expiryTimer;

    while (!playerMessages.isEmpty() && (playerMessages.peek().getTimePoint() < expiry)) {
      playerMessages.poll();
    }

    if (playerMessages.stream().map(TimePointMessage::getMessage).anyMatch(message::equals))
      throw new ExtendedBlockMessageException(Messages.ANTI_DUPLICATION, sender, message);

    if (playerMessages.size() == checkPastMessages) {
      playerMessages.remove();
    }

    playerMessages.add(new TimePointMessage(now, message));

    return message;
  }

  @Override
  public int getPriority() {
    return FilterManager.DUPLICATION_FILTER_PRIORITY;
  }

  @VisibleForTesting
  void clear() {
    playerMessagesStorage.clear();
  }

  @Value
  private static class TimePointMessage {
    private final long timePoint;
    private final String message;
  }
}

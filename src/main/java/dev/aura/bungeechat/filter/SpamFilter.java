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

public class SpamFilter implements BungeeChatFilter {
  @VisibleForTesting static long expiryTimer = TimeUnit.MINUTES.toNanos(1);

  private final ConcurrentMap<UUID, Queue<Long>> playerMessageTimepointStorage;
  private final int messagesPerMinute;
  private final boolean noPermissions;

  public SpamFilter(int messagesPerMinute) {
    this(messagesPerMinute, false);
  }

  @VisibleForTesting
  SpamFilter(int messagesPerMinute, boolean noPermissions) {
    playerMessageTimepointStorage = new ConcurrentHashMap<>();
    this.messagesPerMinute = messagesPerMinute;
    this.noPermissions = noPermissions;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
    if (!noPermissions && PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SPAM))
      return message;

    final UUID uuid = sender.getUniqueId();

    if (!playerMessageTimepointStorage.containsKey(uuid)) {
      playerMessageTimepointStorage.put(uuid, new ArrayDeque<>(messagesPerMinute));
    }

    final Queue<Long> timePoints = playerMessageTimepointStorage.get(uuid);
    final long now = System.nanoTime();
    final long expiry = now - expiryTimer;

    while (!timePoints.isEmpty() && (timePoints.peek() < expiry)) {
      timePoints.poll();
    }

    if (timePoints.size() >= messagesPerMinute)
      throw new ExtendedBlockMessageException(Messages.ANTI_SPAM, sender, message);

    timePoints.add(now);

    return message;
  }

  @Override
  public int getPriority() {
    return FilterManager.SPAM_FILTER_PRIORITY;
  }

  @VisibleForTesting
  void clear() {
    playerMessageTimepointStorage.clear();
  }
}

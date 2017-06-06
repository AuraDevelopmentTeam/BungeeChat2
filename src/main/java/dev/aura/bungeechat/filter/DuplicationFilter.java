package dev.aura.bungeechat.filter;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Message;

public class DuplicationFilter implements BungeeChatFilter {
    private final ConcurrentMap<UUID, Queue<String>> playerMessagesStorage;
    private final int checkPastMessages;

    public DuplicationFilter(int checkPastMessages) {
        playerMessagesStorage = new ConcurrentHashMap<>();
        this.checkPastMessages = checkPastMessages;
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        UUID uuid = sender.getUniqueId();

        if (!playerMessagesStorage.containsKey(uuid)) {
            playerMessagesStorage.put(uuid, new ArrayDeque<>(checkPastMessages));
        }

        Queue<String> playerMessages = playerMessagesStorage.get(uuid);

        if (playerMessages.contains(message))
            throw new BlockMessageException(Message.ANTI_DUPLICATION.get(sender, message));
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

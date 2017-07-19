package dev.aura.bungeechat.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;

public class AutomaticBroadcastTask implements Runnable {
    private final List<String> messages;
    private final int size;
    private final boolean random;
    private int current;
    private Random rand;

    public AutomaticBroadcastTask(List<String> messages, boolean random) {
        this.messages = new ArrayList<>(messages);
        size = messages.size();
        this.random = random;
        current = -1;
        rand = new Random();
    }

    @Override
    public void run() {
        MessagesService.sendToMatchingPlayers(PlaceHolderUtil.formatMessage(getMessage(), new BungeeChatContext()),
                MessagesService.getGlobalPredicate());
    }

    private String getMessage() {
        if (random) {
            current = rand.nextInt(size);
        } else {
            current++;

            if (current >= size) {
                current = 0;
            }
        }

        return messages.get(current);
    }
}

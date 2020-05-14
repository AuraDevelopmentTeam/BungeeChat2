package dev.aura.bungeechat.task;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AutomaticBroadcastTask implements Runnable {
  private final Predicate<BungeeChatAccount> predicate;
  private final List<String> messages;
  private final int size;
  private final boolean random;
  private int current;
  private Random rand;

  public AutomaticBroadcastTask(
      Predicate<BungeeChatAccount> predicate, List<String> messages, boolean random) {
    this.predicate = predicate;
    this.messages = new ArrayList<>(messages);
    size = messages.size();
    this.random = random;
    current = -1;
    rand = new Random();
  }

  @Override
  public void run() {
    MessagesService.sendToMatchingPlayers(
        PlaceHolderUtil.formatMessage(getMessage(), new BungeeChatContext()), predicate);
  }

  private String getMessage() {
    if (random) {
      current = rand.nextInt(size);
    } else {
      current = (++current >= size) ? 0 : current;
    }

    return messages.get(current);
  }
}

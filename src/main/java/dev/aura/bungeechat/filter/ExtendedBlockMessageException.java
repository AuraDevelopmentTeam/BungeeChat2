package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.message.Messages;
import lombok.Getter;

public class ExtendedBlockMessageException extends BlockMessageException {
  private static final long serialVersionUID = 5519820760858610372L;

  @Getter private final Messages messageType;

  public ExtendedBlockMessageException(
      Messages messageType, BungeeChatAccount sender, String message) {
    super(messageType.get(sender, message));

    this.messageType = messageType;
  }
}

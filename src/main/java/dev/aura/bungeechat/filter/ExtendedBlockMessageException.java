package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.message.Message;
import lombok.Getter;

public class ExtendedBlockMessageException extends BlockMessageException {
  private static final long serialVersionUID = 5519820760858610372L;

  @Getter private Message messageType;

  public ExtendedBlockMessageException(
      Message messageType, BungeeChatAccount sender, String message) {
    super(messageType.get(sender, message));

    this.messageType = messageType;
  }
}

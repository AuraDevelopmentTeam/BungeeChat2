package dev.aura.bungeechat.chatlog;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.Format;
import java.util.logging.Logger;

public class ConsoleLogger implements ChatLogger {
  private final Logger logger;

  public ConsoleLogger() {
    logger = BungeeChat.getInstance().getLogger();
  }

  @Override
  public void log(BungeeChatContext context) {
    logger.info(Format.CHAT_LOGGING_CONSOLE.get(context));
  }
}

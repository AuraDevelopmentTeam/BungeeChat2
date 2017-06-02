package dev.aura.bungeechat.chatlog;

import java.util.logging.Logger;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;

public class ConsoleLogger implements ChatLogger {
    private final Logger logger;

    public ConsoleLogger() {
        logger = BungeeChat.getInstance().getLogger();
    }

    @Override
    public void log(BungeeChatContext context) {
        logger.info(PlaceHolderUtil.getFullFormatMessage("chat-logger-console", context));
    }
}

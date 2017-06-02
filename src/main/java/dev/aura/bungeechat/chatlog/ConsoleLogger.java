package dev.aura.bungeechat.chatlog;

import java.util.logging.Logger;

import dev.aura.bungeechat.BungeeChat;

public class ConsoleLogger implements ChatLogger {
    private final Logger logger;

    public ConsoleLogger() {
        logger = BungeeChat.getInstance().getLogger();
    }

    @Override
    public void log(String message) {
        logger.info(message);
    }
}

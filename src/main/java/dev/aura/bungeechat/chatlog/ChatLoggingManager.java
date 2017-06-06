package dev.aura.bungeechat.chatlog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatLoggingManager {
    private static List<ChatLogger> loggers = new LinkedList<ChatLogger>();

    public static void addLogger(ChatLogger logger) {
        loggers.add(logger);
    }

    public static void removeLogger(ChatLogger logger) {
        loggers.remove(logger);
    }

    public static void logMessage(String channel, BungeeChatAccount sender, String message) {
        BungeeChatContext context = new BungeeChatContext(sender, message);
        context.setChannel(channel);

        logMessage(context);
    }

    public static void logMessage(ChannelType channel, BungeeChatContext context) {
        logMessage(channel.name(), context);
    }

    public static void logMessage(String channel, BungeeChatContext context) {
        context.setChannel(channel);

        logMessage(context);
    }

    public static void logMessage(BungeeChatContext context) {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE, BungeeChatContext.HAS_CHANNEL);

        getStream().forEach(logger -> logger.log(context));
    }

    public static void logCommand(BungeeChatAccount account, String command) {
        logMessage("COMMAND", account, command);
    }

    private static Stream<ChatLogger> getStream() {
        return loggers.stream();
    }
}

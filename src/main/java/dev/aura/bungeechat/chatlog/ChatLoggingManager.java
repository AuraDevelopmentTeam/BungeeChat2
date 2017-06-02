package dev.aura.bungeechat.chatlog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
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

    public static void logMessage(BungeeChatContext context, ChannelType channel) {
        context.require(BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE);

        BungeeChatAccount sender = context.getSender().get();

        logMessage(channel + " > " + sender.getServerName() + " > " + context.getMessage().get());
    }

    public static void logMessage(String simpleMessage) {
        getStream().forEach(logger -> logger.log(simpleMessage));
    }

    public static void logCommand(BungeeChatAccount account, String command) {
        logMessage("COMMAND > " + account.getServerName() + " > " + command);
    }

    private static Stream<ChatLogger> getStream() {
        return loggers.stream();
    }
}

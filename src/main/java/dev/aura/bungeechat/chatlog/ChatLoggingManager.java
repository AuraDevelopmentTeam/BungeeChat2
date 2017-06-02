package dev.aura.bungeechat.chatlog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;

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
        getStream().forEach(logger -> logger.logMessage(context, channel));
    }
    
    public static void logMessage(String simpleMessage) {
        ProxyServer.getInstance().getLogger().info(simpleMessage);
    }

    public static void logCommand(BungeeChatAccount account, String command) {
        getStream().forEach(logger -> logger.logCommand(account, command));
    }

    private static Stream<ChatLogger> getStream() {
        return loggers.stream();
    }
}

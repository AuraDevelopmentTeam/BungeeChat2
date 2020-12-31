package dev.aura.bungeechat.chatlog;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.utils.RegexUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatLoggingManager {
  private static final List<ChatLogger> loggers = new LinkedList<>();
  private static List<Pattern> filteredCommands = new LinkedList<>();

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
    context.require(
        BungeeChatContext.HAS_SENDER, BungeeChatContext.HAS_MESSAGE, BungeeChatContext.HAS_CHANNEL);

    getStream().forEach(logger -> logger.log(context));
  }

  public static void logCommand(BungeeChatAccount account, String command) {
    for (Pattern pattern : filteredCommands) {
      if (pattern.matcher(command).find()) return;
    }

    logMessage("COMMAND", account, command);
  }

  public static void loadFilteredCommands(List<String> commands) {
    filteredCommands =
        commands.stream().map(RegexUtil::parseWildcardToPattern).collect(Collectors.toList());
  }

  private static Stream<ChatLogger> getStream() {
    return loggers.stream();
  }
}

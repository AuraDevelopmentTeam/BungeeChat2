package dev.aura.bungeechat.util;

import com.typesafe.config.Config;
import dev.aura.bungeechat.config.Configuration;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

@UtilityClass
public class ServerNameUtil {
  private static Map<String, String> aliasMapping = new HashMap<>();

  public static Optional<ServerInfo> getServerInfo(String serverName) {
    return ProxyServer.getInstance().getServers().values().stream()
        .filter(server -> serverName.equalsIgnoreCase(server.getName()))
        .findAny();
  }

  public static Optional<String> verifyServerName(String serverName) {
    return getServerInfo(serverName).map(ServerInfo::getName);
  }

  public static Optional<String> verifyServerName(String serverName, CommandSender sender) {
    final Optional<String> verifiedServerName = verifyServerName(serverName);

    if (!verifiedServerName.isPresent()) {
      MessagesService.sendMessage(sender, Messages.UNKNOWN_SERVER.get(sender, serverName));
    }

    return verifiedServerName;
  }

  public static List<String> getServerNames() {
    return new ArrayList<>(ProxyServer.getInstance().getServers().keySet());
  }

  public static List<String> getMatchingServerNames(String partialName) {
    return getServerNames().stream()
        .filter(serverName -> serverName.startsWith(partialName))
        .collect(Collectors.toList());
  }

  public static String getServerAlias(ServerInfo server) {
    return getServerAlias(server.getName());
  }

  public static String getServerAlias(String name) {
    return aliasMapping.getOrDefault(name, name);
  }

  public static void loadAliases() {
    Config section = Configuration.get().getConfig("ServerAlias");

    aliasMapping =
        section.root().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, entry -> entry.getValue().unwrapped().toString()));
  }
}

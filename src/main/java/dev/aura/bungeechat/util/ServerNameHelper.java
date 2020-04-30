package dev.aura.bungeechat.util;

import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

@UtilityClass
public class ServerNameHelper {
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
    return ProxyServer.getInstance().getServers().values().stream()
        .map(ServerInfo::getName)
        .collect(Collectors.toList());
  }

  public static List<String> getMatchingServerNames(String partialName) {
    return getServerNames().stream()
        .filter(serverName -> serverName.startsWith(partialName))
        .collect(Collectors.toList());
  }
}

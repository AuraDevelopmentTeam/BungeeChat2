package dev.aura.bungeechat.util;

import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import java.util.Optional;
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
      final Context context = new Context(sender);
      context.setServer(serverName);

      MessagesService.sendMessage(sender, Messages.UNKNOWN_SERVER.get(context));
    }

    return verifiedServerName;
  }
}

package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.command.BaseCommand;
import dev.aura.bungeechat.util.LoggerHelper;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandTabCompleteListener implements Listener {
  private static boolean useModernMethods = true;

  private Map<String, BaseCommand> bungeeChatCommands = null;

  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    final String message = event.getCursor();

    if (!ChatUtils.isCommand(message)) return;

    final String[] allArgs = event.getCursor().split(" ", -1);
    final String command = allArgs[0].substring(1);

    if (allArgs.length == 1) return;
    if (!bungeeChatCommands.containsKey(command)) return;

    final BaseCommand commandHandler = bungeeChatCommands.get(command);
    CommandSender sender = getCommandSenderFromEvent(event);

    if (!commandHandler.hasPermission(sender)) return;

    String[] args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
    Collection<String> suggestions = null;

    try {
      suggestions = commandHandler.tabComplete(sender, args);
    } catch (RuntimeException e) {
      LoggerHelper.warning("Uncaught error during tabcomplete of /" + command, e);
    }

    if (suggestions != null) event.getSuggestions().addAll(suggestions);
  }

  public void updateBungeeChatCommands() {
    bungeeChatCommands = getBungeeChatCommands();
  }

  private static Map<String, BaseCommand> getBungeeChatCommands() {
    return ProxyServer.getInstance().getPluginManager().getCommands().stream()
        .filter(entry -> entry.getValue() instanceof BaseCommand)
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (BaseCommand) entry.getValue()));
  }

  @SuppressWarnings("deprecation") // For the backwards compatibility
  private static CommandSender getCommandSenderFromEvent(TabCompleteEvent event) {
    Stream<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().stream();

    if (useModernMethods) {
      try {
        final SocketAddress senderSocketAddress = event.getSender().getSocketAddress();
        players = players.filter(player -> senderSocketAddress.equals(player.getSocketAddress()));

      } catch (NoSuchMethodError e) {
        // If it fails once, we need to use the old method always
        useModernMethods = false;
        return getCommandSenderFromEvent(event);
      }
    } else {
      final InetSocketAddress senderInetSocketAddress = event.getSender().getAddress();
      players = players.filter(player -> senderInetSocketAddress.equals(player.getAddress()));
    }

    return players.findAny().orElse(null);
  }
}

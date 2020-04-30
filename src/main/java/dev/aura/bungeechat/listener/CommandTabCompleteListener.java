package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.command.BaseCommand;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandTabCompleteListener implements Listener {
  private Map<String, BaseCommand> bungeeChatCommands = null;

  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    final String message = event.getCursor();

    if (!ChatUtils.isCommand(message)) return;

    final String[] allArgs = event.getCursor().split(" ", -1);
    final String command = allArgs[0].substring(1);

    if (allArgs.length == 1) return;
    if (!bungeeChatCommands.containsKey(command)) return;

    final SocketAddress senderSocketAddress = event.getSender().getSocketAddress();
    CommandSender sender =
        ProxyServer.getInstance().getPlayers().stream()
            .filter(player -> senderSocketAddress.equals(player.getSocketAddress()))
            .findAny()
            .orElse(null);
    String[] args = Arrays.copyOfRange(allArgs, 1, allArgs.length);

    event.getSuggestions().addAll(bungeeChatCommands.get(command).tabComplete(sender, args));
  }

  public void updateBungeeChatCommands() {
    bungeeChatCommands = getBungeeChatCommands();
  }

  private static Map<String, BaseCommand> getBungeeChatCommands() {
    return ProxyServer.getInstance().getPluginManager().getCommands().stream()
        .filter(entry -> entry.getValue() instanceof BaseCommand)
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (BaseCommand) entry.getValue()));
  }
}

package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import java.util.List;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MutingListener implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

    if (!account.isMuted()) return;

    final String message = e.getMessage();

    if (ChatUtils.isCommand(message)) {
      List<String> blockCommand =
          BungeecordModuleManager.MUTING_MODULE.getModuleSection().getStringList("blockedcommands");

      for (String s : blockCommand) {
        if (message.startsWith("/" + s + " ")) {
          MessagesService.sendMessage(sender, Messages.MUTED.get(account));
          e.setCancelled(true);

          return;
        }
      }
    } else {
      final ChannelType channel = account.getChannelType();

      if (channel == ChannelType.STAFF) return;

      e.setCancelled(true);
      MessagesService.sendMessage(sender, Messages.MUTED.get(account));
    }
  }
}

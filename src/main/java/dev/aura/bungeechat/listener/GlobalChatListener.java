package dev.aura.bungeechat.listener;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class GlobalChatListener implements Listener {
  private final boolean passToClientServer =
      BungeecordModuleManager.GLOBAL_CHAT_MODULE
          .getModuleSection()
          .getBoolean("passToClientServer");

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    String message = e.getMessage();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

    if (ChatUtils.isCommand(message)) return;

    if (account.getChannelType() == ChannelType.STAFF) return;

    if (BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("default")) {
      if (MessagesService.getGlobalPredicate().test(account)) {
        e.setCancelled(!passToClientServer);
        MessagesService.sendGlobalMessage(sender, message);
        return;
      }
    }

    if (BungeecordAccountManager.getAccount(sender).get().getChannelType() == ChannelType.GLOBAL) {
      if (!MessagesService.getGlobalPredicate().test(account)) {
        MessagesService.sendMessage(sender, Message.NOT_IN_GLOBAL_SERVER.get());

        return;
      }

      e.setCancelled(!passToClientServer);
      MessagesService.sendGlobalMessage(sender, message);

      return;
    }

    Config section =
        BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getConfig("symbol");

    if (section.getBoolean("enabled")) {
      String symbol = section.getString("symbol");

      if (message.startsWith(symbol) && !symbol.equals("/")) {
        if (!MessagesService.getGlobalPredicate().test(account)) {
          MessagesService.sendMessage(sender, Message.NOT_IN_GLOBAL_SERVER.get());

          return;
        }

        e.setCancelled(!passToClientServer);
        MessagesService.sendGlobalMessage(sender, message.replaceFirst(symbol, ""));
      }
    }
  }
}

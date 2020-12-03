package dev.aura.bungeechat.listener;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class GlobalChatListener implements Listener {
  private final boolean passToBackendServer =
      BungeecordModuleManager.GLOBAL_CHAT_MODULE
          .getModuleSection()
          .getBoolean("passToBackendServer");
  private final Config symbolSection =
      BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getConfig("symbol");
  private final Config staffChatSymbolSection =
      BungeecordModuleManager.STAFF_CHAT_MODULE.getModuleSection().getConfig("symbol");

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    String message = e.getMessage();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

    if (ChatUtils.isCommand(message)) return;

    if (staffChatSymbolSection.getBoolean("enabled")) {
      String symbol = staffChatSymbolSection.getString("symbol");

      if (message.startsWith(symbol) && !symbol.equals("/")) return;
    }

    if (symbolSection.getBoolean("enabled")) {
      String symbol = symbolSection.getString("symbol");

      if (message.startsWith(symbol) && !symbol.equals("/")) {
        if (!MessagesService.getGlobalPredicate().test(account)) {
          MessagesService.sendMessage(sender, Messages.NOT_IN_GLOBAL_SERVER.get());
          return;
        }

        final boolean ignoreWithoutPermissions =
            symbolSection.getBoolean("ignoreWithoutPermissions");

        if (!(PermissionManager.hasPermission(
            sender, Permission.COMMAND_GLOBAL, !ignoreWithoutPermissions))) {
          e.setCancelled(!ignoreWithoutPermissions);
          return;
        }

        if (message.equals(symbol)) {
          MessagesService.sendMessage(sender, Messages.MESSAGE_BLANK.get());
          e.setCancelled(true);
          return;
        }

        e.setCancelled(!passToBackendServer);
        MessagesService.sendGlobalMessage(sender, message.substring(1));
      }
    }

    if (account.getChannelType() != ChannelType.GLOBAL) return;

    if (!MessagesService.getGlobalPredicate().test(account)) {
      MessagesService.sendMessage(sender, Messages.NOT_IN_GLOBAL_SERVER.get());

      return;
    }

    e.setCancelled(!passToBackendServer);
    MessagesService.sendGlobalMessage(sender, message);

    return;
  }
}

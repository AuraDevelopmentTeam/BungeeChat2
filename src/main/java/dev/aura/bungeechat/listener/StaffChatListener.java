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

public class StaffChatListener implements Listener {
  private final boolean passToBackendServer =
      BungeecordModuleManager.STAFF_CHAT_MODULE
          .getModuleSection()
          .getBoolean("passToBackendServer");
  private final Config symbolSection =
      BungeecordModuleManager.STAFF_CHAT_MODULE.getModuleSection().getConfig("symbol");

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    String message = e.getMessage();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

    if (ChatUtils.isCommand(message)) return;

    if (symbolSection.getBoolean("enabled")) {
      String symbol = symbolSection.getString("symbol");

      if (message.startsWith(symbol) && !symbol.equals("/")) {
        if (!(PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT))) {
          e.setCancelled(true);
          return;
        }

        if (message.equals(symbol)) {
          MessagesService.sendMessage(sender, Messages.MESSAGE_BLANK.get());
          e.setCancelled(true);
          return;
        }

        e.setCancelled(!passToBackendServer);
        MessagesService.sendStaffMessage(sender, message.substring(1));
      }
    }

    if (account.getChannelType() == ChannelType.STAFF) {
      e.setCancelled(!passToBackendServer);
      MessagesService.sendStaffMessage(sender, message);
    }
  }
}

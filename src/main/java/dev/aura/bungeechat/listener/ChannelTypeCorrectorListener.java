package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.module.ModuleManager;
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

public class ChannelTypeCorrectorListener implements Listener {
  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    BungeeChatAccount player = AccountManager.getAccount(sender.getUniqueId()).get();
    ChannelType channel = player.getChannelType();

    if (((channel == ChannelType.GLOBAL)
            && (!ModuleManager.isModuleActive(BungeecordModuleManager.GLOBAL_CHAT_MODULE)
                || !PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL)))
        || ((channel == ChannelType.LOCAL)
            && (!ModuleManager.isModuleActive(BungeecordModuleManager.LOCAL_CHAT_MODULE)
                || !PermissionManager.hasPermission(sender, Permission.COMMAND_LOCAL)))
        || ((channel == ChannelType.STAFF)
            && (!ModuleManager.isModuleActive(BungeecordModuleManager.STAFF_CHAT_MODULE)
                || !PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)))) {
      e.setCancelled(true);

      ChannelType defaultChannel = player.getDefaultChannelType();

      if (((defaultChannel == ChannelType.GLOBAL)
              && PermissionManager.hasPermissionNoMessage(sender, Permission.COMMAND_GLOBAL))
          || ((defaultChannel == ChannelType.LOCAL)
              && PermissionManager.hasPermissionNoMessage(sender, Permission.COMMAND_LOCAL))) {
        player.setChannelType(defaultChannel);
        MessagesService.sendMessage(sender, Messages.BACK_TO_DEFAULT.get());
      }
    }
  }
}

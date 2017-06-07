package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class StaffChatListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if ((BungeecordAccountManager.getAccount(sender).get().getChannelType() == ChannelType.STAFF)
                && !ChatUtils.isCommand(message)) {

            if (!PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)) {
                BungeecordAccountManager.getAccount(sender).get().setChannelType(ChannelType.LOCAL);
                return;
            }

            e.setCancelled(true);
            MessagesService.sendStaffMessage(sender, message);
        }
    }
}

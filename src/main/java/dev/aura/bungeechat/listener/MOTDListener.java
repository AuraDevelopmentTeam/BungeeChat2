package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.message.Format;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MOTDListener implements Listener {
    @EventHandler
    public void onPlayerJoin(BungeeChatJoinEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if (!PermissionManager.hasPermission(player, Permission.MESSAGE_MOTD))
            return;

        BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(player).get();

        MessagesService.sendMessage(player, Format.MOTD.get(new BungeeChatContext(bungeeChatAccount)));
    }
}

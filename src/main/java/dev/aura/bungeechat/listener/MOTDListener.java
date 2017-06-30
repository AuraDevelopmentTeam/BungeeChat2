package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;

public class MOTDListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (PermissionManager.hasPermission(player, Permission.MESSAGE_MOTD)) {
            BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(player).get();
            ArrayList<String> motd = (ArrayList<String>) BungeecordModuleManager.MOTD_MODULE.getModuleSection().getStringList("message");
            motd.stream().forEach(message -> MessagesService.sendMessage(player, PlaceHolderUtil.formatMessage(message, new BungeeChatContext(bungeeChatAccount))));

        }
    }

}

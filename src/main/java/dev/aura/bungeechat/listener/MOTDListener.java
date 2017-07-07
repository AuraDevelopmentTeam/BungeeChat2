package dev.aura.bungeechat.listener;

import java.util.List;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.BungeecordModuleManager;
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
        List<String> motd = BungeecordModuleManager.MOTD_MODULE.getModuleSection().getStringList("message");

        String message = motd.stream().collect(Collectors.joining("\n"));
        MessagesService.sendMessage(player,
                PlaceHolderUtil.formatMessage(message, new BungeeChatContext(bungeeChatAccount)));
    }
}

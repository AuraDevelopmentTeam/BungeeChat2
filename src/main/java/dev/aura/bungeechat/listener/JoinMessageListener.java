package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinMessageListener implements Listener {
    @EventHandler
    public void onPlayerChat(PostLoginEvent e) {
        if (PermissionManager.hasPermission(e.getPlayer(), Permission.MESSAGE_JOIN)) {
            //TODO: Join Message.
        }
    }
}

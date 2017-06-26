package dev.aura.bungeechat.listener;

import java.util.concurrent.TimeUnit;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.VersionCheckerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@RequiredArgsConstructor
public class VersionCheckerListener implements Listener {
    private static final long FIVE_MINUTES = TimeUnit.MINUTES.toMillis(5);

    private long lastCheck = System.currentTimeMillis();
    private final VersionCheckerModule module;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        if (PermissionManager.hasPermission(player, Permission.CHECK_VERSION)) {
            ProxyServer.getInstance().getScheduler().schedule(BungeeChat.getInstance(),
                    new VersionCheckerTask(player, module.getModuleSection().getBoolean("checkOnAdminLogin")), 1,
                    TimeUnit.SECONDS);
        }
    }

    @RequiredArgsConstructor
    private class VersionCheckerTask implements Runnable {
        private final ProxiedPlayer player;
        private final boolean checkOnAdminLogin;

        @Override
        public void run() {
            BungeeChat instance = BungeeChat.getInstance();

            if (checkOnAdminLogin || ((lastCheck + FIVE_MINUTES) < System.currentTimeMillis())) {
                instance.getLatestVersion(true);
                lastCheck = System.currentTimeMillis();
            }

            if (!instance.isLatestVersion()) {
                MessagesService.sendMessage(player, Message.UPDATE_AVAILABLE.get(player, instance.getLatestVersion()));
            }
        }
    }
}

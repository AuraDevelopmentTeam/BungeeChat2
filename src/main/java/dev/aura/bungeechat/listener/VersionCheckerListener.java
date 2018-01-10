package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.concurrent.TimeUnit;
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
  private final boolean checkOnAdminLogin;

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PostLoginEvent e) {
    ProxiedPlayer player = e.getPlayer();

    if (PermissionManager.hasPermission(player, Permission.CHECK_VERSION)) {
      BungeeChat instance = BungeeChat.getInstance();

      ProxyServer.getInstance()
          .getScheduler()
          .schedule(instance, new VersionCheckerTask(player, instance), 1, TimeUnit.SECONDS);
    }
  }

  @RequiredArgsConstructor
  private class VersionCheckerTask implements Runnable {
    private final ProxiedPlayer player;
    private final BungeeChat instance;

    @Override
    public void run() {
      if (checkOnAdminLogin || ((lastCheck + FIVE_MINUTES) < System.currentTimeMillis())) {
        instance.getLatestVersion(true);
        lastCheck = System.currentTimeMillis();
      }

      if (!instance.isLatestVersion() && player.isConnected()) {
        MessagesService.sendMessage(
            player, Message.UPDATE_AVAILABLE.get(player, instance.getLatestVersion()));
      }
    }
  }
}

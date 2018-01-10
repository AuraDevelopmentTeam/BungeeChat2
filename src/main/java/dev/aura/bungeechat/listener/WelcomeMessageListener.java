package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.message.Format;
import dev.aura.bungeechat.message.MessagesService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class WelcomeMessageListener implements Listener {
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(BungeeChatJoinEvent e) {
    ProxiedPlayer player = e.getPlayer();

    BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(player).get();

    if (!BungeecordAccountManager.isNew(bungeeChatAccount.getUniqueId())) return;

    MessagesService.sendToMatchingPlayers(
        Format.WELCOME_MESSAGE.get(new BungeeChatContext(bungeeChatAccount)));
  }
}

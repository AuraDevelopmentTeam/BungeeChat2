package dev.aura.bungeechat.listener;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import java.util.List;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LocalChatListener implements Listener {
  private final boolean passToBackendServer =
      BungeecordModuleManager.LOCAL_CHAT_MODULE
          .getModuleSection()
          .getBoolean("passToBackendServer");
  private final boolean passTransparently =
      BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection().getBoolean("passTransparently");
  private final boolean logTransparentLocal =
      BungeecordModuleManager.LOCAL_CHAT_MODULE
          .getModuleSection()
          .getBoolean("logTransparentLocal");
  private final Config passThruServerListSection =
      BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection().getConfig("passThruServerList");
  private final boolean passThruServerListEnabled = passThruServerListSection.getBoolean("enabled");
  private final List<String> passThruServers = passThruServerListSection.getStringList("list");
  private final Config globalSymbolSection =
      BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getConfig("symbol");

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();
    String message = e.getMessage();

    if (ChatUtils.isCommand(message)) return;

    if (globalSymbolSection.getBoolean("enabled"))
      if (message.startsWith(globalSymbolSection.getString("symbol"))) return;

    if (account.getChannelType() == ChannelType.LOCAL) {
      if (!MessagesService.getLocalPredicate().test(account)) {
        MessagesService.sendMessage(sender, Messages.NOT_IN_LOCAL_SERVER.get());

        return;
      }

      final boolean passThruServer =
          passTransparently
              || (passThruServerListEnabled && passThruServers.contains(account.getServerName()));

      // Cancel event only if we don't want the backend server to receive it
      e.setCancelled(!(passToBackendServer || passThruServer));
      // Was just cancelled, or we want to process all local chat regardless
      if (e.isCancelled() || (passToBackendServer && !passThruServer)) {
        MessagesService.sendLocalMessage(sender, message);
      }
      // still log and spy after transparently sent messages
      if (passTransparently && logTransparentLocal) {
        MessagesService.sendTransparentMessage(new Context(sender, message));
      }
    }
  }
}

package dev.aura.bungeechat.listener;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
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
  private final Config serverListSection =
      BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection().getConfig("serverList");
  private final boolean serverListDisabled = !serverListSection.getBoolean("enabled");
  private final List<String> passthruServers = serverListSection.getStringList("list");

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();
    String message = e.getMessage();

    if (ChatUtils.isCommand(message)) return;

    if (account.getChannelType() == ChannelType.LOCAL) {

      if (!PermissionManager.hasPermission(sender, Permission.COMMAND_LOCALCHAT)) {
        e.setCancelled(true);
        return;
      }

      // Check we send to this server
      e.setCancelled(
          !(passToBackendServer
              && (serverListDisabled || passthruServers.contains(account.getServerName()))));
      // Was just cancelled, or we want to process all local chat regardless
      if (e.isCancelled() || !passTransparently) {
        MessagesService.sendLocalMessage(sender, message);
      }
      // still log and spy after transparently sent messages
      if (passTransparently && logTransparentLocal) {
        MessagesService.sendTransparentMessage(new Context(sender, message));
      }
    }
  }
}

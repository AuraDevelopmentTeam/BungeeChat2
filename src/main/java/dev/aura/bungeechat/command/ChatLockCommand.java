package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ChatLockModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.ServerNameHelper;
import java.util.Optional;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatLockCommand extends BaseCommand {
  private static final String USAGE = "/chatlock <local [server]|global> [clear]";

  public ChatLockCommand(ChatLockModule chatLockModule) {
    super("chatlock", chatLockModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    // The permission check sends the no permission message
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK)) return;

    BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

    if ((args.length < 1) || (args.length > 2)) {
      MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(player, USAGE));
      return;
    }

    final ChatLockModule chatLock = BungeecordModuleManager.CHAT_LOCK_MODULE;
    final boolean clear = (args.length >= 2) && args[args.length - 1].equalsIgnoreCase("clear");
    final int emptyLines = clear ? chatLock.getModuleSection().getInt("emptyLinesOnClear") : 0;

    if (args[0].equalsIgnoreCase("global")) {
      if (chatLock.isGlobalChatLockEnabled()) {
        chatLock.disableGlobalChatLock();
        MessagesService.sendMessage(sender, Messages.DISABLE_CHATLOCK.get(player));
      } else {
        chatLock.enableGlobalChatLock();

        if (clear) {
          ClearChatCommand.clearGlobalChat(emptyLines);
        }

        MessagesService.sendToMatchingPlayers(
            Messages.ENABLE_CHATLOCK.get(player), MessagesService.getGlobalPredicate());
      }
    } else if (args[0].equalsIgnoreCase("local")) {
      boolean serverSpecified = args.length == (clear ? 3 : 2);

      if (!serverSpecified && !(sender instanceof ProxiedPlayer)) {
        MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(player, USAGE));
        return;
      }

      Optional<String> optServerName =
          ServerNameHelper.verifyServerName(
              serverSpecified ? args[1] : player.getServerName(), sender);

      if (!optServerName.isPresent()) return;

      String serverName = optServerName.get();

      if (chatLock.isLocalChatLockEnabled(serverName)) {
        chatLock.disableLocalChatLock(serverName);
        MessagesService.sendMessage(sender, Messages.DISABLE_CHATLOCK.get(player));
      } else {
        chatLock.enableLocalChatLock(serverName);

        if (clear) {
          ClearChatCommand.clearLocalChat(serverName, emptyLines);
        }

        MessagesService.sendToMatchingPlayers(
            Messages.ENABLE_CHATLOCK.get(player), MessagesService.getLocalPredicate(serverName));
      }
    } else {
      MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(player, USAGE));
    }
  }
}

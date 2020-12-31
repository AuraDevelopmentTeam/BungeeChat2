package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ChatLockModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.ServerNameUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatLockCommand extends BaseCommand {
  private static final String USAGE = "/chatlock <local [server]|global> [clear]";
  private static final String CLEAR = "clear";

  public ChatLockCommand(ChatLockModule chatLockModule) {
    super(
        "chatlock",
        Permission.COMMAND_CHAT_LOCK,
        chatLockModule.getModuleSection().getStringList("aliases"));
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
          ServerNameUtil.verifyServerName(
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

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    final String location = args[0];

    if (args.length == 1) {
      return ClearChatCommand.arg1Completions.stream()
          .filter(completion -> completion.startsWith(location))
          .collect(Collectors.toList());
    } else if ((args.length == 2) && ClearChatCommand.arg1Completions.contains(location)) {
      final String param2 = args[1];
      final List<String> suggestions = new LinkedList<>();

      if (CLEAR.startsWith(param2)) {
        suggestions.add(CLEAR);
      }

      if ("local".equals(location)) {
        suggestions.addAll(ServerNameUtil.getMatchingServerNames(param2));
      }

      return suggestions;
    } else if ((args.length == 3)
        && "local".equals(location)
        && !CLEAR.equals(args[1])
        && CLEAR.startsWith(args[2])) {
      return Collections.singletonList(CLEAR);
    }

    return super.tabComplete(sender, args);
  }
}

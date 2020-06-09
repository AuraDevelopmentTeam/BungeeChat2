package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.AccountType;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageToggleCommand extends BaseCommand {
  public MessageToggleCommand(MessengerModule messengerModule) {
    super(
        "msgtoggle",
        Permission.COMMAND_TOGGLE_MESSAGE,
        messengerModule.getModuleSection().getStringList("aliases.msgtoggle"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE)) return;

    if (args.length == 0) {
      if (!(sender instanceof ProxiedPlayer)) {
        MessagesService.sendMessage(sender, Messages.NOT_A_PLAYER.get());
      } else {
        BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();
        player.toggleMessanger();

        if (player.hasMessangerEnabled()) {
          MessagesService.sendMessage(sender, Messages.ENABLE_MESSAGER.get());
        } else {
          MessagesService.sendMessage(sender, Messages.DISABLE_MESSAGER.get());
        }
      }
    } else if (args.length == 1) {
      if (!PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE_OTHERS))
        return;
      BungeeChatAccount player = BungeecordAccountManager.getAccount(args[0]).get();
      player.toggleMessanger();
      if (player.hasMessangerEnabled()) {
        MessagesService.sendMessage(sender, Messages.ENABLE_MESSAGER_OTHERS.get(player));
      } else {
        MessagesService.sendMessage(sender, Messages.DISABLE_MESSAGER_OTHERS.get(player));
      }
    } else {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/msgtoggle [player]"));
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
      return BungeecordAccountManager.getAccountsForPartialName(args[0], sender).stream()
              .filter(account -> account.getAccountType() == AccountType.PLAYER)
              .map(BungeeChatAccount::getName)
              .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }
}

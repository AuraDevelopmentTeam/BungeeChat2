package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;

public class MessageCommand extends BaseCommand {
  public MessageCommand(MessengerModule messengerModule) {
    super(
        "msg",
        Permission.COMMAND_MESSAGE,
        messengerModule.getModuleSection().getStringList("aliases.message"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) return;

    if (args.length < 2) {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/msg <player> <message>"));
    } else {
      Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[0]);

      if (!targetAccount.isPresent()
          || (targetAccount.get().isVanished()
              && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
        MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
        return;
      }

      CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

      if (target == sender) {
        MessagesService.sendMessage(sender, Messages.MESSAGE_YOURSELF.get());
        return;
      }
      if (!targetAccount.get().hasMessengerEnabled()
          && !PermissionManager.hasPermission(sender, Permission.BYPASS_TOGGLE_MESSAGE)) {
        MessagesService.sendMessage(sender, Messages.HAS_MESSENGER_DISABLED.get(target));
        return;
      }

      String finalMessage = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));

      MessagesService.sendPrivateMessage(sender, target, finalMessage);
      ReplyCommand.setReply(sender, target);
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
      final BungeeChatAccount senderAccount = BungeecordAccountManager.getAccount(sender).get();

      return BungeecordAccountManager.getAccountsForPartialName(args[0], sender).stream()
          .filter(account -> !senderAccount.equals(account))
          .map(BungeeChatAccount::getName)
          .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }
}

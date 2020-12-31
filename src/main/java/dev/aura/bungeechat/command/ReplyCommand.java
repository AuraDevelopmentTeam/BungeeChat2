package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.HashMap;
import java.util.Optional;
import net.md_5.bungee.api.CommandSender;

public class ReplyCommand extends BaseCommand {
  private static HashMap<CommandSender, CommandSender> replies;

  public ReplyCommand(MessengerModule messengerModule) {
    super(
        "reply",
        Permission.COMMAND_MESSAGE,
        messengerModule.getModuleSection().getStringList("aliases.reply"));

    if (replies == null) {
      replies = new HashMap<>();
    } else {
      replies.clear();
    }
  }

  protected static void setReply(CommandSender sender, CommandSender target) {
    replies.put(sender, target);
    replies.put(target, sender);
  }

  private static CommandSender getReplier(CommandSender player) {
    return replies.get(player);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) return;

    if (args.length < 1) {
      MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(sender, "/reply <message>"));
      return;
    }

    CommandSender initialTarget = getReplier(sender);
    Optional<BungeeChatAccount> targetAccount = BungeecordAccountManager.getAccount(initialTarget);

    if ((initialTarget != null) && !targetAccount.isPresent()) {
      MessagesService.sendMessage(sender, Messages.REPLY_OFFLINE.get());
      return;
    } else if (!targetAccount.isPresent()
        || (targetAccount.get().isVanished()
            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
      MessagesService.sendMessage(sender, Messages.NO_REPLY.get());
      return;
    }

    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

    if (!targetAccount.get().hasMessengerEnabled()
        && !PermissionManager.hasPermission(sender, Permission.BYPASS_TOGGLE_MESSAGE)) {
      MessagesService.sendMessage(sender, Messages.HAS_MESSENGER_DISABLED.get(target));
      return;
    }

    String finalMessage = String.join(" ", args);

    MessagesService.sendPrivateMessage(sender, target, finalMessage);
    ReplyCommand.setReply(sender, target);
  }
}

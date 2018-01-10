package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MutingModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Optional;
import net.md_5.bungee.api.CommandSender;

public class MuteCommand extends BaseCommand {
  public MuteCommand(MutingModule mutingModule) {
    super("mute", mutingModule.getModuleSection().getStringList("aliases.mute"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_MUTE)) {
      if (args.length < 1) {
        MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/mute <player>"));
      } else {
        Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[0]);

        if (!targetAccount.isPresent()) {
          MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
          return;
        }

        CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

        if (targetAccount.get().isMuted()) {
          MessagesService.sendMessage(sender, Message.MUTE_IS_MUTED.get());
          return;
        }

        targetAccount.get().mutePermanetly();
        MessagesService.sendMessage(sender, Message.MUTE.get(target));
      }
    }
  }
}

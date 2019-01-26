package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.utils.TimeUtil;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MutingModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Optional;
import net.md_5.bungee.api.CommandSender;

public class TempMuteCommand extends BaseCommand {
  public TempMuteCommand(MutingModule mutingModule) {
    super("tempmute", mutingModule.getModuleSection().getStringList("aliases.tempmute"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_TEMPMUTE)) {
      if (args.length < 2) {
        MessagesService.sendMessage(
            sender, Messages.INCORRECT_USAGE.get(sender, "/tempmute <player> <time>"));
      } else {
        Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[0]);

        if (!targetAccount.isPresent()) {
          MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
          return;
        }

        CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

        if (targetAccount.get().isMuted()) {
          MessagesService.sendMessage(sender, Messages.MUTE_IS_MUTED.get());
          return;
        }

        double timeAmount = TimeUtil.convertStringTimeToDouble(args[1]),
            currentTime = System.currentTimeMillis();
        java.sql.Timestamp timeStamp = new java.sql.Timestamp((long) (currentTime + timeAmount));
        targetAccount.get().setMutedUntil(timeStamp);
        MessagesService.sendMessage(sender, Messages.TEMPMUTE.get(target));
      }
    }
  }
}

package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MutingModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;

public class MuteCommand extends BaseCommand {
  public MuteCommand(MutingModule mutingModule) {
    super("mute", mutingModule.getModuleSection().getStringList("aliases.mute"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_MUTE)) return;

    if (args.length < 1) {
      MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(sender, "/mute <player>"));
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

      targetAccount.get().mutePermanetly();
      MessagesService.sendMessage(sender, Messages.MUTE.get(target));
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
      return BungeecordAccountManager.getAccountsForPartialName(args[0]).stream()
          .map(BungeeChatAccount::getName)
          .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }
}

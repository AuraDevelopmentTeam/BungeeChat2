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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;

public class TempMuteCommand extends BaseCommand {
  private static final List<String> timeUnits = Arrays.asList("s", "m", "h", "d", "w", "mo", "y");
  private static final Pattern digitsAndUnit =
      Pattern.compile("(\\d+(?:\\.\\d*)?)[a-z]*", Pattern.CASE_INSENSITIVE);

  public TempMuteCommand(MutingModule mutingModule) {
    super(
        "tempmute",
        Permission.COMMAND_TEMPMUTE,
        mutingModule.getModuleSection().getStringList("aliases.tempmute"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_TEMPMUTE)) return;

    if (args.length < 2) {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/tempmute <player> <time>"));
      return;
    }

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

    final double timeAmount = TimeUtil.convertStringTimeToDouble(args[1]);
    final double currentTime = System.currentTimeMillis();
    final java.sql.Timestamp timeStamp = new java.sql.Timestamp((long) (currentTime + timeAmount));
    targetAccount.get().setMutedUntil(timeStamp);
    MessagesService.sendMessage(sender, Messages.TEMPMUTE.get(target));
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
      return BungeecordAccountManager.getAccountsForPartialName(args[0]).stream()
          .map(BungeeChatAccount::getName)
          .collect(Collectors.toList());
    } else if (args.length == 2) {
      final String time = args[1];
      String digits = null;

      Matcher match = digitsAndUnit.matcher(time);

      if (time.isEmpty()) {
        digits = "<duration>";
      } else if (match.matches()) {
        digits = match.group(1);
      }

      if (digits != null) {
        final String finalDigits = digits;

        return timeUnits.stream()
            .map(unit -> finalDigits + unit)
            .filter(timeStr -> timeStr.startsWith(time))
            .collect(Collectors.toList());
      }
    }

    return super.tabComplete(sender, args);
  }
}

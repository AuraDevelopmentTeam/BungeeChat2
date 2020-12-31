package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.IgnoringModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class IgnoreCommand extends BaseCommand {
  private static final List<String> arg1Completions = Arrays.asList("list", "add", "remove");

  public IgnoreCommand(IgnoringModule ignoringModule) {
    super(
        "ignore",
        Permission.COMMAND_IGNORE,
        ignoringModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      MessagesService.sendMessage(sender, Messages.NOT_A_PLAYER.get());
      return;
    }

    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE)) return;

    if (args.length < 1) {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
      return;
    }

    BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

    if (args[0].equalsIgnoreCase("list")) {
      List<Optional<BungeeChatAccount>> ignored =
          player.getIgnored().stream()
              .map(AccountManager::getAccount)
              .filter(Optional::isPresent)
              .collect(Collectors.toList());

      if (ignored.size() <= 0) {
        MessagesService.sendMessage(sender, Messages.IGNORE_NOBODY.get(player));
      } else {
        String list =
            ignored.stream()
                .map(account -> account.get().getName())
                .collect(Collectors.joining(", "));

        MessagesService.sendMessage(sender, Messages.IGNORE_LIST.get(player, list));
      }
    } else if (args[0].equalsIgnoreCase("add")) {
      if (args.length < 2) {
        MessagesService.sendMessage(
            sender, Messages.INCORRECT_USAGE.get(sender, "/ignore add <player>"));
        return;
      }

      Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

      if (!targetAccount.isPresent()
          || (targetAccount.get().isVanished()
              && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
        MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
        return;
      }

      CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

      if (target == sender) {
        MessagesService.sendMessage(sender, Messages.IGNORE_YOURSELF.get());
        return;
      }

      if (player.hasIgnored(targetAccount.get().getUniqueId())) {
        MessagesService.sendMessage(sender, Messages.ALREADY_IGNORED.get());
        return;
      }

      player.addIgnore(targetAccount.get().getUniqueId());
      MessagesService.sendMessage(sender, Messages.ADD_IGNORE.get(target));
    } else if (args[0].equalsIgnoreCase("remove")) {
      if (args.length < 2) {
        MessagesService.sendMessage(
            sender, Messages.INCORRECT_USAGE.get(sender, "/ignore remove <player>"));
        return;
      }

      Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

      if (!targetAccount.isPresent()
          || (targetAccount.get().isVanished()
              && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
        MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
        return;
      }

      CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

      if (target == sender) {
        MessagesService.sendMessage(sender, Messages.UNIGNORE_YOURSELF.get());
        return;
      }

      if (!player.hasIgnored(targetAccount.get().getUniqueId())) {
        MessagesService.sendMessage(sender, Messages.NOT_IGNORED.get());
        return;
      }

      player.removeIgnore(targetAccount.get().getUniqueId());
      MessagesService.sendMessage(sender, Messages.REMOVE_IGNORE.get(target));
    } else {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    final String param1 = args[0];

    if (args.length == 1) {
      return arg1Completions.stream()
          .filter(completion -> completion.startsWith(param1))
          .collect(Collectors.toList());
    } else if ((args.length == 2) && ("add".equals(param1) || "remove".equals(param1))) {
      final BungeeChatAccount senderAccount = BungeecordAccountManager.getAccount(sender).get();

      return BungeecordAccountManager.getAccountsForPartialName(args[1], sender).stream()
          .filter(account -> !senderAccount.equals(account))
          .map(BungeeChatAccount::getName)
          .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }
}

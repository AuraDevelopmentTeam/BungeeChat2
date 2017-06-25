package dev.aura.bungeechat.command;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.IgnoringModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class IgnoreCommand extends BaseCommand {
    public IgnoreCommand(IgnoringModule ignoringModule) {
        super("ignore", ignoringModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE)) {
            if (!(sender instanceof ProxiedPlayer)) {
                MessagesService.sendMessage(sender, Message.NOT_A_PLAYER.get());
            } else {

                if (args.length < 1) {
                    MessagesService.sendMessage(sender,
                            Message.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
                    return;
                }

                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                if (args[0].equalsIgnoreCase("list")) {
                    List<Optional<BungeeChatAccount>> ignored = player.getIgnored().stream()
                            .map(AccountManager::getAccount).filter(Optional::isPresent)
                            .collect(Collectors.toList());

                    if (ignored.size() <= 0) {
                        MessagesService.sendMessage(sender, Message.IGNORE_NOBODY.get(player));
                    } else {
                        String list = ignored.stream().map(account -> account.get().getName())
                                .collect(Collectors.joining(", "));

                        MessagesService.sendMessage(sender, Message.IGNORE_LIST.get(player, list));
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length < 2) {
                        MessagesService.sendMessage(sender,
                                Message.INCORRECT_USAGE.get(sender, "/ignore add <player>"));
                        return;
                    }

                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
                        MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
                        return;
                    }

                    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                    if (target == sender) {
                        MessagesService.sendMessage(sender, Message.IGNORE_YOURSELF.get());
                        return;
                    }

                    if (player.hasIgnored(targetAccount.get().getUniqueId())) {
                        MessagesService.sendMessage(sender, Message.ALREADY_IGNORED.get());
                        return;
                    }

                    player.addIgnore(targetAccount.get().getUniqueId());
                    MessagesService.sendMessage(sender, Message.ADD_IGNORE.get(target));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length < 2) {
                        MessagesService.sendMessage(sender,
                                Message.INCORRECT_USAGE.get(sender, "/ignore remove <player>"));
                        return;
                    }

                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
                        MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
                        return;
                    }

                    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                    if (target == sender) {
                        MessagesService.sendMessage(sender, Message.UNIGNORE_YOURSELF.get());
                        return;
                    }

                    if (!player.hasIgnored(targetAccount.get().getUniqueId())) {
                        MessagesService.sendMessage(sender, Message.NOT_IGNORED.get());
                        return;
                    }

                    player.removeIgnore(targetAccount.get().getUniqueId());
                    MessagesService.sendMessage(sender, Message.REMOVE_IGNORE.get(target));
                } else {
                    MessagesService.sendMessage(sender,
                            Message.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
                }
            }
        }
    }
}

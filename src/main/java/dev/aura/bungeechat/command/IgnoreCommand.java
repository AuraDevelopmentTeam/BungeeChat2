package dev.aura.bungeechat.command;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.IgnoringModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class IgnoreCommand extends BaseCommand {
    public IgnoreCommand(IgnoringModule ignoringModule) {
        super("ignore", ignoringModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {

                if (args.length < 1) {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
                    return;
                }

                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                if (args[0].equalsIgnoreCase("list")) {
                    Stream<Optional<BungeeChatAccount>> ignored = player.getIgnored().stream()
                            .map(uuid -> AccountManager.getAccount(uuid)).filter(account -> account.isPresent());

                    if (ignored.count() <= 0) {
                        sender.sendMessage(Message.IGNORE_NOBODY.get(player));
                    } else {
                        String list = ignored.map(account -> account.get().getName()).collect(Collectors.joining(", "));

                        sender.sendMessage(Message.IGNORE_LIST.get(player, list));
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length < 2) {
                        sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/ignore add <player>"));
                        return;
                    }

                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                        sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                        return;
                    }

                    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                    if (target == sender) {
                        sender.sendMessage(Message.IGNORE_YOURSELF.get());
                        return;
                    }

                    if (player.hasIgnored(targetAccount.get().getUniqueId())) {
                        sender.sendMessage(Message.ALREADY_IGNORED.get());
                        return;
                    }

                    player.addIgnore(targetAccount.get().getUniqueId());
                    sender.sendMessage(Message.ADD_IGNORE.get(target));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length < 2) {
                        sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/ignore remove <player>"));
                        return;
                    }

                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                        sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                        return;
                    }

                    CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                    if (target == sender) {
                        sender.sendMessage(Message.UNIGNORE_YOURSELF.get());
                        return;
                    }

                    if (!player.hasIgnored(targetAccount.get().getUniqueId())) {
                        sender.sendMessage(Message.NOT_IGNORED.get());
                        return;
                    }

                    player.removeIgnore(targetAccount.get().getUniqueId());
                    sender.sendMessage(Message.REMOVE_IGNORE.get(target));
                } else {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/ignore <list|add|remove> [player]"));
                }
            }
        }
    }
}

package dev.aura.bungeechat.command;

import java.util.Optional;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class MessageCommand extends BaseCommand {
    public MessageCommand(MessengerModule messengerModule) {
        super("msg", messengerModule.getModuleSection().getStringList("aliases.message"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            if (args.length < 2) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/msg <player> <message>"));
            } else {
                Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[0]);

                if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                    sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                    return;
                }

                CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                if (target == sender) {
                    sender.sendMessage(Message.MESSAGE_YOURSELF.get());
                    return;
                }
                if (!targetAccount.get().hasMessangerEnabled()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE_BYPASS)) {
                    sender.sendMessage(Message.HAS_MESSAGER_DISABLED.get(target));
                    return;
                }
                if (targetAccount.get().hasIgnored(BungeecordAccountManager.getAccount(sender).get())
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE_BYPASS)) {
                    sender.sendMessage(Message.HAS_INGORED.get());
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }

                MessagesService.sendPrivateMessage(sender, target, stringBuilder.toString().trim());
                ReplyCommand.setReply(sender, target);
            }
        }
    }
}

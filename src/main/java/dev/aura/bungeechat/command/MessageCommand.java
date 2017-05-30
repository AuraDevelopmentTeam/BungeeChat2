package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

                if (target == sender) {
                    sender.sendMessage(Message.MESSAGE_YOURSELF.get());
                    return;
                }
                if ((target == null) || (AccountManager.getUserAccount(target).isVanished()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                    sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                    return;
                }
                if ((sender instanceof ProxiedPlayer) && !AccountManager.getUserAccount(target).hasMessangerEnabled()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE_BYPASS)) {
                    sender.sendMessage(Message.HAS_MESSAGER_DISABLED.get(target));
                    return;
                }
                if ((sender instanceof ProxiedPlayer)
                        && AccountManager.getUserAccount(target).getIgnored()
                                .contains(((ProxiedPlayer) sender).getUniqueId())
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE_BYPASS)) {
                    sender.sendMessage(Message.HAS_INGORED.get());
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }

                MessagesService.sendPrivateMessage(sender, target, stringBuilder.toString().trim());

                if (sender instanceof ProxiedPlayer) {
                    ReplyCommand.setReply((ProxiedPlayer) sender, target);
                }

                // TODO: Logger..
            }
        }
    }
}

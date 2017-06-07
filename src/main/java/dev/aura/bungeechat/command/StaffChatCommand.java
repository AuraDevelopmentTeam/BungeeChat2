package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.StaffChatModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffChatCommand extends BaseCommand {
    public StaffChatCommand(StaffChatModule staffChatModule) {
        super("staffchat", staffChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
                return;
            }
            if (BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("default")) {
                sender.sendMessage(Message.GLOBAL_IS_DEFAULT.get());
                return;
            }
            if (args.length == 0) {
                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                if (player.getChannelType() == ChannelType.STAFF) {
                    player.setChannelType(ChannelType.LOCAL);
                    sender.sendMessage(Message.ENABLE_LOCAL.get());
                } else {
                    player.setChannelType(ChannelType.STAFF);
                    sender.sendMessage(Message.ENABLE_STAFFCHAT.get());
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                MessagesService.sendStaffMessage(sender, stringBuilder.toString().trim());
            }
        }
    }
}

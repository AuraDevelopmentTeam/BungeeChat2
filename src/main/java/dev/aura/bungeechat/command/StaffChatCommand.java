package dev.aura.bungeechat.command;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.StaffChatModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class StaffChatCommand extends BaseCommand {
    public StaffChatCommand(StaffChatModule staffChatModule) {
        super("staffchat", staffChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)) {
            if (args.length == 0) {
                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                if (player.getChannelType() == ChannelType.STAFF) {
                    player.setChannelType(ChannelType.LOCAL);
                    MessagesService.sendMessage(sender, Message.ENABLE_LOCAL.get());
                } else {
                    player.setChannelType(ChannelType.STAFF);
                    MessagesService.sendMessage(sender, Message.ENABLE_STAFFCHAT.get());
                }
            } else {
                String finalMessage = Arrays.stream(args).collect(Collectors.joining(" "));

                MessagesService.sendStaffMessage(sender, finalMessage);
            }
        }
    }
}

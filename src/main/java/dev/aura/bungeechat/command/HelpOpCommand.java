package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.HelpOpModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class HelpOpCommand extends BaseCommand {
    public HelpOpCommand(HelpOpModule helpOpModule) {
        super("helpop", helpOpModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_HELPOP)) {
            if (args.length < 1) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/helpop <message>"));
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                MessagesService.sendChannelMessage(sender, ChannelType.HELP, stringBuilder.toString().trim());
            }
        }
    }
}

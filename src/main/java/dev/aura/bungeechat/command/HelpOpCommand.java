package dev.aura.bungeechat.command;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_HELPOP)) {
            if (args.length < 1) {
                MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/helpop <message>"));
            } else {
                String finalMessage = Arrays.stream(args).collect(Collectors.joining(" "));

                MessagesService.sendHelpMessage(sender, finalMessage);
            }
        }
    }
}

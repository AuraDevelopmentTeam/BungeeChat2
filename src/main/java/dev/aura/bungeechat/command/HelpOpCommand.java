package dev.aura.bungeechat.command;

import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.HelpOpModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;

public class HelpOpCommand extends BaseCommand {
  public HelpOpCommand(HelpOpModule helpOpModule) {
    super("helpop", helpOpModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_HELPOP)) {
      if (args.length < 1) {
        MessagesService.sendMessage(
            sender, Messages.INCORRECT_USAGE.get(sender, "/helpop <message>"));
      } else {
        String finalMessage = Arrays.stream(args).collect(Collectors.joining(" "));

        MessagesService.sendHelpMessage(sender, finalMessage);
      }
    }
  }
}

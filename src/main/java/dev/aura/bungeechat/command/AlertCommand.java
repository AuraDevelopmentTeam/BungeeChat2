package dev.aura.bungeechat.command;

import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Format;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.AlertModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class AlertCommand extends BaseCommand {
  public AlertCommand(AlertModule alertModule) {
    super("alert", alertModule.getModuleSection().getStringList("aliases"));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_ALERT)) {
      if (args.length < 1) {
        MessagesService.sendMessage(
            sender, Message.INCORRECT_USAGE.get(sender, "/alert <message>"));
      } else {
        String finalMessage =
            PlaceHolderUtil.transformAltColorCodes(
                Arrays.stream(args).collect(Collectors.joining(" ")));
        String format = Format.ALERT.get(new Context(sender, finalMessage));

        ProxyServer.getInstance().broadcast(format);
      }
    }
  }
}

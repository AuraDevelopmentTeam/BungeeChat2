package dev.aura.bungeechat.command;

import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Format;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.AlertModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class AlertCommand extends BaseCommand {
  public AlertCommand(AlertModule alertModule) {
    super(
        "alert", Permission.COMMAND_ALERT, alertModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_ALERT)) {
      if (args.length < 1) {
        MessagesService.sendMessage(
            sender, Messages.INCORRECT_USAGE.get(sender, "/alert <message>"));
      } else {
        String finalMessage = PlaceHolderUtil.transformAltColorCodes(String.join(" ", args));
        String format = Format.ALERT.get(new Context(sender, finalMessage));

        ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(format));
      }
    }
  }
}

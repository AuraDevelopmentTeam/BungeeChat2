package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.StaffChatModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class StaffChatCommand extends BaseCommand {
  public StaffChatCommand(StaffChatModule staffChatModule) {
    super(
        "staffchat",
        Permission.COMMAND_STAFFCHAT,
        staffChatModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)) return;

    if (args.length == 0) {
      BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

      if (player.getChannelType() == ChannelType.STAFF) {
        ChannelType defaultChannelType = player.getDefaultChannelType();
        player.setChannelType(defaultChannelType);

        if (defaultChannelType == ChannelType.LOCAL) {
          MessagesService.sendMessage(sender, Messages.ENABLE_LOCAL.get());
        } else {
          MessagesService.sendMessage(sender, Messages.ENABLE_GLOBAL.get());
        }
      } else {
        player.setChannelType(ChannelType.STAFF);
        MessagesService.sendMessage(sender, Messages.ENABLE_STAFFCHAT.get());
      }
    } else {
      String finalMessage = String.join(" ", args);

      MessagesService.sendStaffMessage(sender, finalMessage);
    }
  }
}

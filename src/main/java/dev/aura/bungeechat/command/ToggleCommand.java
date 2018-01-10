package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ToggleCommand extends BaseCommand {
  public ToggleCommand(MessengerModule messengerModule) {
    super("msgtoggle", messengerModule.getModuleSection().getStringList("aliases.msgtoggle"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE)) {
      if (!(sender instanceof ProxiedPlayer)) {
        MessagesService.sendMessage(sender, Message.NOT_A_PLAYER.get());
      } else {
        BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();
        player.toggleMessanger();

        if (player.hasMessangerEnabled()) {
          MessagesService.sendMessage(sender, Message.ENABLE_MESSAGER.get());
        } else {
          MessagesService.sendMessage(sender, Message.DISABLE_MESSAGER.get());
        }
      }
    }
  }
}

package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.SpyModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SocialSpyCommand extends BaseCommand {
  public SocialSpyCommand(SpyModule socialSpyModule) {
    super("socialspy", socialSpyModule.getModuleSection().getStringList("aliases.socialspy"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_SOCIALSPY)) {
      if (!(sender instanceof ProxiedPlayer)) {
        MessagesService.sendMessage(sender, Messages.NOT_A_PLAYER.get());
      } else {
        BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();
        player.toggleSocialSpy();

        if (player.hasSocialSpyEnabled()) {
          MessagesService.sendMessage(sender, Messages.ENABLE_SOCIAL_SPY.get(player));
        } else {
          MessagesService.sendMessage(sender, Messages.DISABLE_SOCIAL_SPY.get(player));
        }
      }
    }
  }
}

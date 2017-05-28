package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.SocialSpyModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SocialSpyCommand extends BaseCommand {
    public SocialSpyCommand(SocialSpyModule socialSpyModule) {
        super("socialspy", socialSpyModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_SOCIALSPY)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                AccountManager.getUserAccount(player).toggleSocialSpy();
                if (!AccountManager.getUserAccount(player).hasSocialSpyEnabled()) {
                    player.sendMessage(Message.DISABLE_SOCIALSPY.get());
                } else {
                    player.sendMessage(Message.ENABLE_SOCIALSPY.get());
                }
            }
        }
    }
}

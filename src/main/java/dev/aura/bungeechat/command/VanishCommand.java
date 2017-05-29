package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.VanisherModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VanishCommand extends BaseCommand {
    public VanishCommand(VanisherModule vanisherModule) {
        super("bvanish", vanisherModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                AccountManager.getUserAccount(player).toggleVanished();
                if (!AccountManager.getUserAccount(player).isVanished()) {
                    player.sendMessage(Message.DISABLE_VANISH.get());
                } else {
                    player.sendMessage(Message.ENABLE_VANISH.get());
                }
            }
        }
    }
}

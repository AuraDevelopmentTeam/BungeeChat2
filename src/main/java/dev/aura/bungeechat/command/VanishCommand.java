package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.VanishModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VanishCommand extends BaseCommand {
    public VanishCommand(VanishModule vanisherModule) {
        super("bvanish", vanisherModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();
                player.toggleVanished();

                if (player.isVanished()) {
                    sender.sendMessage(Message.ENABLE_VANISH.get());
                } else {
                    sender.sendMessage(Message.DISABLE_VANISH.get());
                }
            }
        }
    }
}

package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.VanishModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VanishCommand extends BaseCommand {
    public VanishCommand(VanishModule vanisherModule) {
        super("bvanish", vanisherModule.getModuleSection().getStringList("aliases"));
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH)) {
            if (!(sender instanceof ProxiedPlayer)) {
                MessagesService.sendMessage(sender, Message.NOT_A_PLAYER.get());
            } else {
                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("on")) {
                        player.setVanished(true);
                    } else if (args[0].equalsIgnoreCase("off")){
                        player.setVanished(false);
                    } else {
                        player.toggleVanished();
                    }
                } else {
                    player.toggleVanished();
                }

                if (player.isVanished()) {
                    MessagesService.sendMessage(sender, Message.ENABLE_VANISH.get());
                } else {
                    MessagesService.sendMessage(sender, Message.DISABLE_VANISH.get());
                }
            }
        }
    }
}

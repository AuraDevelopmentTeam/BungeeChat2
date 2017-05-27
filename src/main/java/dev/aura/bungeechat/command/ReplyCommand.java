package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends BaseCommand {
    public ReplyCommand(MessengerModule messengerModule) {
        super("reply", messengerModule.getModuleSection().getStringList("aliases.reply"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            if (args.length < 2) {

            } else {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);


            }
        }
    }
}

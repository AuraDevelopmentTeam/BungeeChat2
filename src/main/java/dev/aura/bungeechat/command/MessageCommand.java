package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageCommand extends BaseCommand {
    public MessageCommand(MessengerModule messengerModule) {
        super("msg", messengerModule.getModuleSection().getStringList("aliases.message"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            if (args.length < 2) {
                //TODO: Args Message.
            } else {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);


            }
        }
    }
}

package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class ReplyCommand extends BaseCommand {
    private static HashMap<ProxiedPlayer, ProxiedPlayer> replies;

    public ReplyCommand(MessengerModule messengerModule) {
        super("reply", messengerModule.getModuleSection().getStringList("aliases.reply"));
        replies = new HashMap();
    }

    public static void setReply(ProxiedPlayer sender, ProxiedPlayer target) {
        replies.put(sender, target);
        replies.put(target, sender);
    }

    public static ProxiedPlayer getReplier(ProxiedPlayer player) {
        return replies.getOrDefault(player, null);
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

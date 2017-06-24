package dev.aura.bungeechat.permission;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PermissionManager {
    public static boolean hasPermission(ProxiedPlayer player, Permission permission) {
        if (player.hasPermission(permission.getStringedPermission()))
            return true;
        else {
            if (permission.getWarnOnLackingPermission()) {
                MessagesService.sendMessage(player, Message.NO_PERMISSION.get(player));
            }

            return false;
        }
    }

    public static boolean hasPermission(CommandSender sender, Permission permission) {
        if (sender instanceof ProxiedPlayer)
            return hasPermission((ProxiedPlayer) sender, permission);
        else
            return true;
    }

    public static boolean hasPermission(BungeeChatAccount account, Permission permission) {
        return hasPermission(BungeecordAccountManager.getCommandSender(account).get(), permission);
    }
}

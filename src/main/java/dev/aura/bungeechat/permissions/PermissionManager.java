package dev.aura.bungeechat.permissions;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionManager {

    public static boolean hasPermission(ProxiedPlayer player, Permission permission){
        if (player.hasPermission(permission.getStringedPermission())) return true;
        else {
            if ((permission.equals(Permission.BYPASS_ANTI_ADVERTISEMENT) || permission.equals(Permission.BYPASS_ANTI_SPAM) || permission.equals(Permission.BYPASS_ANTI_SWEAR))
                    && player.hasPermission(Permission.BYPASS_ALL.getStringedPermission())) return true;
            else return false;
        }
    }

    public static boolean hasPermission(CommandSender sender, Permission permission){
        if (sender instanceof ProxiedPlayer) return hasPermission((ProxiedPlayer) sender, permission);
        else return true;
    }

}

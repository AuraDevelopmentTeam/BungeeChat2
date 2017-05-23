package dev.aura.bungeechat.permissions;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Permissions {

    public static boolean hasPermission(ProxiedPlayer player, Permission permission){
        if (player.hasPermission(permission.getStringedPermission())) return true;
        else {
            if ((permission.equals(Permission.BYPASS_ANTI_ADVERTISEMENT) || permission.equals(Permission.BYPASS_ANTI_SPAM) || permission.equals(Permission.BYPASS_ANTI_SWEAR))
                    && player.hasPermission(Permission.BYPASS_ALL.getStringedPermission())) return true;
            else return false;
        }
    }

}

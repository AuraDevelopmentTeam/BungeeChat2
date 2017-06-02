package dev.aura.bungeechat.permission;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PermissionManager {
    @SuppressWarnings("deprecation")
    public static boolean hasPermission(ProxiedPlayer player, Permission permission) {
        if (player.hasPermission(permission.getStringedPermission()))
            return true;
        else {
            if (permission.getWarnOnLackingPermission()) {
                player.sendMessage(PlaceHolderUtil.getFullMessage("no-permission", new Context(player)));
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
        return hasPermission(ProxyServer.getInstance().getPlayer(account.getUniqueId()), permission);
    }
}

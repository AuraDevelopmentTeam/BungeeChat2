package dev.aura.bungeechat.permission;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.config.Config;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PermissionManager {
    public static boolean hasPermission(ProxiedPlayer player, Permission permission) {
        if (player.hasPermission(permission.getStringedPermission()))
            return true;
        else {
            if (permission.getStringedPermission().startsWith("bungeechat.chat.")
                    && player.hasPermission(Permission.BYPASS_ALL.getStringedPermission()))
                return true;
            else if (permission.getStringedPermission().startsWith("bungeechat.command.")
                    && player.hasPermission(Permission.COMMAND_ALL.getStringedPermission()))
                return true;
            else {
                if (!permission.equals(Permission.USE_COLORED_CHAT) && !permission.equals(Permission.COMMAND_VANISH_SEE)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Config.get().getString("Messages.no-permission")));
                }
                return false;
            }
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

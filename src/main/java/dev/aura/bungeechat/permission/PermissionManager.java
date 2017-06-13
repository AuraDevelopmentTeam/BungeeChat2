package dev.aura.bungeechat.permission;

import java.util.regex.Pattern;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PermissionManager {
    /**
     * Greedily matches all letters from the last dot, if it exists, if not from
     * the beginning, to the end.<br>
     * So <code>bungeechat.chat.bypass.chatlock</code> will match
     * <code>.chatlock</code> and <code>bungeechat</code> will match
     * <code>bungeechat</code>.
     */
    private static final Pattern LAST_NODE_REMOVER = Pattern.compile("\\.?\\w+$");

    @SuppressWarnings("deprecation")
    public static boolean hasPermission(ProxiedPlayer player, Permission permission) {
        String permissionString = permission.getStringedPermission();

        while (!permissionString.isEmpty()) {
            if (player.hasPermission(permissionString))
                return true;

            permissionString = LAST_NODE_REMOVER.matcher(permissionString).replaceFirst("");
        }

        if (permission.getWarnOnLackingPermission()) {
            player.sendMessage(Message.NO_PERMISSION.get(player));
        }

        return false;
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

package dev.aura.bungeechat.permission;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PermissionManager {
  public static boolean hasPermissionNoMessage(ProxiedPlayer player, Permission permission) {
    return player.hasPermission(permission.getStringedPermission());
  }

  public static boolean hasPermissionNoMessage(CommandSender sender, Permission permission) {
    return !(sender instanceof ProxiedPlayer)
        || hasPermissionNoMessage((ProxiedPlayer) sender, permission);
  }

  public static boolean hasPermissionNoMessage(BungeeChatAccount account, Permission permission) {
    return hasPermissionNoMessage(
        BungeecordAccountManager.getCommandSender(account).get(), permission);
  }

  public static boolean hasPermission(ProxiedPlayer player, Permission permission) {
    return hasPermission(player, permission, true);
  }

  public static boolean hasPermission(CommandSender sender, Permission permission) {
    return !(sender instanceof ProxiedPlayer) || hasPermission((ProxiedPlayer) sender, permission);
  }

  public static boolean hasPermission(BungeeChatAccount account, Permission permission) {
    return hasPermission(BungeecordAccountManager.getCommandSender(account).get(), permission);
  }

  public static boolean hasPermission(
      ProxiedPlayer player, Permission permission, boolean sendMessage) {
    if (hasPermissionNoMessage(player, permission)) return true;
    else {
      if (sendMessage && permission.getWarnOnLackingPermission()) {
        MessagesService.sendMessage(player, Messages.NO_PERMISSION.get(player));
      }

      return false;
    }
  }

  public static boolean hasPermission(
      CommandSender sender, Permission permission, boolean sendMessage) {
    return !(sender instanceof ProxiedPlayer)
        || hasPermission((ProxiedPlayer) sender, permission, sendMessage);
  }

  public static boolean hasPermission(
      BungeeChatAccount account, Permission permission, boolean sendMessage) {
    return hasPermission(
        BungeecordAccountManager.getCommandSender(account).get(), permission, sendMessage);
  }
}

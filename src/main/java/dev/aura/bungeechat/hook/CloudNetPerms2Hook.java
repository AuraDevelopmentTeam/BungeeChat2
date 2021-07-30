package dev.aura.bungeechat.hook;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Optional;

public class CloudNetPerms2Hook implements BungeeChatHook {
  private final CloudAPI api;

  public CloudNetPerms2Hook() {
    api = CloudAPI.getInstance();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getPermissionGroup(account).map(PermissionGroup::getPrefix);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getPermissionGroup(account).map(PermissionGroup::getSuffix);
  }

  private Optional<PermissionGroup> getPermissionGroup(BungeeChatAccount account) {
    CloudPlayer player = api.getOnlinePlayer(account.getUniqueId());
    PermissionGroup permissionGroup =
        player.getPermissionEntity().getHighestPermissionGroup(api.getPermissionPool());

    return Optional.ofNullable(permissionGroup);
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }

  public boolean permissionsEnabled() {
    PermissionPool permissionPool = api.getPermissionPool();

    return (permissionPool != null) && permissionPool.isAvailable();
  }
}

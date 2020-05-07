package dev.aura.bungeechat.hook;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsManagement;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Objects;
import java.util.Optional;

public class CloudNetPerms3Hook implements BungeeChatHook {
  private final CloudPermissionsManagement api;

  public CloudNetPerms3Hook() {
    api = CloudPermissionsManagement.getInstance();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getPermissionGroup(account).map(IPermissionGroup::getPrefix).filter(Objects::nonNull);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getPermissionGroup(account).map(IPermissionGroup::getSuffix).filter(Objects::nonNull);
  }

  private Optional<IPermissionGroup> getPermissionGroup(BungeeChatAccount account) {
    IPermissionUser player = api.getUser(account.getUniqueId());
    IPermissionGroup permissionGroup = api.getHighestPermissionGroup(player);

    return Optional.ofNullable(permissionGroup);
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }
}

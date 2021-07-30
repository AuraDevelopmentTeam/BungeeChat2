package dev.aura.bungeechat.hook;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsManagement;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Objects;
import java.util.Optional;

public class CloudNetPerms3Hook implements BungeeChatHook {
  private final IPermissionManagement api;

  @SuppressWarnings("deprecation")
  public CloudNetPerms3Hook() {
    IPermissionManagement api = null;
    Throwable lastException = null;

    try {
      api = CloudNetDriver.getInstance().getPermissionManagement();
    } catch (LinkageError | NullPointerException e) {
      // Fallback for CloudNet 3.2 or lower
      // api is null. We'll hit the next condition
      lastException = e;
    }

    if (api == null) {
      try {
        // This is just a fallback
        //noinspection UnstableApiUsage
        api = CloudPermissionsManagement.getInstance();
      } catch (LinkageError | NullPointerException e) {
        // Still nothing. At least keep the exception
        lastException = e;
      }

      if (api == null) {
        throw new IllegalStateException(
            "Could not initialize CloudNet 3 due to neither the new, nor the old method working",
            lastException);
      }
    }

    this.api = api;
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
    return Optional.ofNullable(api.getUser(account.getUniqueId()))
        .map(api::getHighestPermissionGroup)
        .filter(Objects::nonNull);
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }
}

package dev.aura.bungeechat.hook;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;

import java.util.Optional;

public class CloudNetPermsHook implements BungeeChatHook {
  private final CloudAPI api;

  public CloudNetPermsHook() {
    api = CloudAPI.getInstance();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return Optional.ofNullable(getPermissionGroup(account).get().getPrefix());
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return Optional.ofNullable(getPermissionGroup(account).get().getSuffix());
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
}

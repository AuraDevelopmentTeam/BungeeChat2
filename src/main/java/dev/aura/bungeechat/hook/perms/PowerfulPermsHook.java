package dev.aura.bungeechat.hook.perms;

import com.github.cheesesoftware.PowerfulPermsAPI.PermissionManager;
import com.github.cheesesoftware.PowerfulPermsAPI.PermissionPlayer;
import com.github.cheesesoftware.PowerfulPermsAPI.PowerfulPermsPlugin;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Objects;
import java.util.Optional;
import net.md_5.bungee.api.ProxyServer;

public class PowerfulPermsHook implements BungeeChatHook {
  private final PermissionManager permissionManager;

  public PowerfulPermsHook() {
    PowerfulPermsPlugin plugin =
        (PowerfulPermsPlugin)
            ProxyServer.getInstance().getPluginManager().getPlugin("PowerfulPerms");
    permissionManager = plugin.getPermissionManager();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getPlayer(account).map(PermissionPlayer::getPrefix).filter(Objects::nonNull);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getPlayer(account).map(PermissionPlayer::getSuffix).filter(Objects::nonNull);
  }

  private Optional<PermissionPlayer> getPlayer(BungeeChatAccount account) {
    return Optional.ofNullable(permissionManager.getPermissionPlayer(account.getUniqueId()));
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }
}

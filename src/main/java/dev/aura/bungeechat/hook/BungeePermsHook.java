package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.util.LoggerHelper;
import java.util.Comparator;
import java.util.Optional;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.PermissionsManager;
import net.alpenblock.bungeeperms.User;

public class BungeePermsHook implements BungeeChatHook {
  private static final Comparator<Group> groupComparator = Comparator.comparing(Group::getWeight);
  private final PermissionsManager permissionManager;

  public BungeePermsHook() {
    permissionManager = BungeePerms.getInstance().getPermissionsManager();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getUser(account).flatMap(this::getGroup).map(Group::getPrefix);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getUser(account).flatMap(this::getGroup).map(Group::getSuffix);
  }

  private Optional<Group> getGroup(User user) {
    Group group = permissionManager.getMainGroup(user);

    if (group == null) {
      return getDefaultPlayerGroup();
    } else {
      return Optional.of(group);
    }
  }

  private Optional<Group> getDefaultPlayerGroup() {
    return permissionManager.getDefaultGroups().stream().max(groupComparator);
  }

  private Optional<User> getUser(BungeeChatAccount account) {
    try {
      return Optional.ofNullable(permissionManager.getUser(account.getUniqueId()));
    } catch (NullPointerException e) {
      LoggerHelper.warning(
          "BungeePerms returned faulty data. This is a bug in BungeePerms. We recommend switching to LuckPerms or another permission manager if this error recurs.",
          e);

      return Optional.empty();
    }
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }
}

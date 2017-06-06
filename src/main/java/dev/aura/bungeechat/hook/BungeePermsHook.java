package dev.aura.bungeechat.hook;

import java.util.Optional;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.PermissionsManager;
import net.alpenblock.bungeeperms.User;

public class BungeePermsHook implements BungeeChatHook {
    private final PermissionsManager permissionManager;

    public BungeePermsHook() {
        permissionManager = BungeePerms.getInstance().getPermissionsManager();
    }

    @Override
    public Optional<String> getPrefix(BungeeChatAccount account) {
        Optional<User> user = getUser(account);

        if (!user.isPresent())
            return Optional.empty();

        return Optional.ofNullable(permissionManager.getMainGroup(user.get()).getPrefix());
    }

    @Override
    public Optional<String> getSuffix(BungeeChatAccount account) {
        Optional<User> user = getUser(account);

        if (!user.isPresent())
            return Optional.empty();

        return Optional.ofNullable(permissionManager.getMainGroup(user.get()).getSuffix());
    }

    private Optional<User> getUser(BungeeChatAccount account) {
        return Optional.ofNullable(permissionManager.getUser(account.getUniqueId()));
    }

    @Override
    public int getPriority() {
        return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
    }
}

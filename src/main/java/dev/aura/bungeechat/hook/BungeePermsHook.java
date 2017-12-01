package dev.aura.bungeechat.hook;

import java.util.Optional;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.util.LoggerHelper;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.Group;
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
        
        if (permissionManager.getMainGroup(user.get()) == null) {
        	Group defaultGroup = getDefaultPlayerGroup();
        	if (defaultGroup == null) {
        		return Optional.empty();
        	}
        	return Optional.ofNullable(defaultGroup.getPrefix());
        }
        
        return Optional.ofNullable(permissionManager.getMainGroup(user.get()).getPrefix());
    }
    
    private Group getDefaultPlayerGroup() {
    	if (permissionManager.getDefaultGroups().isEmpty()) {
    		return null;
    	}
    	
    	Group defaultGroup = permissionManager.getDefaultGroups().get(0);
    	for (int i = 1; i < permissionManager.getDefaultGroups().size(); ++i) {
    		if (permissionManager.getDefaultGroups().get(i).getWeight() < defaultGroup.getWeight()) {
    			defaultGroup = permissionManager.getDefaultGroups().get(i);
            }
    	}
    	
    	return defaultGroup;
    }

    @Override
    public Optional<String> getSuffix(BungeeChatAccount account) {
        Optional<User> user = getUser(account);

        if (!user.isPresent())
            return Optional.empty();
        
        if (permissionManager.getMainGroup(user.get()) == null) {
        	Group defaultGroup = getDefaultPlayerGroup();
        	if (defaultGroup == null) {
        		return Optional.empty();
        	}
        	return Optional.ofNullable(defaultGroup.getSuffix());
        }
        
        return Optional.ofNullable(permissionManager.getMainGroup(user.get()).getSuffix());
    }

    private Optional<User> getUser(BungeeChatAccount account) {
        try {
            return Optional.ofNullable(permissionManager.getUser(account.getUniqueId()));
        } catch (NullPointerException e) {
            LoggerHelper.warning(
                    "BungePerms returned faulty data. This is a bug in BungeePerms. We recommend switching to LuckPerms or another permission manager if this error recurrs.",
                    e);

            return Optional.empty();
        }
    }

    @Override
    public int getPriority() {
        return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
    }
}

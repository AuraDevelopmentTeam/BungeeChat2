package dev.aura.bungeechat.hook;

import java.util.Optional;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;

public class LuckPermsHook implements BungeeChatHook {
    private final LuckPermsApi api;

    public LuckPermsHook() {
        api = LuckPerms.getApi();
    }

    @Override
    public Optional<String> getPrefix(BungeeChatAccount account) {
        Optional<MetaData> metaData = getMetaData(account);

        if (!metaData.isPresent())
            return Optional.empty();

        return Optional.ofNullable(metaData.get().getPrefix());
    }

    @Override
    public Optional<String> getSuffix(BungeeChatAccount account) {
        Optional<MetaData> metaData = getMetaData(account);

        if (!metaData.isPresent())
            return Optional.empty();

        return Optional.ofNullable(metaData.get().getSuffix());
    }

    private Optional<MetaData> getMetaData(BungeeChatAccount account) {
        Optional<User> user = api.getUserSafe(account.getUniqueId());

        if (!user.isPresent())
            return Optional.empty();

        Optional<Contexts> contexts = api.getContextForUser(user.get());

        if (!contexts.isPresent())
            return Optional.empty();

        return Optional.ofNullable(user.get().getCachedData().getMetaData(contexts.get()));
    }

    @Override
    public int getPriority() {
        return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
    }
}

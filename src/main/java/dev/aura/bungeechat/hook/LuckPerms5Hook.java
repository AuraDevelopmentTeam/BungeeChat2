package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;

@RequiredArgsConstructor
public class LuckPerms5Hook implements BungeeChatHook {
  private final boolean fixContexts;
  private final LuckPerms api = LuckPermsProvider.get();

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getMetaData(account).map(CachedMetaData::getPrefix);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getMetaData(account).map(CachedMetaData::getSuffix);
  }

  private Optional<CachedMetaData> getMetaData(BungeeChatAccount account) {
    final Optional<User> user =
        Optional.ofNullable(api.getUserManager().getUser(account.getUniqueId()));

    return user.map(User::getCachedData).map(data -> data.getMetaData(getQueryOptions(user)));
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }

  private QueryOptions getQueryOptions(Optional<User> user) {
    final ContextManager contextManager = api.getContextManager();
    final QueryOptions queryOptions =
        user.flatMap(contextManager::getQueryOptions)
            .orElseGet(contextManager::getStaticQueryOptions);

    if (fixContexts && (queryOptions.mode() == QueryMode.CONTEXTUAL)) {
      final MutableContextSet context = queryOptions.context().mutableCopy();

      context
          .getValues(DefaultContextKeys.WORLD_KEY)
          .forEach(world -> context.add(DefaultContextKeys.SERVER_KEY, world));

      return queryOptions.toBuilder().context(context).build();
    } else {
      return queryOptions;
    }
  }
}

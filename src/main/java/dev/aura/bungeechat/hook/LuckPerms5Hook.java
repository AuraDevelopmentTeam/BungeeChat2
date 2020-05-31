package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

@RequiredArgsConstructor
public class LuckPerms5Hook implements BungeeChatHook {
  private final boolean fixContexts;
  private final LuckPerms api = LuckPermsProvider.get();

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getMetaData(account).map(CachedMetaData::getPrefix).filter(Objects::nonNull);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getMetaData(account).map(CachedMetaData::getSuffix).filter(Objects::nonNull);
  }

  private Optional<CachedMetaData> getMetaData(BungeeChatAccount account) {
    final Optional<User> user =
        Optional.ofNullable(api.getUserManager().getUser(account.getUniqueId()));

    return user.map(User::getCachedData)
        .map(data -> data.getMetaData(getQueryOptions(user)))
        .filter(Objects::nonNull);
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }

  private QueryOptions getQueryOptions(Optional<User> user) {
    final ContextManager contextManager = api.getContextManager();

    if (fixContexts) {
      final MutableContextSet contextSet =
          user.flatMap(contextManager::getContext)
              .orElseGet(contextManager::getStaticContext)
              .mutableCopy();

      user.flatMap(contextManager::getContext)
          .flatMap(context -> context.getAnyValue(DefaultContextKeys.WORLD_KEY))
          .ifPresent(world -> contextSet.add(DefaultContextKeys.SERVER_KEY, world));

      return QueryOptions.contextual(contextSet);
    } else {
      return user.flatMap(contextManager::getQueryOptions)
          .orElseGet(contextManager::getStaticQueryOptions);
    }
  }
}

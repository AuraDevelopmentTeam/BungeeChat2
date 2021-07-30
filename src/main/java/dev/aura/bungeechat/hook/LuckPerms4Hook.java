package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.context.ContextManager;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.api.context.MutableContextSet;

@RequiredArgsConstructor
public class LuckPerms4Hook implements BungeeChatHook {
  private final boolean fixContexts;
  private final LuckPermsApi api = LuckPerms.getApi();

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getMetaData(account).map(MetaData::getPrefix);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getMetaData(account).map(MetaData::getSuffix);
  }

  private Optional<MetaData> getMetaData(BungeeChatAccount account) {
    final Optional<User> user = api.getUserSafe(account.getUniqueId());
    final Contexts contexts =
        user.flatMap(this::getContexts).orElseGet(api.getContextManager()::getStaticContexts);

    return user.map(User::getCachedData).map(data -> data.getMetaData(contexts));
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }

  private Optional<Contexts> getContexts(User user) {
    final Optional<Contexts> contexts = api.getContextForUser(user);

    if (fixContexts) {
      final ContextManager contextManager = api.getContextManager();

      final Optional<ContextSet> userContextSet = contexts.map(Contexts::getContexts);
      final MutableContextSet contextSet =
          userContextSet.orElseGet(contextManager::getStaticContext).mutableCopy();

      userContextSet
          .flatMap(context -> context.getAnyValue(Contexts.WORLD_KEY))
          .ifPresent(world -> contextSet.add(Contexts.SERVER_KEY, world));

      return Optional.of(contextManager.formContexts(contextSet.makeImmutable()));
    } else {
      return contexts;
    }
  }
}

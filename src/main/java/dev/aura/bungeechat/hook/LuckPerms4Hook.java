package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Objects;
import java.util.Optional;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;

public class LuckPerms4Hook implements BungeeChatHook {
  private final LuckPermsApi api;

  public LuckPerms4Hook() {
    api = LuckPerms.getApi();
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return getMetaData(account).map(MetaData::getPrefix).filter(Objects::nonNull);
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return getMetaData(account).map(MetaData::getSuffix).filter(Objects::nonNull);
  }

  private Optional<MetaData> getMetaData(BungeeChatAccount account) {
    final Optional<User> user = api.getUserSafe(account.getUniqueId());
    final Optional<Contexts> contexts = user.flatMap(api::getContextForUser);

    return user.filter(xxx -> contexts.isPresent())
        .map(User::getCachedData)
        .map(data -> data.getMetaData(contexts.get()))
        .filter(Objects::nonNull);
  }

  @Override
  public int getPriority() {
    return HookManager.PERMISSION_PLUGIN_PREFIX_PRIORITY;
  }
}

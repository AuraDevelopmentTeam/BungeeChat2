package dev.aura.bungeechat.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.hook.BungeeChatHook;
import dev.aura.bungeechat.api.hook.HookManager;
import java.util.Optional;

public class DefaultHook implements BungeeChatHook {
  private final Optional<String> defaultPrefix;
  private final Optional<String> defaultSuffix;

  public DefaultHook(String defaultPrefix, String defaultSuffix) {
    this.defaultPrefix = Optional.of(defaultPrefix);
    this.defaultSuffix = Optional.of(defaultSuffix);
  }

  @Override
  public Optional<String> getPrefix(BungeeChatAccount account) {
    return defaultPrefix;
  }

  @Override
  public Optional<String> getSuffix(BungeeChatAccount account) {
    return defaultSuffix;
  }

  @Override
  public int getPriority() {
    return HookManager.DEFAULT_PREFIX_PRIORITY;
  }
}

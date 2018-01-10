package dev.aura.bungeechat.api.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FunctionFilter implements BungeeChatFilter {
  private final Function<String, String> filter;
  @Getter private final int priority;

  public FunctionFilter(Function<String, String> filter) {
    this(filter, 0);
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) {
    return filter.apply(message);
  }
}

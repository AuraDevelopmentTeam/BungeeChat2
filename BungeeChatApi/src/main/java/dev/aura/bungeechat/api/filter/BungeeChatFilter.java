package dev.aura.bungeechat.api.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;

public interface BungeeChatFilter extends Comparable<BungeeChatFilter> {
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException;

  public int getPriority();

  @Override
  default int compareTo(BungeeChatFilter other) {
    return getPriority() - other.getPriority();
  }
}

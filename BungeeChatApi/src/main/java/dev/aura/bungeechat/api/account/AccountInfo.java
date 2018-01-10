package dev.aura.bungeechat.api.account;

import lombok.Value;

@Value
public class AccountInfo {
  private final BungeeChatAccount account;
  private final boolean forceSave;
  private final boolean newAccount;
}

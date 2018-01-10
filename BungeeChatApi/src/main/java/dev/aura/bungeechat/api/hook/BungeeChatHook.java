package dev.aura.bungeechat.api.hook;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import java.util.Optional;

/** This interface is used to get prefixes and suffixes for users from Permission plugins. */
public interface BungeeChatHook extends Comparable<BungeeChatHook> {
  /**
   * Get the active prefix for the passed account.
   *
   * @param account Account to get the prefix for
   * @return Prefix of the Account, if it exists, else {@link Optional#empty}
   */
  public Optional<String> getPrefix(BungeeChatAccount account);

  /**
   * Get the active suffix for the passed account.
   *
   * @param account Account to get the suffix for
   * @return Suffix of the Account, if it exists, else {@link Optional#empty}
   */
  public Optional<String> getSuffix(BungeeChatAccount account);

  /**
   * Retrieves the priority of this hook. Higher priority means the hook is checked earlier.
   *
   * @return Numeric priority of this hook
   */
  public int getPriority();

  @Override
  default int compareTo(BungeeChatHook other) {
    return getPriority() - other.getPriority();
  }
}

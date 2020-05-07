package dev.aura.bungeechat.api.account;

import dev.aura.bungeechat.api.enums.AccountType;
import dev.aura.bungeechat.api.enums.ChannelType;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public interface BungeeChatAccount {
  public static final String unknownServer = "unknown";

  public UUID getUniqueId();

  public default AccountType getAccountType() {
    return AccountType.PLAYER;
  }

  public ChannelType getChannelType();

  /**
   * Returns the <b>global</b> default channel type for <b>all players</b>!!
   *
   * @return default channel type
   */
  public ChannelType getDefaultChannelType();

  public boolean isVanished();

  public boolean hasMessangerEnabled();

  public boolean hasSocialSpyEnabled();

  public boolean hasLocalSpyEnabled();

  public BlockingQueue<UUID> getIgnored();

  public default boolean hasIgnored(UUID uuid) {
    return getIgnored().contains(uuid);
  }

  public default boolean hasIgnored(BungeeChatAccount account) {
    return this.hasIgnored(account.getUniqueId());
  }

  public String getName();

  public default String getDisplayName() {
    return getName();
  }

  public int getPing();

  public String getServerName();

  public String getServerIP();

  public Timestamp getMutedUntil();

  public default boolean isMuted() {
    return getMutedUntil().after(new Timestamp(System.currentTimeMillis()));
  }

  public Optional<String> getStoredPrefix();

  public Optional<String> getStoredSuffix();

  public void setChannelType(ChannelType channelType);

  /**
   * Sets the <b>global</b> default channel type for <b>all players</b>!!
   *
   * @param channelType new default channel type
   */
  public void setDefaultChannelType(ChannelType channelType);

  public void setVanished(boolean vanished);

  public void setMessanger(boolean messanger);

  public void setSocialSpy(boolean socialSpy);

  public void setLocalSpy(boolean localSpy);

  public default void toggleVanished() {
    setVanished(!isVanished());
  }

  public default void toggleMessanger() {
    setMessanger(!hasMessangerEnabled());
  }

  public default void toggleSocialSpy() {
    setSocialSpy(!hasSocialSpyEnabled());
  }

  public default void toggleLocalSpy() {
    setLocalSpy(!hasLocalSpyEnabled());
  }

  public void addIgnore(UUID uuid);

  public default void addIgnore(BungeeChatAccount account) {
    this.addIgnore(account.getUniqueId());
  }

  public void removeIgnore(UUID uuid);

  public default void removeIgnore(BungeeChatAccount account) {
    this.removeIgnore(account.getUniqueId());
  }

  public void setMutedUntil(Timestamp mutedUntil);

  public default void setMutedUntil(long mutedUntilMillis) {
    setMutedUntil(new Timestamp(mutedUntilMillis));
  }

  default void mutePermanetly() {
    setMutedUntil(Timestamp.valueOf("9999-12-31 23:59:59"));
  }

  default void muteFor(long amount, TimeUnit timeUnit) {
    setMutedUntil(System.currentTimeMillis() + timeUnit.toMillis(amount));
  }

  default void unmute() {
    setMutedUntil(0L);
  }

  public void setStoredPrefix(Optional<String> newPrefix);

  public void setStoredSuffix(Optional<String> newSuffix);
}

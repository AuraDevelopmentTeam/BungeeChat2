package dev.aura.bungeechat.api.account;

import dev.aura.bungeechat.api.enums.AccountType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import lombok.Getter;

public class AccountManager {
  @Getter protected static final BungeeChatAccount consoleAccount = new ConsoleAccount();
  protected static final ConcurrentMap<UUID, BungeeChatAccount> accounts =
      new ConcurrentHashMap<>();
  @Getter private static BungeeChatAccountStorage accountStorage;

  public static void setAccountStorage(BungeeChatAccountStorage accountStorage) {
    AccountManager.accountStorage = accountStorage;

    if (accountStorage.requiresConsoleAccountSave()) {
      saveAccount(consoleAccount);
    }
  }

  public static Optional<BungeeChatAccount> getAccount(UUID uuid) {
    return Optional.ofNullable(accounts.get(uuid));
  }

  public static Optional<BungeeChatAccount> getAccount(String name) {
    List<BungeeChatAccount> accounts = getAccountsForPartialName(name);

    if (accounts.size() == 1) {
      // See if a partial match is enough to identify one player
      return Optional.of(accounts.get(0));
    } else {
      // If there are several, we need to make sure the full name or UUID matches
      return accounts.stream()
          .filter(
              account ->
                  name.equalsIgnoreCase(account.getName())
                      || name.equalsIgnoreCase(account.getUniqueId().toString()))
          .findFirst();
    }
  }

  public static List<BungeeChatAccount> getAccounts() {
    return new ArrayList<>(accounts.values());
  }

  public static List<BungeeChatAccount> getPlayerAccounts() {
    return accounts.values().stream()
        .filter(account -> account.getAccountType() == AccountType.PLAYER)
        .collect(Collectors.toList());
  }

  public static List<BungeeChatAccount> getAccountsForPartialName(String partialName) {
    final String lowercasePartialName = partialName.toLowerCase();

    return accounts.values().stream()
        .filter(
            account ->
                account.getName().toLowerCase().startsWith(lowercasePartialName)
                    || account.getUniqueId().toString().startsWith(lowercasePartialName))
        .collect(Collectors.toList());
  }

  public static void loadAccount(UUID uuid) {
    AccountInfo loadedAccount = accountStorage.load(uuid);

    accounts.put(uuid, loadedAccount.getAccount());

    if (loadedAccount.isForceSave()) {
      saveAccount(loadedAccount.getAccount());
    }
  }

  public static void unloadAccount(UUID uuid) {
    Optional<BungeeChatAccount> account = getAccount(uuid);

    if (account.isPresent()) {
      unloadAccount(account.get());
    }
  }

  public static void unloadAccount(BungeeChatAccount account) {
    saveAccount(account);

    accounts.remove(account.getUniqueId());
  }

  public static void saveAccount(BungeeChatAccount account) {
    accountStorage.save(account);
  }

  static {
    accounts.put(consoleAccount.getUniqueId(), consoleAccount);
  }
}

package dev.aura.bungeechat.account;

import dev.aura.bungeechat.api.account.AccountInfo;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.event.BungeeChatLeaveEvent;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeecordAccountManager extends AccountManager implements Listener {
  private static final ConcurrentMap<UUID, CommandSender> nativeObjects = new ConcurrentHashMap<>();
  private static final List<UUID> newPlayers = new LinkedList<>();

  public static Optional<BungeeChatAccount> getAccount(CommandSender player) {
    if (player instanceof ProxiedPlayer) return getAccount(((ProxiedPlayer) player).getUniqueId());
    else if (player == null) return Optional.empty();
    else return Optional.of(consoleAccount);
  }

  public static Optional<CommandSender> getCommandSender(UUID uuid) {
    return Optional.ofNullable(nativeObjects.get(uuid));
  }

  public static Optional<CommandSender> getCommandSender(BungeeChatAccount account) {
    return getCommandSender(account.getUniqueId());
  }

  public static List<BungeeChatAccount> getAccountsForPartialName(
      String partialName, CommandSender player) {
    List<BungeeChatAccount> accounts = getAccountsForPartialName(partialName);

    if (!PermissionManager.hasPermission(player, Permission.COMMAND_VANISH_VIEW)) {
      accounts =
          accounts.stream().filter(account -> !account.isVanished()).collect(Collectors.toList());
    }

    return accounts;
  }

  public static List<BungeeChatAccount> getAccountsForPartialName(
      String partialName, BungeeChatAccount account) {
    return getAccountsForPartialName(partialName, getCommandSenderFromAccount(account));
  }

  public static void loadAccount(UUID uuid) {
    AccountInfo loadedAccount = getAccountStorage().load(uuid);

    accounts.put(uuid, loadedAccount.getAccount());
    nativeObjects.put(uuid, getCommandSenderFromAccount(loadedAccount.getAccount()));

    if (loadedAccount.isForceSave()) {
      saveAccount(loadedAccount.getAccount());
    }

    if (loadedAccount.isNewAccount()) {
      newPlayers.add(loadedAccount.getAccount().getUniqueId());
    }
  }

  public static void unloadAccount(UUID uuid) {
    Optional<BungeeChatAccount> account = getAccount(uuid);

    account.ifPresent(
        acc -> {
          unloadAccount(acc);
          newPlayers.remove(acc.getUniqueId());
        });
  }

  public static void unloadAccount(BungeeChatAccount account) {
    AccountManager.unloadAccount(account);
    nativeObjects.remove(account.getUniqueId());
  }

  public static boolean isNew(UUID uuid) {
    return newPlayers.contains(uuid);
  }

  private static CommandSender getCommandSenderFromAccount(BungeeChatAccount account) {
    ProxyServer instance = ProxyServer.getInstance();

    if (instance == null) return new DummyConsole();

    switch (account.getAccountType()) {
      case PLAYER:
        return instance.getPlayer(account.getUniqueId());
      case CONSOLE:
      default:
        return instance.getConsole();
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerConnect(BungeeChatJoinEvent event) {
    loadAccount(event.getPlayer().getUniqueId());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDisconnect(BungeeChatLeaveEvent event) {
    unloadAccount(event.getPlayer().getUniqueId());
  }

  static {
    nativeObjects.put(consoleAccount.getUniqueId(), getCommandSenderFromAccount(consoleAccount));
  }

  private static class DummyConsole implements CommandSender {
    @Override
    public String getName() {
      return null;
    }

    @Override
    @Deprecated
    public void sendMessage(String message) {
      // Do nothing
    }

    @Override
    @Deprecated
    public void sendMessages(String... messages) {
      // Do nothing
    }

    @Override
    public void sendMessage(BaseComponent... message) {
      // Do nothing
    }

    @Override
    public void sendMessage(BaseComponent message) {
      // Do nothing
    }

    @Override
    public Collection<String> getGroups() {
      return null;
    }

    @Override
    public void addGroups(String... groups) {
      // Do nothing
    }

    @Override
    public void removeGroups(String... groups) {
      // Do nothing
    }

    @Override
    public boolean hasPermission(String permission) {
      return true;
    }

    @Override
    public void setPermission(String permission, boolean value) {
      // Do nothing
    }

    @Override
    public Collection<String> getPermissions() {
      return Collections.singletonList("*");
    }
  }
}

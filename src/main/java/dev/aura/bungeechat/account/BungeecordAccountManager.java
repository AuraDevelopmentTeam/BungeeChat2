package dev.aura.bungeechat.account;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@SuppressWarnings("unused")
public class BungeecordAccountManager extends AccountManager implements Listener {
    protected static ConcurrentMap<UUID, CommandSender> nativeObjects = new ConcurrentHashMap<>();

    public static Optional<BungeeChatAccount> getAccount(CommandSender player) {
        if (player instanceof ProxiedPlayer)
            return getAccount(((ProxiedPlayer) player).getUniqueId());
        else if (player instanceof CommandSender)
            return Optional.of(consoleAccount);
        else
            return Optional.empty();
    }

    public static Optional<CommandSender> getCommandSender(UUID uuid) {
        return Optional.ofNullable(nativeObjects.get(uuid));
    }

    public static Optional<CommandSender> getCommandSender(BungeeChatAccount account) {
        return getCommandSender(account.getUniqueId());
    }

    public static void loadAccount(UUID uuid) {
        Entry<BungeeChatAccount, Boolean> loadedAccount = accountStorage.load(uuid);

        accounts.put(uuid, loadedAccount.getKey());
        nativeObjects.put(uuid, getCommandSenderFromAccount(loadedAccount.getKey()));

        if (loadedAccount.getValue()) {
            saveAccount(loadedAccount.getKey());
        }
    }

    public static void unloadAccount(UUID uuid) {
        Optional<BungeeChatAccount> account = getAccount(uuid);

        account.ifPresent(BungeecordAccountManager::unloadAccount);
    }

    public static void unloadAccount(BungeeChatAccount account) {
        AccountManager.unloadAccount(account);
        nativeObjects.remove(account.getUniqueId());
    }

    private static CommandSender getCommandSenderFromAccount(BungeeChatAccount account) {
        switch (account.getAccountType()) {
        case PLAYER:
            return ProxyServer.getInstance().getPlayer(account.getUniqueId());
        case CONSOLE:
        default:
            return ProxyServer.getInstance().getConsole();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerConnect(PostLoginEvent event) {
        loadAccount(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        unloadAccount(event.getPlayer().getUniqueId());
    }

    static {
        nativeObjects.put(consoleAccount.getUniqueId(), getCommandSenderFromAccount(consoleAccount));
    }
}

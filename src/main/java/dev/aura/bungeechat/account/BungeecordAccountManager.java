package dev.aura.bungeechat.account;

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
        BungeeChatAccount account = accountStorage.load(uuid);

        accounts.put(uuid, account);
        nativeObjects.put(uuid, getCommandSenderFromAccount(account));
    }

    public static void unloadAccount(UUID uuid) {
        Optional<BungeeChatAccount> account = getAccount(uuid);

        if (account.isPresent()) {
            unloadAccount(account.get());
        }
    }

    public static void unloadAccount(BungeeChatAccount account) {
        accountStorage.save(account);

        accounts.remove(account.getUniqueId());
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

    @EventHandler
    public void onPlayerConnect(PostLoginEvent event) {
        loadAccount(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        unloadAccount(event.getPlayer().getUniqueId());
    }

    static {
        nativeObjects.put(consoleAccount.getUniqueId(), getCommandSenderFromAccount(consoleAccount));
    }
}

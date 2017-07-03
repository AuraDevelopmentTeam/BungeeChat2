package dev.aura.bungeechat.account;

import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

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
        ProxyServer instance = ProxyServer.getInstance();

        if (instance == null)
            return new DummyConsole();

        switch (account.getAccountType()) {
        case PLAYER:
            return instance.getPlayer(account.getUniqueId());
        case CONSOLE:
        default:
            return instance.getConsole();
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
            return Arrays.asList("*");
        }
    }
}

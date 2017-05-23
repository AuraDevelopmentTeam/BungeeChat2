package dev.aura.bungeechat;

import dev.aura.bungeechat.api.BungeeChatApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class BungeeChat extends Plugin implements BungeeChatApi {

    private static CopyOnWriteArrayList<Account> userAccounts = new CopyOnWriteArrayList<>();

    public static Account getUserAccount(UUID uuid) {
        for (Account acc : userAccounts) {
            if (acc.getUniqueId().equals(uuid)) return acc;
        }
        return null;
    }
    public static Account getUserAccount(ProxiedPlayer player) {
        for (Account acc : userAccounts) {
            if (acc.getUniqueId().equals(player.getUniqueId())) return acc;
        }
        return null;
    }

    public static void registerAccount(Account account) { userAccounts.add(account); }
    public static void unregisterAccount(Account account) { userAccounts.remove(account); }

}

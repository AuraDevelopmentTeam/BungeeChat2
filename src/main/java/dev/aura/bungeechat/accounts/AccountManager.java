package dev.aura.bungeechat.accounts;

import dev.aura.bungeechat.api.enums.ChannelType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountManager {

    private static CopyOnWriteArrayList<Account> userAccounts = new CopyOnWriteArrayList<>();

    public static Account getUserAccount(UUID uuid) {
        for (Account acc : userAccounts) {
            if (acc.getUniqueId().equals(uuid)) return acc;
        }
        return null;
    }
    public static Account getUserAccount(ProxiedPlayer player) {
        return getUserAccount(player.getUniqueId());
    }

    public static void registerAccount(Account account) { userAccounts.add(account); }
    public static void unregisterAccount(Account account) { userAccounts.remove(account); }

    public static void saveAccount(Account account) throws IOException {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata");
        if (!folder.exists()){
            folder.mkdir();
        }
        File checker = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + account.getUniqueId().toString() + ".sav");
        if (!checker.exists()) {
            checker.createNewFile();
        }
        FileOutputStream saveFile= new FileOutputStream(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + account.getUniqueId().toString() + ".sav");
        ObjectOutputStream save = new ObjectOutputStream(saveFile);
        save.writeObject(account.getChannelType());
        save.writeObject(account.isMessanger());
        save.writeObject(account.isVanished());
        save.writeObject(account.isSocialspy());
        save.writeObject(account.getIgnored());
        save.close();
        unregisterAccount(account);
    }

    public static void loadAccount(UUID uuid) throws IOException, ClassNotFoundException {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata");
        if (!folder.exists()){
            registerAccount(new Account(uuid));
            return;
        }
        File checker = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + uuid.toString() + ".sav");
        if (!checker.exists()) {
            registerAccount(new Account(uuid));
            return;
        }
        FileInputStream saveFile = new FileInputStream(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + uuid + ".sav");
        ObjectInputStream save = new ObjectInputStream(saveFile);
        Account account = new Account(uuid, (ChannelType) save.readObject(), (boolean) save.readObject(), (boolean) save.readObject(), (boolean) save.readObject(),
                (CopyOnWriteArrayList<UUID>) save.readObject());
        save.close();
        registerAccount(account);
    }

}

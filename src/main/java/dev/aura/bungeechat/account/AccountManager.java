package dev.aura.bungeechat.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AccountManager implements Listener {

    private static CopyOnWriteArrayList<BungeeChatAccount> userAccounts = new CopyOnWriteArrayList<>();

    public static BungeeChatAccount getUserAccount(UUID uuid) {
        for (BungeeChatAccount acc : userAccounts) {
            if (acc.getUniqueId().equals(uuid))
                return acc;
        }
        return null;
    }

    public static BungeeChatAccount getUserAccount(ProxiedPlayer player) {
        return getUserAccount(player.getUniqueId());
    }

    public static void registerAccount(BungeeChatAccount account) {
        userAccounts.add(account);
    }

    public static void unregisterAccount(BungeeChatAccount account) {
        userAccounts.remove(account);
    }

    public static void saveAccount(BungeeChatAccount account) throws IOException {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File checker = new File(folder, account.getUniqueId().toString() + ".sav");
        if (!checker.exists()) {
            checker.createNewFile();
        }
        FileOutputStream saveFile = new FileOutputStream(checker);
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
        if (!folder.exists()) {
            registerAccount(new Account(uuid));
            return;
        }
        File checker = new File(folder, uuid.toString() + ".sav");
        if (!checker.exists()) {
            registerAccount(new Account(uuid));
            return;
        }
        FileInputStream saveFile = new FileInputStream(checker);
        ObjectInputStream save = new ObjectInputStream(saveFile);
        @SuppressWarnings("unchecked")
        Account account = new Account(uuid, (ChannelType) save.readObject(), (boolean) save.readObject(),
                (boolean) save.readObject(), (boolean) save.readObject(),
                (CopyOnWriteArrayList<UUID>) save.readObject());
        save.close();
        registerAccount(account);
    }

    @EventHandler
    public void onPlayerConnect(PostLoginEvent event) throws IOException, ClassNotFoundException {
        loadAccount(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) throws IOException, ClassNotFoundException {
        saveAccount(AccountManager.getUserAccount(event.getPlayer().getUniqueId()));
    }

}

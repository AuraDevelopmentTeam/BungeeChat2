package dev.aura.bungeechat.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.utils.UUIDUtils;
import lombok.Cleanup;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AccountManager implements Listener {
    private static final String FILE_EXTENSION = ".sav";
    
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
        File accountFile = new File(getUserDataDir(), account.getUniqueId() + FILE_EXTENSION);

        if (!accountFile.exists()) {
            accountFile.createNewFile();
        }

        @Cleanup
        FileOutputStream saveFile = new FileOutputStream(accountFile);
        @Cleanup
        ObjectOutputStream save = new ObjectOutputStream(saveFile);
        
        save.writeObject(account.getChannelType());
        save.writeObject(account.isMessanger());
        save.writeObject(account.isVanished());
        save.writeObject(account.isSocialspy());
        save.writeObject(account.getIgnored());
        save.close();
        
        unregisterAccount(account);
    }

    private static void getAccountSQL(UUID uuid) throws SQLException {
        if (AccountSQL.getConnection().isClosed()) {
            AccountSQL.openConnection();
        }

        Statement statement = AccountSQL.getConnection().createStatement();
        String sql = "SELECT * FROM BungeeChatAccounts WHERE uuid='" + uuid + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        Account acc = null;
        if (!resultSet.next()) {
            acc = new Account(uuid);
        } else {
            while (resultSet.next()) {
                ChannelType channelType = ChannelType.valueOf(resultSet.getString("channeltype").toUpperCase());
                boolean vanished = resultSet.getBoolean("vanished");
                boolean messenger = resultSet.getBoolean("messenger");
                boolean socialspy = resultSet.getBoolean("socialspy");
                String ignored = resultSet.getString("ignored");
                CopyOnWriteArrayList<UUID> uuidList = UUIDUtils
                        .convertStringCopyOnWriteArrayList(Arrays.asList(ignored.split("\\s*,\\s*")));
                acc = new Account(uuid, channelType, vanished, messenger, socialspy, uuidList);
            }
        }
        if (acc != null) {
            registerAccount(acc);
        }
    }

    public static void loadAccount(UUID uuid) throws IOException, ClassNotFoundException {
        File checker = new File(getUserDataDir(), uuid.toString() + FILE_EXTENSION);

        if (!checker.exists()) {
            registerAccount(new Account(uuid));
            
            return;
        }

        @Cleanup
        FileInputStream saveFile = new FileInputStream(checker);
        @Cleanup
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

    private static File getUserDataDir() {
        File folder = new File(BungeeChat.getInstance().getConfigFolder(), "userdata");
        folder.mkdirs();
        
        return folder;
    }
}

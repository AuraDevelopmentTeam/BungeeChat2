package dev.aura.bungeechat.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.account.BungeeChatAccountStorage;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.util.LoggerHelper;
import lombok.Cleanup;

public class AccountFileStorage implements BungeeChatAccountStorage {
    private static final String FILE_EXTENSION = ".sav";

    private static File getUserDataDir() {
        File folder = new File(BungeeChat.getInstance().getConfigFolder(), "userdata");
        folder.mkdirs();

        return folder;
    }

    @Override
    public void save(BungeeChatAccount account) {
        try {
            File accountFile = new File(getUserDataDir(), account.getUniqueId() + FILE_EXTENSION);

            if (!accountFile.exists()) {
                accountFile.createNewFile();
            }

            @Cleanup
            FileOutputStream saveFile = new FileOutputStream(accountFile);
            @Cleanup
            ObjectOutputStream save = new ObjectOutputStream(saveFile);

            save.writeObject(account.getName());
            save.writeObject(account.getChannelType());
            save.writeObject(account.hasMessangerEnabled());
            save.writeObject(account.isVanished());
            save.writeObject(account.hasSocialSpyEnabled());
            save.writeObject(account.hasLocalSpyEnabled());
            save.writeObject(account.getIgnored());
            save.writeObject(account.getMutedUntil());
            save.writeObject(account.getStoredPrefix().orElse(null));
            save.writeObject(account.getStoredSuffix().orElse(null));
        } catch (IOException e) {
            LoggerHelper.warning("Could not save player " + account.getUniqueId(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entry<BungeeChatAccount, Boolean> load(UUID uuid) {
        try {
            File accountFile = new File(getUserDataDir(), uuid.toString() + FILE_EXTENSION);

            if (!accountFile.exists())
                return new SimpleEntry<>(new Account(uuid), false);

            @Cleanup
            FileInputStream saveFile = new FileInputStream(accountFile);
            @Cleanup
            ObjectInputStream save = new ObjectInputStream(saveFile);

            // Read Name (and discard it (for now))
            save.readObject();

            return new SimpleEntry<>(new Account(uuid, (ChannelType) save.readObject(), (boolean) save.readObject(),
                    (boolean) save.readObject(), (boolean) save.readObject(), (boolean) save.readObject(),
                    (BlockingQueue<UUID>) save.readObject(), (Timestamp) save.readObject(),
                    Optional.ofNullable((String) save.readObject()), Optional.ofNullable((String) save.readObject())),
                    false);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            LoggerHelper.warning("Could not load player " + uuid, e);

            return new SimpleEntry<>(new Account(uuid), false);
        }
    }
}

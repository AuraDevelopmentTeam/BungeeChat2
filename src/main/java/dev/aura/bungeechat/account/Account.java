package dev.aura.bungeechat.account;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.SkinConfiguration;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

@Data
@EqualsAndHashCode(of = "uuid")
public class Account implements BungeeChatAccount {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UUID uuid;
    @Getter(lazy = true)
    private final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) BungeecordAccountManager.getCommandSender(this)
            .orElse(new DummyPlayer());
    private ChannelType channelType;
    private boolean vanished;
    @Getter(AccessLevel.NONE)
    private boolean messanger;
    @Getter(AccessLevel.NONE)
    private boolean socialSpy;
    @Getter(AccessLevel.NONE)
    private boolean localSpy;
    private final BlockingQueue<UUID> ignored;
    private Timestamp mutedUntil;
    private Optional<String> storedPrefix;
    private Optional<String> storedSuffix;

    protected Account(ProxiedPlayer player) {
        this(player.getUniqueId());
    }

    protected Account(UUID uuid) {
        this.uuid = uuid;
        channelType = ChannelType.LOCAL;
        vanished = false;
        messanger = true;
        socialSpy = false;
        localSpy = false;
        ignored = new LinkedBlockingQueue<>();
        mutedUntil = new Timestamp(0);
        storedPrefix = Optional.empty();
        storedSuffix = Optional.empty();
    }

    protected Account(UUID uuid, ChannelType channelType, boolean vanished, boolean messanger, boolean socialSpy,
            boolean localSpy, BlockingQueue<UUID> ignored, Timestamp mutedUntil, Optional<String> storedPrefix,
            Optional<String> storedSuffix) {
        this.uuid = uuid;
        this.channelType = channelType;
        this.vanished = vanished;
        this.messanger = messanger;
        this.socialSpy = socialSpy;
        this.localSpy = localSpy;
        this.ignored = ignored;
        this.mutedUntil = mutedUntil;
        this.storedPrefix = storedPrefix;
        this.storedSuffix = storedSuffix;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean hasMessangerEnabled() {
        return messanger;
    }

    @Override
    public boolean hasSocialSpyEnabled() {
        return socialSpy;
    }

    @Override
    public boolean hasLocalSpyEnabled() {
        return localSpy;
    }

    public boolean hasIgnored(ProxiedPlayer player) {
        return hasIgnored(player.getUniqueId());
    }

    @Override
    public void addIgnore(UUID uuid) {
        ignored.add(uuid);
    }

    public void addIgnore(ProxiedPlayer player) {
        this.addIgnore(player.getUniqueId());
    }

    @Override
    public void removeIgnore(UUID uuid) {
        ignored.remove(uuid);
    }

    public void removeIgnore(ProxiedPlayer player) {
        this.removeIgnore(player.getUniqueId());
    }

    @Override
    public String getName() {
        return getProxiedPlayer().getName();
    }

    @Override
    public String getDisplayName() {
        return getProxiedPlayer().getDisplayName();
    }

    @Override
    public int getPing() {
        return getProxiedPlayer().getPing();
    }

    @Override
    public String getServerName() {
        try {
            return getServerInfo().getName();
        } catch (NullPointerException e) {
            return "unknown";
        }
    }

    @Override
    public String getServerIP() {
        try {
            return getServerInfo().getAddress().toString();
        } catch (NullPointerException e) {
            return "unknown";
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    private ServerInfo getServerInfo() {
        ProxiedPlayer player = getProxiedPlayer();
        Server server = player.getServer();

        if (server == null)
            return player.getReconnectServer();
        else
            return server.getInfo();
    }

    private class DummyPlayer implements ProxiedPlayer {
        @Override
        public InetSocketAddress getAddress() {
            return null;
        }

        @Override
        @Deprecated
        public void disconnect(String reason) {
            // Nothing
        }

        @Override
        public void disconnect(BaseComponent... reason) {
            // Nothing
        }

        @Override
        public void disconnect(BaseComponent reason) {
            // Nothing
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public Unsafe unsafe() {
            return null;
        }

        @Override
        public String getName() {
            return "Dummy";
        }

        @Override
        @Deprecated
        public void sendMessage(String message) {
            // Nothing
        }

        @Override
        @Deprecated
        public void sendMessages(String... messages) {
            // Nothing
        }

        @Override
        public void sendMessage(BaseComponent... message) {
            // Nothing
        }

        @Override
        public void sendMessage(BaseComponent message) {
            // Nothing
        }

        @Override
        public Collection<String> getGroups() {
            return new LinkedList<>();
        }

        @Override
        public void addGroups(String... groups) {
            // Nothing
        }

        @Override
        public void removeGroups(String... groups) {
            // Nothing
        }

        @Override
        public boolean hasPermission(String permission) {
            return false;
        }

        @Override
        public void setPermission(String permission, boolean value) {
            // Nothing
        }

        @Override
        public Collection<String> getPermissions() {
            return new LinkedList<>();
        }

        @Override
        public String getDisplayName() {
            return "";
        }

        @Override
        public void setDisplayName(String name) {
            // Nothing
        }

        @Override
        public void sendMessage(ChatMessageType position, BaseComponent... message) {
            // Nothing
        }

        @Override
        public void sendMessage(ChatMessageType position, BaseComponent message) {
            // Nothing
        }

        @Override
        public void connect(ServerInfo target) {
            // Nothing
        }

        @Override
        public void connect(ServerInfo target, Callback<Boolean> callback) {
            // Nothing
        }

        @Override
        public Server getServer() {
            return null;
        }

        @Override
        public int getPing() {
            return 0;
        }

        @Override
        public void sendData(String channel, byte[] data) {
            // Nothing
        }

        @Override
        public PendingConnection getPendingConnection() {
            return null;
        }

        @Override
        public void chat(String message) {
            // Nothing
        }

        @Override
        public ServerInfo getReconnectServer() {
            return null;
        }

        @Override
        public void setReconnectServer(ServerInfo server) {
            // Nothing
        }

        @Override
        @Deprecated
        public String getUUID() {
            return getUniqueId().toString();
        }

        @Override
        public UUID getUniqueId() {
            return uuid;
        }

        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }

        @Override
        public byte getViewDistance() {
            return 0;
        }

        @Override
        public ChatMode getChatMode() {
            return ChatMode.HIDDEN;
        }

        @Override
        public boolean hasChatColors() {
            return false;
        }

        @Override
        public SkinConfiguration getSkinParts() {
            return null;
        }

        @Override
        public MainHand getMainHand() {
            return MainHand.RIGHT;
        }

        @Override
        public void setTabHeader(BaseComponent header, BaseComponent footer) {
            // Nothing
        }

        @Override
        public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
            // Nothing
        }

        @Override
        public void resetTabHeader() {
            // Nothing
        }

        @Override
        public void sendTitle(Title title) {
            // Nothing
        }

        @Override
        public boolean isForgeUser() {
            return false;
        }

        @Override
        public Map<String, String> getModList() {
            return new HashMap<>();
        }
    }
}

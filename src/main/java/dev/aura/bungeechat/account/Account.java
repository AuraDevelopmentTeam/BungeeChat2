package dev.aura.bungeechat.account;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.config.Config;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

@Data
public class Account implements BungeeChatAccount {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UUID uuid;
    @Getter(lazy = true)
    private final ProxiedPlayer proxiedPlayer = toProxiedPlayer(this);
    private ChannelType channelType;
    private boolean vanished;
    private boolean messanger;
    private boolean socialspy;
    private final CopyOnWriteArrayList<UUID> ignored;
    private String storedPrefix, storedSuffix;

    public static ProxiedPlayer toProxiedPlayer(BungeeChatAccount account) {
        return ProxyServer.getInstance().getPlayer(account.getUniqueId());
    }

    public Account(ProxiedPlayer player) {
        this(player.getUniqueId());
    }

    public Account(UUID uuid) {
        this.uuid = uuid;
        channelType = ChannelType.NONE;
        vanished = false;
        messanger = true;
        socialspy = false;
        ignored = new CopyOnWriteArrayList<>();
        storedPrefix = Config.get().getString("Settings.PermissionsManager.Default-Prefix");
        storedSuffix = Config.get().getString("Settings.PermissionsManager.Default-Suffix");
    }

    public Account(ProxiedPlayer player, ChannelType channelType, boolean vanished, boolean messanger,
            boolean socialspy, CopyOnWriteArrayList<UUID> ignored, String storedPrefix, String storedSuffix) {
        this(player.getUniqueId(), channelType, vanished, messanger, socialspy, ignored, storedPrefix, storedSuffix);
    }

    public Account(UUID uuid, ChannelType channelType, boolean vanished, boolean messanger, boolean socialspy,
            CopyOnWriteArrayList<UUID> ignored, String storedPrefix, String storedSuffix) {
        this.uuid = uuid;
        this.channelType = channelType;
        this.vanished = vanished;
        this.messanger = messanger;
        this.socialspy = socialspy;
        this.ignored = ignored;
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
        return socialspy;
    }

    @Override
    public boolean hasIgnored(UUID uuid) {
        return ignored.contains(uuid);
    }

    @Override
    public String getStoredPrefix() {
        return storedPrefix;
    }

    @Override
    public String getStoredSuffix() {
        return storedSuffix;
    }

    public boolean hasIgnored(ProxiedPlayer player) {
        return this.hasIgnored(player.getUniqueId());
    }

    @Override
    public void toggleVanished() {
        vanished = !vanished;
    }

    @Override
    public void toggleMessanger() {
        messanger = !messanger;
    }

    @Override
    public void toggleSocialSpy() {
        socialspy = !socialspy;
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

    @Override
    public void setStoredPrefix(String newPrefix) {
        storedPrefix = newPrefix;
    }

    @Override
    public void setStoredSuffix(String newSuffix) {
        storedSuffix = newSuffix;
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

    private ServerInfo getServerInfo() {
        ProxiedPlayer player = getProxiedPlayer();
        Server server = player.getServer();

        if (server == null)
            return player.getReconnectServer();
        else
            return server.getInfo();
    }
}

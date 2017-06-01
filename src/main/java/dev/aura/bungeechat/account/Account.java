package dev.aura.bungeechat.account;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
public class Account implements BungeeChatAccount {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UUID uuid;
    @Getter(lazy = true)
    private final ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
    private ChannelType channelType;
    private boolean vanished;
    private boolean messanger;
    private boolean socialspy;
    private final CopyOnWriteArrayList<UUID> ignored;

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
    }

    public Account(ProxiedPlayer player, ChannelType channelType, boolean vanished, boolean messanger,
            boolean socialspy, CopyOnWriteArrayList<UUID> ignored) {
        this(player.getUniqueId(), channelType, vanished, messanger, socialspy, ignored);
    }

    public Account(UUID uuid, ChannelType channelType, boolean vanished, boolean messanger, boolean socialspy,
            CopyOnWriteArrayList<UUID> ignored) {
        this.uuid = uuid;
        this.channelType = channelType;
        this.vanished = vanished;
        this.messanger = messanger;
        this.socialspy = socialspy;
        this.ignored = ignored;
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
        return getProxiedPlayer().getServer().getInfo().getName();
    }

    @Override
    public String getServerIP() {
        return getProxiedPlayer().getServer().getInfo().getAddress().toString();
    }
}

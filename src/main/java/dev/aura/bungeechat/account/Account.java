package dev.aura.bungeechat.account;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Account implements BungeeChatAccount {
    private UUID uuid;
    @Getter
    @Setter
    private ProxiedPlayer proxiedPlayer;
    @Getter
    @Setter
    private ChannelType channelType;
    @Getter
    @Setter
    private boolean vanished;
    @Getter
    @Setter
    private boolean messanger;
    @Getter
    @Setter
    private boolean socialspy;
    @Getter
    private CopyOnWriteArrayList<UUID> ignored;

    public static ProxiedPlayer toProxiedPlayer(BungeeChatAccount account) {
        return ProxyServer.getInstance().getPlayer(account.getUniqueId());
    }

    public Account(ProxiedPlayer player) {
        this(player.getUniqueId());
    }

    public Account(UUID uuid) {
        this.uuid = uuid;
        proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
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
        proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
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
        return proxiedPlayer.getName();
    }

    @Override
    public String getDisplayName() {
        return proxiedPlayer.getDisplayName();
    }

    @Override
    public int getPing() {
        return proxiedPlayer.getPing();
    }

    @Override
    public String getServerName() {
        return proxiedPlayer.getServer().getInfo().getName();
    }

    @Override
    public String getServerIP() {
        return proxiedPlayer.getServer().getInfo().getAddress().toString();
    }
}

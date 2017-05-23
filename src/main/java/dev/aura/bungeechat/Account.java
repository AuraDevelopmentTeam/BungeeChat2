package dev.aura.bungeechat;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public class Account {

    private UUID uuid;
    private ChannelType channelType;
    private boolean vanished, messanger, socialspy;
    private CopyOnWriteArrayList<UUID> ignored;

    public Account(UUID uuid) {
        this.uuid = uuid;
        this.channelType = ChannelType.NONE;
        this.vanished = false;
        this.messanger = true;
        this.socialspy = false;
        this.ignored = new CopyOnWriteArrayList<>();
    }

    public UUID getUniqueId() { return this.uuid; }
    public ChannelType getChannelType() { return this.channelType; }
    public boolean isVanished() { return this.vanished; }
    public boolean hasMessangerEnabled() { return this.messanger; }
    public boolean hasSocialSpyEnabled() { return this.socialspy; }
    public CopyOnWriteArrayList<UUID> getIgnored() { return this.ignored; }
    public boolean hasIgnored(UUID uuid) { return this.ignored.contains(uuid); }
    public boolean hasIgnored(ProxiedPlayer player) { return this.ignored.contains(player.getUniqueId()); }

    public void setChannelType(ChannelType channelType) { this.channelType = channelType; }
    public void toggleVanished() { this.vanished = !this.vanished; }
    public void toggleMessanger() { this.messanger = !this.messanger; }
    public void toggleSocialSpy() { this.socialspy = !this.socialspy; }
    public void addIgnore(UUID uuid) { this.ignored.add(uuid); }
    public void addIgnore(ProxiedPlayer player) { this.ignored.add(player.getUniqueId()); }
    public void removeIgnore(UUID uuid) { this.ignored.remove(uuid); }
    public void removeIgnore(ProxiedPlayer player) { this.ignored.remove(player.getUniqueId()); }



}

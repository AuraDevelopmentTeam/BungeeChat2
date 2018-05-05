package dev.aura.bungeechat.util;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.SkinConfiguration;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.util.*;

@RequiredArgsConstructor
public class DummyPlayer implements ProxiedPlayer {
  private final UUID uuid;

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
  public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {

  }

  @Override
  public void connect(ServerInfo target, Callback<Boolean> callback) {
    // Nothing
  }

  @Override
  public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {

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

  @Override
  public Scoreboard getScoreboard() {
    return new Scoreboard();
  }
}

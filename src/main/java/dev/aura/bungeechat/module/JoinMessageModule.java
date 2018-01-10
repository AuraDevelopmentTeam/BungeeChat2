package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.JoinMessageListener;
import net.md_5.bungee.api.ProxyServer;

public class JoinMessageModule extends Module {
  private JoinMessageListener joinMessageListener;

  @Override
  public String getName() {
    return "JoinMessage";
  }

  @Override
  public void onEnable() {
    joinMessageListener = new JoinMessageListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), joinMessageListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterListener(joinMessageListener);
  }
}

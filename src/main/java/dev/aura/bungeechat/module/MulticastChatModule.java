package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.MulticastChatListener;
import net.md_5.bungee.api.ProxyServer;

public class MulticastChatModule extends Module {
  private MulticastChatListener multicastListener;

  @Override
  public String getName() {
    return "MulticastChat";
  }

  @Override
  public void onEnable() {
    multicastListener = new MulticastChatListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), multicastListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterListener(multicastListener);
  }
}

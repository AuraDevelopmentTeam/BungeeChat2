package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.WelcomeMessageListener;
import net.md_5.bungee.api.ProxyServer;

public class WelcomeMessageModule extends Module {
  private WelcomeMessageListener welcomeMessageListener;

  @Override
  public String getName() {
    return "WelcomeMessage";
  }

  @Override
  public void onEnable() {
    welcomeMessageListener = new WelcomeMessageListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), welcomeMessageListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterListener(welcomeMessageListener);
  }
}

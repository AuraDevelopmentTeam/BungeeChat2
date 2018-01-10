package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.TabCompletionListener;
import net.md_5.bungee.api.ProxyServer;

public class TabCompletionModule extends Module {
  private TabCompletionListener tabCompletionListener;

  @Override
  public String getName() {
    return "TabCompletion";
  }

  @Override
  public void onEnable() {
    tabCompletionListener = new TabCompletionListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), tabCompletionListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterListener(tabCompletionListener);
  }
}

package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.LeaveMessageListener;
import net.md_5.bungee.api.ProxyServer;

public class LeaveMessageModule extends Module {
  private LeaveMessageListener leaveMessageListener;

  @Override
  public String getName() {
    return "LeaveMessage";
  }

  @Override
  public void onEnable() {
    leaveMessageListener = new LeaveMessageListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), leaveMessageListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterListener(leaveMessageListener);
  }
}

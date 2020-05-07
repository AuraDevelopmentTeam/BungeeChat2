package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.LocalChatCommand;
import dev.aura.bungeechat.listener.LocalChatListener;
import net.md_5.bungee.api.ProxyServer;

public class LocalChatModule extends Module {
  private LocalChatCommand localChatCommand;
  private LocalChatListener localChatListener;

  @Override
  public String getName() {
    return "LocalChat";
  }

  @Override
  public void onEnable() {
    localChatCommand = new LocalChatCommand(this);
    localChatListener = new LocalChatListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(BungeeChat.getInstance(), localChatCommand);
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), localChatListener);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterCommand(localChatCommand);
    ProxyServer.getInstance().getPluginManager().unregisterListener(localChatListener);
  }
}

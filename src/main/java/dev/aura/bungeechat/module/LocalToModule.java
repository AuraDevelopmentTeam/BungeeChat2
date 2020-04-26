package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.LocalToCommand;
import net.md_5.bungee.api.ProxyServer;

public class LocalToModule extends Module {

  private LocalToCommand localToCommand;

  @Override
  public String getName() {
    return "LocalTo";
  }

  @Override
  public void onEnable() {
    localToCommand = new LocalToCommand(this);

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(BungeeChat.getInstance(), localToCommand);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterCommand(localToCommand);
  }
}

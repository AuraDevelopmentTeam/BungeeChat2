package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.VanishCommand;
import net.md_5.bungee.api.ProxyServer;

public class VanishModule extends Module {
  private VanishCommand vanishCommand;

  @Override
  public String getName() {
    return "Vanish";
  }

  @Override
  public void onEnable() {
    vanishCommand = new VanishCommand(this);

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(BungeeChat.getInstance(), vanishCommand);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterCommand(vanishCommand);
  }
}

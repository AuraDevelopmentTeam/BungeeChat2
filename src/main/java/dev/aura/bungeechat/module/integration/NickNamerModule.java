package dev.aura.bungeechat.module.integration;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.integration.NickNamerIntegration;
import net.md_5.bungee.api.ProxyServer;

public class NickNamerModule extends PluginIntegrationModule {
  public static final String channelName = "nicknamer:main";
  private static final String pluginName = "NickNamerBungee";

  private NickNamerIntegration nickNamerIntegration;

  @Override
  public String getName() {
    return "NickNamer";
  }

  @Override
  public boolean isEnabled() {
    return isPluginPresent(pluginName) || super.isEnabled();
  }

  @Override
  public void onEnable() {
    final ProxyServer proxyServer = ProxyServer.getInstance();

    // Since we don't own the channel we only want to register and unregister it if NickNamerBungee
    // is not installed
    if (!isPluginPresent(pluginName)) proxyServer.registerChannel(channelName);

    nickNamerIntegration = new NickNamerIntegration();
    proxyServer.getPluginManager().registerListener(BungeeChat.getInstance(), nickNamerIntegration);
  }

  @Override
  public void onDisable() {
    final ProxyServer proxyServer = ProxyServer.getInstance();

    // Since we don't own the channel we only want to register and unregister it if NickNamerBungee
    // is not installed
    if (!isPluginPresent(pluginName)) proxyServer.unregisterChannel(channelName);

    proxyServer.getPluginManager().unregisterListener(nickNamerIntegration);
    nickNamerIntegration = null;
  }
}

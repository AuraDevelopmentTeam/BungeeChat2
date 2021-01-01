package dev.aura.bungeechat.integration;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NickNamerIntegration implements Listener {
  @EventHandler
  public void onPluginMessage(PluginMessageEvent e) {
    System.out.println("Plugin Message: " + e.getTag());
  }
}

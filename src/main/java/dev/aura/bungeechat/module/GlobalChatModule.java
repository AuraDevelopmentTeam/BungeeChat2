package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.command.GlobalChatCommand;
import dev.aura.bungeechat.listener.GlobalChatListener;
import net.md_5.bungee.api.ProxyServer;

public class GlobalChatModule extends Module {
  private GlobalChatCommand globalChatCommand;
  private GlobalChatListener globalChatListener;

  @Override
  public String getName() {
    return "GlobalChat";
  }

  @Override
  public void onEnable() {
    globalChatCommand = new GlobalChatCommand(this);
    globalChatListener = new GlobalChatListener();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(BungeeChat.getInstance(), globalChatCommand);
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(BungeeChat.getInstance(), globalChatListener);

    if (getModuleSection().getBoolean("default")
        || !BungeecordModuleManager.LOCAL_CHAT_MODULE.isEnabled()) {
      Account.staticSetDefaultChannelType(ChannelType.GLOBAL);
    }
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getPluginManager().unregisterCommand(globalChatCommand);
    ProxyServer.getInstance().getPluginManager().unregisterListener(globalChatListener);

    Account.staticSetDefaultChannelType(ChannelType.LOCAL);
  }
}

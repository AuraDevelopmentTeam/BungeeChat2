package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
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

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), globalChatCommand);
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), globalChatListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(globalChatCommand);
        ProxyServer.getInstance().getPluginManager().unregisterListener(globalChatListener);
    }
}

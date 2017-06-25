package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.ClearChatCommand;
import net.md_5.bungee.api.ProxyServer;

public class ClearChatModule extends Module {
    private ClearChatCommand clearChatCommand;

    @Override
    public String getName() {
        return "ClearChat";
    }

    @Override
    public void onEnable() {
        clearChatCommand = new ClearChatCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), clearChatCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(clearChatCommand);
    }
}

package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.StaffChatCommand;
import dev.aura.bungeechat.listener.StaffChatListener;
import net.md_5.bungee.api.ProxyServer;

public class StaffChatModule implements Module {
    private StaffChatCommand staffChatCommand;
    private StaffChatListener staffChatListener;

    @Override
    public String getName() {
        return "StaffChat";
    }

    @Override
    public void onEnable() {
        staffChatCommand = new StaffChatCommand(this);
        staffChatListener = new StaffChatListener();

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), staffChatCommand);
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), staffChatListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(staffChatCommand);
        ProxyServer.getInstance().getPluginManager().unregisterListener(staffChatListener);
    }
}

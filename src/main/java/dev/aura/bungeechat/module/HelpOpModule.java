package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.HelpOpCommand;
import net.md_5.bungee.api.ProxyServer;

public class HelpOpModule implements Module {
    private HelpOpCommand helpOpCommand;

    @Override
    public String getName() {
        return "HelpOp";
    }

    @Override
    public void onEnable() {
        helpOpCommand = new HelpOpCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), helpOpCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(helpOpCommand);
    }
}

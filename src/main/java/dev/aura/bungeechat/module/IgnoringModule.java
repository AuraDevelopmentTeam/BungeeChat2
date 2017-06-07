package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.IgnoreCommand;
import net.md_5.bungee.api.ProxyServer;

public class IgnoringModule extends Module {
    private IgnoreCommand ignoreCommand;

    @Override
    public String getName() {
        return "Ignoring";
    }

    @Override
    public void onEnable() {
        ignoreCommand = new IgnoreCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), ignoreCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(ignoreCommand);
    }
}

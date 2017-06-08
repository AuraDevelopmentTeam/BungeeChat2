package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.LocalSpyCommand;
import dev.aura.bungeechat.command.SocialSpyCommand;
import net.md_5.bungee.api.ProxyServer;

public class SpyModule extends Module {
    private SocialSpyCommand socialSpyCommand;
    private LocalSpyCommand localSpyCommand;

    @Override
    public String getName() {
        return "Spy";
    }

    @Override
    public void onEnable() {
        socialSpyCommand = new SocialSpyCommand(this);
        localSpyCommand = new LocalSpyCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), socialSpyCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), localSpyCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(socialSpyCommand);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(localSpyCommand);
    }
}

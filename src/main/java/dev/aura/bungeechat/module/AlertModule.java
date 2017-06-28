package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.AlertCommand;
import dev.aura.bungeechat.task.AutomaticBroadcastTask;
import net.md_5.bungee.api.ProxyServer;

public class AlertModule extends Module {
    private AlertCommand alertCommand;

    @Override
    public String getName() {
        return "Alert";
    }

    @Override
    public void onEnable() {
        alertCommand = new AlertCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), alertCommand);
        AutomaticBroadcastTask.startAutoBroadcast();
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(alertCommand);
    }
}

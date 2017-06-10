package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.MuteCommand;
import dev.aura.bungeechat.command.TempMuteCommand;
import dev.aura.bungeechat.command.UnmuteCommand;
import dev.aura.bungeechat.listener.MutingListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;

public class MutingModule extends Module {
    private MuteCommand muteCommand;
    private TempMuteCommand tempMuteCommand;
    private UnmuteCommand unmuteCommand;
    private MutingListener mutingListener;
    private final String[] mutePlugins = { "BungeeBan", "BungeeSystem", "BungeeAdminTools", "Banmanager" };

    @Override
    public String getName() {
        return "Muting";
    }

    @Override
    public boolean isEnabled() {
        if (!super.isEnabled())
            return false;

        if (getModuleSection().getBoolean("disableWithOtherMutePlugins")) {
            PluginManager pm = ProxyServer.getInstance().getPluginManager();

            for (String mutePlugin : mutePlugins) {
                if (pm.getPlugin(mutePlugin) != null)
                    return false;
            }
        }

        return true;
    }

    @Override
    public void onEnable() {
        muteCommand = new MuteCommand(this);
        tempMuteCommand = new TempMuteCommand(this);
        unmuteCommand = new UnmuteCommand(this);
        mutingListener = new MutingListener();

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), muteCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), tempMuteCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), unmuteCommand);
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), mutingListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(muteCommand);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(tempMuteCommand);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(unmuteCommand);
        ProxyServer.getInstance().getPluginManager().unregisterListener(mutingListener);
    }
}

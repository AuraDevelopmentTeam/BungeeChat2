package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.listener.VersionCheckerListener;
import net.md_5.bungee.api.ProxyServer;

public class VersionCheckerModule extends Module {
    private VersionCheckerListener versionCheckerListener;

    @Override
    public String getName() {
        return "VersionChecker";
    }

    @Override
    public void onEnable() {
        versionCheckerListener = new VersionCheckerListener(getModuleSection().getBoolean("checkOnAdminLogin"));

        ProxyServer.getInstance().getPluginManager().registerListener(BungeeChat.getInstance(), versionCheckerListener);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterListener(versionCheckerListener);
    }
}

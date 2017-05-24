package dev.aura.bungeechat;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.listener.PlaceHolderListener;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BungeeChat extends Plugin implements BungeeChatApi {

    @Override
    public void onEnable() {
        Config.load();
        if (CONFIG_VERSION != Config.get().getDouble("Version")) {
            Logger.info("You config is outdated and might cause errors when been used with this version of BungeeChat!");
            Logger.info("Please update your config by either deleting your old one or downloading the new one on the plugin page.");
            return;
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AccountManager());
        loadScreen();
    }
    
    @Override
	public ServerType getServerType() {
		return ServerType.BUNGEECORD;
	}
    
    @Override
    public boolean hasPermission(BungeeChatAccount account, Permission permission) {
    	return PermissionManager.hasPermission(account, permission);
    }

    private void loadScreen() {
        Logger.normal(ChatColor.GOLD + "---------------- " + ChatColor.AQUA + "Bungee Chat" + ChatColor.GOLD + " ----------------");
        Logger.normal(ChatColor.YELLOW + "Authors: " + ChatColor.GREEN + AUTHOR_SHAWN + " & " + AUTHOR_BRAINSTONE);
        Logger.normal(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + VERSION);
        Logger.normal(ChatColor.YELLOW + "Build: " + ChatColor.GREEN + BUILD);
        //TODO: Loaded Modules Displayed.
        if (!isLatestVersion()) {
            Logger.normal(ChatColor.YELLOW + "There is an update avalible. You can download version " + ChatColor.GREEN
                    + getLatestVersion() + ChatColor.YELLOW + " on the plugin page at SpigotMC.org!");
        }
        Logger.normal(ChatColor.GOLD + "---------------------------------------------");
    }

    private String getLatestVersion() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + PLUGIN_ID).getBytes("UTF-8"));
            return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isLatestVersion() {
        return getLatestVersion().equals(VERSION);
    }

}

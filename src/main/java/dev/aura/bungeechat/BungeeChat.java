package dev.aura.bungeechat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;
import dev.aura.bungeechat.command.ReloadCommand;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.PlaceHolders;
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.bungeechat.util.Version;
import lombok.Cleanup;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeChat extends Plugin implements BungeeChatApi {
    @Getter
    private static BungeeChat instance;
    @Getter(lazy = true)
    private final String latestVersion = queryLatestVersion();

    @Override
    public void onEnable() {
        instance = this;
        BungeeChatInstaceHolder.setInstance(instance);

        Config.load();
        PlaceHolders.registerPlaceholders();

        if (CONFIG_VERSION != Config.get().getDouble("Version")) {
            LoggerHelper.info(
                    "You config is outdated and might cause errors when been used with this version of BungeeChat!");
            LoggerHelper.info(
                    "Please update your config by either deleting your old one or downloading the new one on the plugin page.");
            return;
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AccountManager());
        ModuleManager.enableModules();
        loadScreen();
    }

    @Override
    public void onDisable() {
        ModuleManager.disableModules();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUNGEECORD;
    }

    @Override
    public boolean hasPermission(BungeeChatAccount account, Permission permission) {
        return PermissionManager.hasPermission(account, permission);
    }

    @Override
    public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
        MessagesService.sendPrivateMessage(context);
    }

    @Override
    public void sendChannelMessage(BungeeChatContext context, ChannelType channel) throws InvalidContextError {
        MessagesService.sendChannelMessage(context, channel);
    }

    private void loadScreen() {
        LoggerHelper.info(ChatColor.GOLD + "---------------- " + ChatColor.AQUA + "Bungee Chat" + ChatColor.GOLD
                + " ----------------");
        LoggerHelper.info(ChatColor.YELLOW + "Authors: " + ChatColor.GREEN + AUTHOR_SHAWN + " & " + AUTHOR_BRAINSTONE);
        LoggerHelper.info(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + VERSION);
        LoggerHelper.info(ChatColor.YELLOW + "Build: " + ChatColor.GREEN + BUILD);
        LoggerHelper.info(ChatColor.YELLOW + "Modules: " + ChatColor.GREEN + ModuleManager.getActiveModuleString());
        if (!isLatestVersion()) {
            LoggerHelper.info(ChatColor.YELLOW + "There is an update avalible. You can download version "
                    + ChatColor.GREEN + getLatestVersion() + ChatColor.YELLOW + " on the plugin page at SpigotMC.org!");
        }
        LoggerHelper.info(ChatColor.GOLD + "---------------------------------------------");
    }

    private String queryLatestVersion() {
        try {
            @Cleanup("disconnect")
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
                    .openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(
                    ("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + PLUGIN_ID)
                            .getBytes("UTF-8"));

            return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (Exception ex) {
            LoggerHelper.warning("Could not fetch the latest version!", ex);

            return "";
        }
    }

    public boolean isLatestVersion() {
        return (new Version(getLatestVersion())).compareTo(new Version(VERSION)) < 0;
    }
}

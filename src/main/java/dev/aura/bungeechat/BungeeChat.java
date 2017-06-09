package dev.aura.bungeechat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import dev.aura.bungeechat.account.AccountFileStorage;
import dev.aura.bungeechat.account.AccountSQLStorage;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;
import dev.aura.bungeechat.command.BungeeChatCommand;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.hook.DefaultHook;
import dev.aura.bungeechat.hook.StoredDataHook;
import dev.aura.bungeechat.listener.ChannelTypeCorrectorListener;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolders;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.bungeechat.util.Version;
import lombok.Cleanup;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class BungeeChat extends Plugin implements BungeeChatApi {
    private static final String storedDataHookName = "storedData";
    private static final String defaultHookName = "default";
    @Getter
    private static BungeeChat instance;
    @Getter(lazy = true)
    private final String latestVersion = queryLatestVersion();
    private File configDir;
    private BungeeChatCommand bungeeChatCommand;
    private BungeecordAccountManager bungeecordAccountManager;
    private ChannelTypeCorrectorListener channelTypeCorrectorListener;

    @Override
    public void onLoad() {
        instance = this;
        BungeeChatInstaceHolder.setInstance(instance);
    }

    @Override
    public void onEnable() {
        onEnable(true);
    }

    public void onEnable(boolean prinLoadScreen) {
        Config.load();

        PlaceHolders.registerPlaceholders();

        Configuration accountDataBase = Config.get().getSection("AccountDataBase");

        if (accountDataBase.getBoolean("enabled")) {
            try {
                AccountManager.setAccountStorage(
                        new AccountSQLStorage(accountDataBase.getString("ip"), accountDataBase.getInt("port"),
                                accountDataBase.getString("database"), accountDataBase.getString("user"),
                                accountDataBase.getString("password"), accountDataBase.getString("tablePrefix")));
            } catch (SQLException e) {
                LoggerHelper.error("Could not connect to specified database. Using file storage", e);

                AccountManager.setAccountStorage(new AccountFileStorage());
            }
        } else {
            AccountManager.setAccountStorage(new AccountFileStorage());
        }

        bungeeChatCommand = new BungeeChatCommand();
        bungeecordAccountManager = new BungeecordAccountManager();
        channelTypeCorrectorListener = new ChannelTypeCorrectorListener();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, bungeeChatCommand);
        ProxyServer.getInstance().getPluginManager().registerListener(this, bungeecordAccountManager);
        ProxyServer.getInstance().getPluginManager().registerListener(this, channelTypeCorrectorListener);

        Configuration permissionsManager = Config.get().getSection("Settings.PermissionsManager");

        BungeecordModuleManager.registerPluginModules();
        ModuleManager.enableModules();
        HookManager.addHook(storedDataHookName, new StoredDataHook());
        HookManager.addHook(defaultHookName, new DefaultHook(permissionsManager.getString("defaultPrefix"),
                permissionsManager.getString("defaultSuffix")));

        if (prinLoadScreen) {
            loadScreen();
        }
    }

    @Override
    public void onDisable() {
        HookManager.removeHook(defaultHookName);
        HookManager.removeHook(storedDataHookName);
        ModuleManager.disableModules();

        ProxyServer.getInstance().getPluginManager().unregisterListener(bungeecordAccountManager);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(bungeeChatCommand);
        ProxyServer.getInstance().getPluginManager().unregisterListener(channelTypeCorrectorListener);

        // Just to be sure
        ProxyServer.getInstance().getPluginManager().unregisterListeners(this);
        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

        PlaceHolderManager.clear();
        ModuleManager.clearActiveModules();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUNGEECORD;
    }

    @Override
    public File getConfigFolder() {
        if (configDir == null) {
            configDir = new File(getProxy().getPluginsFolder(), "BungeeChat");
            configDir.mkdirs();
        }

        return configDir;
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
        LoggerHelper.info(
                ChatColor.YELLOW + "Modules: " + ChatColor.GREEN + BungeecordModuleManager.getActiveModuleString());
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

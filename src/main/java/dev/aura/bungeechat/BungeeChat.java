package dev.aura.bungeechat;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.listeners.PlaceHolderListener;
import dev.aura.bungeechat.listeners.PlayerConnectionListeners;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class BungeeChat extends Plugin implements BungeeChatApi {

    private static CopyOnWriteArrayList<Account> userAccounts = new CopyOnWriteArrayList<>();

    public static Account getUserAccount(UUID uuid) {
        for (Account acc : userAccounts) {
            if (acc.getUniqueId().equals(uuid)) return acc;
        }
        return null;
    }
    public static Account getUserAccount(ProxiedPlayer player) {
        return getUserAccount(player.getUniqueId());
    }

    public static void registerAccount(Account account) { userAccounts.add(account); }
    public static void unregisterAccount(Account account) { userAccounts.remove(account); }

    public static void saveAccount(Account account) throws IOException {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata");
        if (!folder.exists()){
            folder.mkdir();
        }
        File checker = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + account.getUniqueId().toString() + ".sav");
        if (!checker.exists()) {
            checker.createNewFile();
        }
        FileOutputStream saveFile= new FileOutputStream(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + account.getUniqueId().toString() + ".sav");
        ObjectOutputStream save = new ObjectOutputStream(saveFile);
        save.writeObject(account.getChannelType());
        save.writeObject(account.isMessanger());
        save.writeObject(account.isVanished());
        save.writeObject(account.isSocialspy());
        save.writeObject(account.getIgnored());
        save.close();
        unregisterAccount(account);
    }

    public static void loadAccount(UUID uuid) throws IOException, ClassNotFoundException {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata");
        if (!folder.exists()){
            registerAccount(new Account(uuid));
            return;
        }
        File checker = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + uuid.toString() + ".sav");
        if (!checker.exists()) {
            registerAccount(new Account(uuid));
            return;
        }
        FileInputStream saveFile = new FileInputStream(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/userdata/" + uuid + ".sav");
        ObjectInputStream save = new ObjectInputStream(saveFile);
        Account account = new Account(uuid, (ChannelType) save.readObject(), (boolean) save.readObject(), (boolean) save.readObject(), (boolean) save.readObject(),
                (CopyOnWriteArrayList<UUID>) save.readObject());
        save.close();
        registerAccount(account);
    }

    @Override
    public void onEnable() {
        Config.load();
        if (CONFIG_VERSION != Config.get().getDouble("Version")) {
            Logger.info("You config is outdated and might cause errors when been used with this version of BungeeChat!");
            Logger.info("Please update your config by either deleting your old one or downloading the new one on the plugin page.");
            return;
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlaceHolderListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerConnectionListeners());
        loadScreen();
    }
    
    @Override
	public ServerType getServerType() {
		return ServerType.BUNGEECORD;
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

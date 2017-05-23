package dev.aura.bungeechat;

import dev.aura.bungeechat.api.BungeeChatApi;
import net.alpenblock.bungeeperms.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        for (Account acc : userAccounts) {
            if (acc.getUniqueId().equals(player.getUniqueId())) return acc;
        }
        return null;
    }

    public static void registerAccount(Account account) { userAccounts.add(account); }
    public static void unregisterAccount(Account account) { userAccounts.remove(account); }

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
        loadScreen();
    }

    private void loadScreen() {
        Logger.normal(ChatColor.GOLD + "---------------- " + ChatColor.AQUA + "Bungee Chat" + ChatColor.GOLD + " ----------------");
        Logger.normal(ChatColor.YELLOW + "Authors: " + ChatColor.GREEN + AUTHOR_SHAWN + " & " + AUTHOR_BRAINSTONE);
        Logger.normal(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + VERSION);
        Logger.normal(ChatColor.YELLOW + "Build: " + ChatColor.GREEN + BUILD);
        //TODO: Loaded Modules Displayed.
        if (isLatestVersion()) {
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
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=12592").getBytes("UTF-8"));
            return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isLatestVersion() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=12592").getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            return version.equals(VERSION);
        } catch (Exception ex) {
            return false;
        }
    }

}

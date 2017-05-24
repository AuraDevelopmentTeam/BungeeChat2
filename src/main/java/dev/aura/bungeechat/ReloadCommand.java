package dev.aura.bungeechat;

import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.permissions.Permission;
import dev.aura.bungeechat.permissions.PermissionManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecation")
public class ReloadCommand extends Command {

    private final String prefix = ChatColor.BLUE + "Bungee Chat " + ChatColor.DARK_GRAY + "// ";

    public ReloadCommand(){ super("bungeechat", ""); }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("reload") && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_RELOAD)) {
                Config.reload();
                sender.sendMessage(prefix + ChatColor.GREEN + "Your configuration has been reloaded!");
                return;
            }
        }
        checkForUpdates(sender);
        sender.sendMessage(prefix + ChatColor.GRAY + "Coded by " + ChatColor.GOLD + BungeeChat.AUTHOR_SHAWN + ChatColor.GRAY + " and " + ChatColor.GOLD + BungeeChat.AUTHOR_BRAINSTONE + ChatColor.GRAY +  " with help from " + ChatColor.GOLD + "paulpkyou" + ChatColor.GRAY + ".");
    }

    private boolean checkForUpdates(CommandSender sender) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + BungeeChat.PLUGIN_ID).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(BungeeChat.VERSION)) {
                sender.sendMessage(prefix + ChatColor.GRAY + "Version: " + ChatColor.RED + BungeeChat.VERSION + " (Build #" + BungeeChat.BUILD + ")");
                sender.sendMessage(prefix + ChatColor.GRAY + "Newest Version: " + ChatColor.GREEN + version);
                return true;
            } else {
                sender.sendMessage(prefix + ChatColor.GRAY + "Version: " + ChatColor.GREEN + BungeeChat.VERSION + " [test] (Build #" + BungeeChat.BUILD + ")");
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

}

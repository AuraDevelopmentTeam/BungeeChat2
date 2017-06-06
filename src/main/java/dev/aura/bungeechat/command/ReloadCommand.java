package dev.aura.bungeechat.command;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

@SuppressWarnings("deprecation")
public class ReloadCommand extends BaseCommand {
    private final String prefix = ChatColor.BLUE + "Bungee Chat " + ChatColor.DARK_GRAY + "// ";

    public ReloadCommand() {
        super("bungeechat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("reload")
                    && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_RELOAD)) {

                BungeeChat.getInstance().onDisable();
                BungeeChat.getInstance().onEnable(false);

                sender.sendMessage(prefix + ChatColor.GREEN + "The plugin has been reloaded!");

                return;
            }
        }

        checkForUpdates(sender);
        sender.sendMessage(prefix + ChatColor.GRAY + "Coded by " + ChatColor.GOLD + BungeeChatApi.AUTHOR_SHAWN
                + ChatColor.GRAY + " and " + ChatColor.GOLD + BungeeChatApi.AUTHOR_BRAINSTONE + ChatColor.GRAY
                + ", with help from " + ChatColor.GOLD + BungeeChatApi.AUTHOR_RYADA + ChatColor.GRAY + ".");
    }

    private boolean checkForUpdates(CommandSender sender) {
        String version = BungeeChat.getInstance().getLatestVersion();

        if (BungeeChat.getInstance().isLatestVersion()) {
            sender.sendMessage(prefix + ChatColor.GRAY + "Version: " + ChatColor.GREEN + BungeeChatApi.VERSION
                    + " [test] (Build #" + BungeeChatApi.BUILD + ")");

            return false;
        } else {
            sender.sendMessage(prefix + ChatColor.GRAY + "Version: " + ChatColor.RED + BungeeChatApi.VERSION
                    + " (Build #" + BungeeChatApi.BUILD + ")");
            sender.sendMessage(prefix + ChatColor.GRAY + "Newest Version: " + ChatColor.GREEN + version);

            return true;
        }
    }

}

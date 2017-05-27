package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class AlertModule extends Command implements Module {
    @Override
    public String getName() {
        return "Alert";
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().
                registerCommand(BungeeChat.getInstance(), this);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(this);
    }

    public AlertModule() {
        super("alert", "", Config.get().getStringList(getConfigBasePath() + "aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!PermissionManager.hasPermission(sender, Permission.COMMAND_ALERT)) {
            //TODO: No Perms Message.
            return;
        }
        if (args.length < 1) {
            //TODO: Command Usage Message.
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) {
                stringBuilder.append(arg).append(" ");
            }
            String finalMessage;
            if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                finalMessage = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString().trim());
            } else {
                finalMessage = stringBuilder.toString().trim();
            }
            //TODO
        }
    }

}

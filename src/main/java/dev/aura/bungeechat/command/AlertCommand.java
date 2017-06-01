package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.AlertModule;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class AlertCommand extends BaseCommand {
    public AlertCommand(AlertModule alertModule) {
        super("alert", alertModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_ALERT)) {
            if (args.length < 1) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/alert <message>"));
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                String finalMessage = stringBuilder.toString().trim();

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
                }

                String Format = PlaceHolderUtil.getFullFormatMessage("alert", new Context(sender, finalMessage));

                ProxyServer.getInstance().broadcast(Format);
            }
        }
    }
}

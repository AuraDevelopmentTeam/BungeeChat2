package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.HelpOpModule;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class HelpOpCommand extends BaseCommand {
    public HelpOpCommand(HelpOpModule helpOpModule) {
        super("helpop", helpOpModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_HELPOP)) {
            if (args.length < 1) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/helpop <message>"));
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                String finalMessage = stringBuilder.toString().trim();

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
                }

                String Format = PlaceHolderUtil.getFullMessage("helpop", new Context(sender, finalMessage));

                ProxyServer.getInstance().getPlayers().stream()
                        .filter(pp -> PermissionManager.hasPermission(pp, Permission.COMMAND_HELPOP))
                        .forEach(pp -> pp.sendMessage(Format));
            }
        }
    }
}

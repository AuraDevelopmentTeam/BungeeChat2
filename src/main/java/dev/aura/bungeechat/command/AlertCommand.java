package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.module.AlertModule;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class AlertCommand extends BaseCommand {
    public AlertCommand(AlertModule alertModule) {
        super("alert", alertModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_ALERT)) {
            if (args.length < 1) {
                // TODO: Command Usage Message.
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
                String rawFormat = Config.get().getString("Formats.alert");
                String Format = PlaceHolderManager.processMessage(rawFormat, new Context(sender));

            }
        }
    }
}

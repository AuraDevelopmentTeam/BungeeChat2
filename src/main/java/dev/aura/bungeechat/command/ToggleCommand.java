package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class ToggleCommand extends BaseCommand {
    public ToggleCommand(MessengerModule messengerModule) {
        super("msgtoggle", messengerModule.getModuleSection().getStringList("aliases.toggle"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            //TODO: Command
        }
    }
}

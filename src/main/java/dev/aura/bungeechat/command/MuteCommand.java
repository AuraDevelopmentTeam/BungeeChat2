package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.MutingModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class MuteCommand extends BaseCommand {
    public MuteCommand(MutingModule mutingModule) {
        super("mute", mutingModule.getModuleSection().getStringList("aliases.mute"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MUTE_TEMPMUTE)) {
            if (args.length < 1) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/mute <player> [time]"));
            } else {
                //TODO Mute Stuff.
            }
        }
    }
}

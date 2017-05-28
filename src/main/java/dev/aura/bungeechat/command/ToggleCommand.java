package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ToggleCommand extends BaseCommand {
    public ToggleCommand(MessengerModule messengerModule) {
        super("msgtoggle", messengerModule.getModuleSection().getStringList("aliases.toggle"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                AccountManager.getUserAccount(player).toggleMessanger();
                if (!AccountManager.getUserAccount(player).hasMessangerEnabled()) {
                    player.sendMessage(Message.DISABLE_MESSAGER.get());
                } else {
                    player.sendMessage(Message.ENABLE_MESSAGER.get());
                }
            }
        }
    }
}

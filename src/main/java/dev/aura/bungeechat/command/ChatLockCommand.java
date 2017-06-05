package dev.aura.bungeechat.command;

import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.ChatLockModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatLockCommand extends BaseCommand {
    private static final String USAGE = "/lockchat <local|global>";
    
    public ChatLockCommand(ChatLockModule chatLockModule) {
        super("lockchat", chatLockModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                if (args.length < 1) {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(player, USAGE));
                } else {
                    if (args[0].equalsIgnoreCase("global")) {
                        //TODO: ENABLE CHATLOCK OR DISABLE GLOBAL
                    } else if (args[0].equalsIgnoreCase("local")) {
                        String serverName = player.getServer().getInfo().getName();
                        //TODO: ADD SERVER TO LOCKED.
                    } else {
                        sender.sendMessage(Message.INCORRECT_USAGE.get(player, USAGE));
                    }
                }
            }
        }
    }
}

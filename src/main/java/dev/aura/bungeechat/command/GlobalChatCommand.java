package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.GlobalChatModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GlobalChatCommand extends BaseCommand {
    public GlobalChatCommand(GlobalChatModule globalChatModule) {
        super("global", globalChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL)) {
            if (ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("default")) {
                sender.sendMessage(Message.GLOBAL_IS_DEFAULT.get());
                return;
            }
            if (args.length < 1) {
                if (!(sender instanceof ProxiedPlayer)) {
                    sender.sendMessage(Message.NOT_A_PLAYER.get());
                    return;
                }
                if (PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL_TOGGLE)) {
                    BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                    if (player.getChannelType() == ChannelType.GLOBAL) {
                        player.setChannelType(ChannelType.NONE);
                        sender.sendMessage(Message.ENABLE_LOCAL.get());
                    } else {
                        player.setChannelType(ChannelType.GLOBAL);
                        sender.sendMessage(Message.ENABLE_GLOBAL.get());
                    }
                } else {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/global <message>"));
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                MessagesService.sendGlobalMessage(sender, stringBuilder.toString().trim());
            }
        }
    }
}

package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ClearChatModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.Optional;

public class ClearChatCommand extends BaseCommand {
    public ClearChatCommand(ClearChatModule clearChatModule) {
        super("clearchat", clearChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CLEAR_CHAT)) {
            if (args.length == 0) {
                MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/clearchat <local|global>"));
            } else {

                int lines = Math.abs(BungeecordModuleManager.CLEAR_CHAT_MODULE.getModuleSection().getInt("emptyLines"));
                Optional<BungeeChatAccount> bungeeChatAccount = BungeecordAccountManager.getAccount(sender);

                if (args[0].equalsIgnoreCase("local")) {
                    //
                } else if (args[0].equalsIgnoreCase("global")) {
                    while (lines != 0) {
                        ProxyServer.getInstance().broadcast(" ");
                        lines--;
                    }
                    ProxyServer.getInstance().broadcast(Message.CLEARED_GLOBAL.get(sender));
                } else {
                    MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/clearchat <local|global>"));
                }

            }
        }
    }
}
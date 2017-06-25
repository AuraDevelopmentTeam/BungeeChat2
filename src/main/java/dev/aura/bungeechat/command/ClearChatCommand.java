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

public class ClearChatCommand extends BaseCommand {
    private static final String USAGE = "/clearchat <local|global>";
    private static final String EMPTY_LINE = " ";

    public ClearChatCommand(ClearChatModule clearChatModule) {
        super("clearchat", clearChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CLEAR_CHAT)) {
            if (args.length == 0) {
                MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, USAGE));
            } else {

                final int lines = Math.abs(BungeecordModuleManager.CLEAR_CHAT_MODULE.getModuleSection().getInt("emptyLines"));
                final BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(sender).get();
                final String serverName = bungeeChatAccount.getServerName();

                if (args[0].equalsIgnoreCase("local")) {
                    for (int i = 0; i < lines; i++) {
                        MessagesService.sendToMatchingPlayers(EMPTY_LINE, MessagesService.getLocalPredicate(serverName));
                    }
                    MessagesService.sendToMatchingPlayers(Message.CLEARED_LOCAL.get(sender), MessagesService.getLocalPredicate(serverName));
                } else if (args[0].equalsIgnoreCase("global")) {
                    for (int i = 0; i < lines; i++) {
                        MessagesService.sendToMatchingPlayers(EMPTY_LINE, MessagesService.getGlobalPredicate());
                    }
                    MessagesService.sendToMatchingPlayers(Message.CLEARED_GLOBAL.get(sender), MessagesService.getGlobalPredicate());
                } else {
                    MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, USAGE));
                }

            }
        }
    }
}

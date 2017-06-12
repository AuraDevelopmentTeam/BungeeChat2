package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ChatLockModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatLockCommand extends BaseCommand {
    private static final String USAGE = "/chatlock <local|global> [clear]";
    private static final String EMPTY_LINE = "";

    public ChatLockCommand(ChatLockModule chatLockModule) {
        super("chatlock", chatLockModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
            } else {
                BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

                if (args.length < 1) {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(player, USAGE));
                } else {
                    final ChatLockModule chatLock = BungeecordModuleManager.CHAT_LOCK_MODULE;
                    final boolean clear = (args.length >= 2) && args[1].equalsIgnoreCase("clear");
                    final int emptyLines = clear ? chatLock.getModuleSection().getInt("emptyLinesOnClear") : 0;

                    if (args[0].equalsIgnoreCase("global")) {
                        if (chatLock.isGlobalChatLockEnabled()) {
                            chatLock.disableGlobalChatLock();
                            sender.sendMessage(Message.DISABLE_CHATLOCK.get(player));
                        } else {
                            chatLock.enableGlobalChatLock();

                            if (clear) {
                                for (int i = 0; i < emptyLines; i++) {
                                    MessagesService.sendToMatchingPlayers(EMPTY_LINE,
                                            MessagesService.getGlobalPredicate());
                                }
                            }

                            MessagesService.sendToMatchingPlayers(Message.ENABLE_CHATLOCK.get(player),
                                    MessagesService.getGlobalPredicate());
                        }
                    } else if (args[0].equalsIgnoreCase("local")) {
                        String serverName = player.getServerName();

                        if (chatLock.isLocalChatLockEnabled(serverName)) {
                            chatLock.disableLocalChatLock(serverName);
                            sender.sendMessage(Message.DISABLE_CHATLOCK.get(player));
                        } else {
                            chatLock.enableLocalChatLock(serverName);

                            if (clear) {
                                for (int i = 0; i < emptyLines; i++) {
                                    MessagesService.sendToMatchingPlayers(EMPTY_LINE,
                                            MessagesService.getLocalPredicate(serverName));
                                }
                            }

                            MessagesService.sendToMatchingPlayers(Message.ENABLE_CHATLOCK.get(player),
                                    MessagesService.getLocalPredicate(serverName));
                        }
                    } else {
                        sender.sendMessage(Message.INCORRECT_USAGE.get(player, USAGE));
                    }
                }
            }
        }
    }
}

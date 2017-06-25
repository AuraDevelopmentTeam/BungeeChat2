package dev.aura.bungeechat.command;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.LoggerHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class BungeeChatCommand extends BaseCommand {
    private final String prefix = ChatColor.BLUE + "Bungee Chat " + ChatColor.DARK_GRAY + "// ";

    public BungeeChatCommand() {
        super("bungeechat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("reload")
                    && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_RELOAD)) {
                final BungeeChat instance = BungeeChat.getInstance();

                ProxyServer.getInstance().getScheduler().runAsync(instance, () -> {
                    instance.onDisable();
                    instance.onEnable(false);

                    MessagesService.sendMessage(sender, prefix + ChatColor.GREEN + "The plugin has been reloaded!");
                });

                return;
            } else if (args[0].equalsIgnoreCase("setprefix")
                    && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_SETPREFIX)) {

                if (args.length < 2) {
                    MessagesService.sendMessage(sender,
                            Message.INCORRECT_USAGE.get(sender, "/bungeechat setprefix <player> [new prefix]"));
                } else {
                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent()) {
                        MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
                    } else {
                        CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                        if (args.length < 3) {
                            targetAccount.get().setStoredPrefix(Optional.empty());
                            MessagesService.sendMessage(sender, prefix + Message.PREFIX_REMOVED.get(target));
                        } else {
                            String newPrefix = getUnquotedString(
                                    Arrays.stream(args, 2, args.length).collect(Collectors.joining(" ")));

                            targetAccount.get().setStoredPrefix(Optional.of(newPrefix));
                            MessagesService.sendMessage(sender, prefix + Message.PREFIX_SET.get(target));
                        }
                    }
                }
                return;
            } else if (args[0].equalsIgnoreCase("setsuffix")
                    && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_SETSUFFIX)) {

                if (args.length < 2) {
                    MessagesService.sendMessage(sender,
                            Message.INCORRECT_USAGE.get(sender, "/bungeechat setsuffix <player> [new suffix]"));
                } else {
                    Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

                    if (!targetAccount.isPresent()) {
                        MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
                    } else {
                        CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                        if (args.length < 3) {
                            targetAccount.get().setStoredSuffix(Optional.empty());
                            MessagesService.sendMessage(sender, prefix + Message.SUFFIX_REMOVED.get(target));
                        } else {
                            String newSuffix = getUnquotedString(
                                    Arrays.stream(args, 2, args.length).collect(Collectors.joining(" ")));

                            targetAccount.get().setStoredSuffix(Optional.of(newSuffix));
                            MessagesService.sendMessage(sender, prefix + Message.SUFFIX_SET.get(target));
                        }
                    }
                }

                return;
            } else if (args[0].equalsIgnoreCase("modules") && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_MODULES)) {
                MessagesService.sendMessage(sender, prefix + ChatColor.GRAY + "Active Modules: " + ChatColor.GREEN + BungeecordModuleManager.getActiveModuleString());
                return;
            }
        }

        checkForUpdates(sender);
        MessagesService.sendMessage(sender,
                prefix + ChatColor.GRAY + "Coded by " + ChatColor.GOLD + BungeeChatApi.AUTHOR_SHAWN + ChatColor.GRAY
                        + " and " + ChatColor.GOLD + BungeeChatApi.AUTHOR_BRAINSTONE + ChatColor.GRAY
                        + ", with help from " + ChatColor.GOLD + BungeeChatApi.AUTHOR_RYADA + ChatColor.GRAY + ".");
    }

    private void checkForUpdates(CommandSender sender) {
        BungeeChat instance = BungeeChat.getInstance();
        String latestVersion = instance.getLatestVersion(true);

        if (instance.isLatestVersion()) {
            MessagesService.sendMessage(sender,
                    prefix + ChatColor.GRAY + "Version: " + ChatColor.GREEN + BungeeChatApi.VERSION);
        } else {
            MessagesService.sendMessage(sender,
                    prefix + ChatColor.GRAY + "Version: " + ChatColor.RED + BungeeChatApi.VERSION);
            MessagesService.sendMessage(sender,
                    prefix + ChatColor.GRAY + "Newest Version: " + ChatColor.GREEN + latestVersion);
        }
    }

    private String getUnquotedString(String str) {
        if ((str == null) || !(str.startsWith("\"") && str.endsWith("\"")))
            return str;

        new StreamTokenizer(new StringReader(str));
        StreamTokenizer parser = new StreamTokenizer(new StringReader(str));
        String result;

        try {
            parser.nextToken();
            if (parser.ttype == '"') {
                result = parser.sval;
            } else {
                result = "ERROR!";
            }
        } catch (IOException e) {
            result = null;

            LoggerHelper.info("Encountered an IOException while parsing the input string", e);
        }

        return result;
    }
}

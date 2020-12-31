package dev.aura.bungeechat.command;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.LoggerHelper;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class BungeeChatCommand extends BaseCommand {
  private final String prefix = ChatColor.BLUE + "Bungee Chat " + ChatColor.DARK_GRAY + "// ";
  private static final List<String> arg1Completions =
      Arrays.asList("modules", "reload", "setprefix", "setsuffix");

  public BungeeChatCommand() {
    super("bungeechat");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length != 0) {
      if (args[0].equalsIgnoreCase("reload")
          && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_RELOAD)) {
        final BungeeChat instance = BungeeChat.getInstance();

        ProxyServer.getInstance()
            .getScheduler()
            .runAsync(
                instance,
                () -> {
                  instance.onDisable();
                  instance.onEnable(false);

                  MessagesService.sendMessage(
                      sender, prefix + ChatColor.GREEN + "The plugin has been reloaded!");
                });

        return;
      } else if (args[0].equalsIgnoreCase("setprefix")
          && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_SETPREFIX)) {

        if (args.length < 2) {
          MessagesService.sendMessage(
              sender,
              Messages.INCORRECT_USAGE.get(sender, "/bungeechat setprefix <player> [new prefix]"));
        } else {
          Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

          if (!targetAccount.isPresent()) {
            MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
          } else {
            CommandSender target =
                BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

            if (args.length < 3) {
              targetAccount.get().setStoredPrefix(Optional.empty());
              MessagesService.sendMessage(sender, prefix + Messages.PREFIX_REMOVED.get(target));
            } else {
              String newPrefix =
                  getUnquotedString(
                      Arrays.stream(args, 2, args.length).collect(Collectors.joining(" ")));

              targetAccount.get().setStoredPrefix(Optional.of(newPrefix));
              MessagesService.sendMessage(sender, prefix + Messages.PREFIX_SET.get(target));
            }
          }
        }
        return;
      } else if (args[0].equalsIgnoreCase("setsuffix")
          && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_SETSUFFIX)) {

        if (args.length < 2) {
          MessagesService.sendMessage(
              sender,
              Messages.INCORRECT_USAGE.get(sender, "/bungeechat setsuffix <player> [new suffix]"));
        } else {
          Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[1]);

          if (!targetAccount.isPresent()) {
            MessagesService.sendMessage(sender, Messages.PLAYER_NOT_FOUND.get());
          } else {
            CommandSender target =
                BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

            if (args.length < 3) {
              targetAccount.get().setStoredSuffix(Optional.empty());
              MessagesService.sendMessage(sender, prefix + Messages.SUFFIX_REMOVED.get(target));
            } else {
              String newSuffix =
                  getUnquotedString(
                      Arrays.stream(args, 2, args.length).collect(Collectors.joining(" ")));

              targetAccount.get().setStoredSuffix(Optional.of(newSuffix));
              MessagesService.sendMessage(sender, prefix + Messages.SUFFIX_SET.get(target));
            }
          }
        }

        return;
      } else if (args[0].equalsIgnoreCase("modules")
          && PermissionManager.hasPermission(sender, Permission.BUNGEECHAT_MODULES)) {
        MessagesService.sendMessage(
            sender,
            prefix
                + ChatColor.GRAY
                + "Active Modules: "
                + ChatColor.GREEN
                + BungeecordModuleManager.getActiveModuleString());
        return;
      }
    }

    checkForUpdates(sender);
    MessagesService.sendMessage(
        sender,
        prefix
            + ChatColor.GRAY
            + "Coded by "
            + ChatColor.GOLD
            + BungeeChatApi.AUTHOR_BRAINSTONE
            + ChatColor.GRAY
            + " and "
            + ChatColor.GOLD
            + BungeeChatApi.AUTHOR_SHAWN
            + ".");
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    final String param1 = args[0];

    if (args.length == 1) {
      return arg1Completions.stream()
          .filter(completion -> completion.startsWith(param1))
          .collect(Collectors.toList());
    } else if ((args.length == 2) && ("setprefix".equals(param1) || "setsuffix".equals(param1))) {
      return BungeecordAccountManager.getAccountsForPartialName(args[1], sender).stream()
          .map(BungeeChatAccount::getName)
          .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }

  private void checkForUpdates(CommandSender sender) {
    BungeeChat instance = BungeeChat.getInstance();
    String latestVersion = instance.getLatestVersion(true);

    if (instance.isLatestVersion()) {
      MessagesService.sendMessage(
          sender,
          prefix + ChatColor.GRAY + "Version: " + ChatColor.GREEN + BungeeChatApi.VERSION_STR);
    } else {
      MessagesService.sendMessage(
          sender,
          prefix + ChatColor.GRAY + "Version: " + ChatColor.RED + BungeeChatApi.VERSION_STR);
      MessagesService.sendMessage(
          sender, prefix + ChatColor.GRAY + "Newest Version: " + ChatColor.GREEN + latestVersion);
    }
  }

  private String getUnquotedString(String str) {
    if ((str == null) || !(str.startsWith("\"") && str.endsWith("\""))) return str;

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

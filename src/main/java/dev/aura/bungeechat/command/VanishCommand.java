package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.VanishModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VanishCommand extends BaseCommand {
  private static final List<String> arg1Completions = Arrays.asList("on", "off");

  public VanishCommand(VanishModule vanisherModule) {
    super(
        "bvanish",
        Permission.COMMAND_VANISH,
        vanisherModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH)) return;

    if (!(sender instanceof ProxiedPlayer)) {
      MessagesService.sendMessage(sender, Messages.NOT_A_PLAYER.get());
      return;
    }

    BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("on")) {
        player.setVanished(true);
      } else if (args[0].equalsIgnoreCase("off")) {
        player.setVanished(false);
      } else {
        player.toggleVanished();
      }
    } else {
      player.toggleVanished();
    }

    if (player.isVanished()) {
      MessagesService.sendMessage(sender, Messages.ENABLE_VANISH.get());
    } else {
      MessagesService.sendMessage(sender, Messages.DISABLE_VANISH.get());
    }
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    final String param1 = args[0];

    if (args.length == 1) {
      return arg1Completions.stream()
          .filter(completion -> completion.startsWith(param1))
          .collect(Collectors.toList());
    }

    return super.tabComplete(sender, args);
  }
}

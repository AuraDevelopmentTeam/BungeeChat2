package dev.aura.bungeechat.command;

import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.LocalToModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.util.ServerNameHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.CommandSender;

public class LocalToCommand extends BaseCommand {
  public LocalToCommand(LocalToModule localToModule) {
    super(
        "localto",
        Permission.COMMAND_LOCALTO,
        localToModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!PermissionManager.hasPermission(sender, Permission.COMMAND_LOCALTO)) return;

    if (args.length < 2) {
      MessagesService.sendMessage(
          sender, Messages.INCORRECT_USAGE.get(sender, "/localto <server> <message>"));
      return;
    }

    Optional<String> optServerName = ServerNameHelper.verifyServerName(args[0], sender);

    if (!optServerName.isPresent()) return;

    String serverName = optServerName.get();

    String finalMessage = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));
    MessagesService.sendLocalMessage(new Context(sender, finalMessage, serverName));
  }

  @Override
  public Collection<String> tabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
      return ServerNameHelper.getMatchingServerNames(args[0]);
    }

    return super.tabComplete(sender, args);
  }
}

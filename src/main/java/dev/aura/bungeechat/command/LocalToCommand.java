package dev.aura.bungeechat.command;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.command.BaseCommand;
import dev.aura.bungeechat.message.Context;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import dev.aura.bungeechat.module.LocalToModule;

public class LocalToCommand extends BaseCommand {

	public LocalToCommand(LocalToModule localToModule) {
		super("localto", localToModule.getModuleSection().getStringList("aliases"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (PermissionManager.hasPermission(sender, Permission.COMMAND_LOCALTO)) {
			if (!(sender instanceof ProxiedPlayer)) {
                MessagesService.sendMessage(sender, Message.NOT_A_PLAYER.get());
                return;
            }
			
			if (args.length < 2) {
				MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/localto <server> <message>"));
				return;
			}

			String server = args[0];
			boolean serverExists = false;
			for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
				if (serverInfo.getName().equalsIgnoreCase(server)) {
					serverExists = true;
					server = serverInfo.getName();
					break;
				}
			}
			
			if (!serverExists) {
				MessagesService.sendMessage(sender, Message.UNKNOWN_SERVER.get(sender, server));
				return;
			}
			
			String finalMessage = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
			MessagesService.sendLocalMessage(new Context(sender, finalMessage, server));
		}

	}

}

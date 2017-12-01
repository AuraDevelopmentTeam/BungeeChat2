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
import dev.aura.bungeechat.module.LocalToModule;

public class LocalToCommand extends BaseCommand {

	public LocalToCommand(LocalToModule localToModule) {
		super("localto", localToModule.getModuleSection().getStringList("aliases"));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (PermissionManager.hasPermission(sender, Permission.COMMAND_LOCALTO)) {
			if (args.length < 2) {
				MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/localto <server> <message>"));
			}
			else
			{
				String server = args[0];
				String finalMessage = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
				
				Boolean serverExists = false;
				for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
					if (serverInfo.getName().equalsIgnoreCase(server)) {
						serverExists = true;
						server = serverInfo.getName();
						break;
					}
				}
				if (serverExists) {
					MessagesService.sendLocalMessage(new Context(sender, finalMessage, server));
				} else {
					MessagesService.sendMessage(sender, Message.UNKNOWN_SERVER.get(sender, server));
				}
				
			}
			
		}
		
	}

}

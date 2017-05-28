package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.module.AntiSwearModule;
import dev.aura.bungeechat.module.GlobalChatModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GlobalChatCommand extends BaseCommand {
    public GlobalChatCommand(GlobalChatModule globalChatModule) {
        super("global", globalChatModule.getModuleSection().getStringList("aliases"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL)) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(Message.NOT_A_PLAYER.get());
                return;
            }
            if (Config.get().getBoolean("Settings.Features.GlobalChat.default")) {
                sender.sendMessage(Message.GLOBAL_IS_DEFAULT.get());
                return;
            }
            if (args.length < 1) {
                if(PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL_TOGGLE)) {
                    ProxiedPlayer p = (ProxiedPlayer) sender;
                    AccountManager.getUserAccount(p).setChannelType(ChannelType.GLOBAL);
                    if (!AccountManager.getUserAccount(p).getChannelType().equals(ChannelType.GLOBAL)) {
                        p.sendMessage(Message.DISABLE_GLOBAL.get());
                    } else {
                        p.sendMessage(Message.ENABLE_GLOBAL.get());
                    }
                } else {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/global <message>"));
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                String finalMessage = stringBuilder.toString().trim();

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
                }

                if (ModuleManager.getActiveModules().contains(new AntiSwearModule()) &&
                        !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR))
                    finalMessage = SwearWordsFilter.replaceSwearWords(finalMessage);

                String Format = Config.get().getString("Formats.global");
                Format = PlaceHolderManager.processMessage(Format, new Context(sender, finalMessage));

                ProxyServer.getInstance().broadcast(Format);
            }
        }
    }
}

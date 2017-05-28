package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.module.GlobalChatModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
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
            if (ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("default")) {
                sender.sendMessage(Message.GLOBAL_IS_DEFAULT.get());
                return;
            }
            if (args.length < 1) {
                if (PermissionManager.hasPermission(sender, Permission.COMMAND_GLOBAL_TOGGLE)) {
                    ProxiedPlayer p = (ProxiedPlayer) sender;
                    if (AccountManager.getUserAccount(p).getChannelType().equals(ChannelType.GLOBAL)) {
                        AccountManager.getUserAccount(p).setChannelType(ChannelType.NONE);
                        p.sendMessage(Message.DISABLE_GLOBAL.get());
                    } else {
                        AccountManager.getUserAccount(p).setChannelType(ChannelType.GLOBAL);
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

                if (ModuleManager.isModuleActive(ModuleManager.ANTI_SWEAR_MODULE)
                        && !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR)) {
                    finalMessage = SwearWordsFilter.replaceSwearWords(finalMessage);
                }

                String Format = PlaceHolderUtil.getFullMessage("global", new Context(sender, finalMessage));

                ProxyServer.getInstance().broadcast(Format);
            }
        }
    }
}

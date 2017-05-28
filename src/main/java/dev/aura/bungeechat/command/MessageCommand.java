package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.module.AntiSwearModule;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.module.SocialSpyModule;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageCommand extends BaseCommand {
    public MessageCommand(MessengerModule messengerModule) {
        super("msg", messengerModule.getModuleSection().getStringList("aliases.message"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            if (args.length < 2) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/msg <player> <message>"));
            } else {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

                if (target == sender) {
                    sender.sendMessage(Message.MESSAGE_YOURSELF.get());
                    return;
                }
                if ((target == null) || (AccountManager.getUserAccount(target).isVanished()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                    sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                    return;
                }
                if ((sender instanceof ProxiedPlayer) && !AccountManager.getUserAccount(target).hasMessangerEnabled()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_TOGGLE_MESSAGE_BYPASS)) {
                    sender.sendMessage(Message.HAS_MESSAGER_DISABLED.get(target));
                    return;
                }
                if ((sender instanceof ProxiedPlayer)
                        && AccountManager.getUserAccount(target).getIgnored()
                                .contains(((ProxiedPlayer) sender).getUniqueId())
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE_BYPASS)) {
                    sender.sendMessage(Message.HAS_INGORED.get());
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }

                String finalMessage = stringBuilder.toString().trim();

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
                }

                if (ModuleManager.getActiveModules().contains(new AntiSwearModule())
                        && !PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR))
                    finalMessage = SwearWordsFilter.replaceSwearWords(finalMessage);

                String FormatSender = PlaceHolderUtil.getFullMessage("message-sender",
                        new Context(sender, target, finalMessage));
                sender.sendMessage(FormatSender);

                String FormatTarget = PlaceHolderUtil.getFullMessage("message-target",
                        new Context(sender, target, finalMessage));
                target.sendMessage(FormatTarget);

                if (ModuleManager.getActiveModules().contains(new SocialSpyModule())) {
                    String SocialSpyFormat = PlaceHolderUtil.getFullMessage("socialspy",
                            new Context(sender, target, finalMessage));
                    ProxyServer.getInstance().getPlayers().stream().filter(pp -> !(pp == target) && !(pp == sender)
                            && AccountManager.getUserAccount(pp).hasSocialSpyEnabled()).forEach(pp -> {
                                pp.sendMessage(SocialSpyFormat);
                            });
                }

                if (sender instanceof ProxiedPlayer) {
                    ReplyCommand.setReply((ProxiedPlayer) sender, target);
                }

                // TODO: Logger..
            }
        }
    }
}

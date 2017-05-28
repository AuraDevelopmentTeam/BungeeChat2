package dev.aura.bungeechat.command;

import dev.aura.bungeechat.Message;
import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.module.SocialSpyModule;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

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
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_IGNORE_BYPASS))
                    // TODO: ignore message.
                    return;

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }

                String finalMessage = stringBuilder.toString().trim();

                if (PermissionManager.hasPermission(sender, Permission.USE_COLORED_CHAT)) {
                    finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
                }
                // TODO: AntiSwear.

                Configuration config = Config.get();

                String rawFormatSender = config.getString("Formats.message-sender");
                String FormatSender = PlaceHolderManager.processMessage(rawFormatSender, new Context(sender, target))
                        .replace("%message%", finalMessage);
                sender.sendMessage(FormatSender);

                String rawFormatTarget = config.getString("Formats.message-target");
                String FormatTarget = PlaceHolderManager.processMessage(rawFormatTarget, new Context(sender, target))
                        .replace("%message%", finalMessage);
                target.sendMessage(FormatTarget);

                if (ModuleManager.getActiveModules().contains(new SocialSpyModule())) {
                    String rawSocialSpyFormat = config.getString("Formats.socialspy");
                    String SocialSpyFormat = PlaceHolderManager
                            .processMessage(rawSocialSpyFormat, new Context(sender, target))
                            .replace("%message%", finalMessage);
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

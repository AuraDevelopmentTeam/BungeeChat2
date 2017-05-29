package dev.aura.bungeechat.command;

import java.util.HashMap;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.filter.SwearWordsFilter;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.module.ModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends BaseCommand {
    private static HashMap<ProxiedPlayer, ProxiedPlayer> replies;

    public ReplyCommand(MessengerModule messengerModule) {
        super("reply", messengerModule.getModuleSection().getStringList("aliases.reply"));
        replies = new HashMap<>();
    }

    public static void setReply(ProxiedPlayer sender, ProxiedPlayer target) {
        replies.put(sender, target);
        replies.put(target, sender);
    }

    public static ProxiedPlayer getReplier(ProxiedPlayer player) {
        return replies.getOrDefault(player, null);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Message.NOT_A_PLAYER.get());
            return;
        } else {
            if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
                if (args.length < 1) {
                    sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/reply <message>"));
                } else {
                    ProxiedPlayer target = getReplier((ProxiedPlayer) sender);

                    if ((target == null) || (AccountManager.getUserAccount(target).isVanished()
                            && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_SEE))) {
                        sender.sendMessage(Message.NO_REPLY.get());
                        return;
                    }
                    if ((sender instanceof ProxiedPlayer)
                            && !AccountManager.getUserAccount(target).hasMessangerEnabled()
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

                    String FormatSender = PlaceHolderUtil.getFullMessage("message-sender",
                            new Context(sender, target, finalMessage));
                    sender.sendMessage(FormatSender);

                    String FormatTarget = PlaceHolderUtil.getFullMessage("message-target",
                            new Context(sender, target, finalMessage));
                    sender.sendMessage(FormatTarget);

                    if (ModuleManager.isModuleActive(ModuleManager.SOCIAL_SPY_MODULE)) {
                        String SocialSpyFormat = PlaceHolderUtil.getFullMessage("socialspy",
                                new Context(sender, target, finalMessage));
                        ProxyServer.getInstance().getPlayers().stream()
                                .filter(pp -> !(pp == target) && !(pp == sender)
                                        && AccountManager.getUserAccount(pp).hasSocialSpyEnabled())
                                .forEach(pp -> pp.sendMessage(SocialSpyFormat));
                    }

                    if (sender instanceof ProxiedPlayer) {
                        ReplyCommand.setReply((ProxiedPlayer) sender, target);
                    }

                    // TODO: Logger.
                }
            }
        }
    }
}

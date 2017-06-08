package dev.aura.bungeechat.command;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.utils.TimeUtil;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.MutingModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

import java.util.Optional;

public class TempMuteCommand extends BaseCommand {
    public TempMuteCommand(MutingModule mutingModule) {
        super("tempmute", mutingModule.getModuleSection().getStringList("aliases.tempmute"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_TEMPMUTE)) {
            if (args.length < 2) {
                sender.sendMessage(Message.INCORRECT_USAGE.get(sender, "/tempmute <player> <time>"));
            } else {
                Optional<BungeeChatAccount> targetAccount = BungeecordAccountManager.getAccount(args[0]);

                if (!targetAccount.isPresent()) {
                    sender.sendMessage(Message.PLAYER_NOT_FOUND.get());
                    return;
                }

                CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                if (targetAccount.get().isMuted()) {
                    sender.sendMessage(Message.MUTE_IS_MUTED.get());
                    return;
                }

                double timeAmount = TimeUtil.convertStringTimeToDouble(args[1]), currentTime = System.currentTimeMillis();
                java.sql.Timestamp timeStamp =  new java.sql.Timestamp((long) (currentTime + timeAmount));
                targetAccount.get().setMutedUntil(timeStamp);
                //TODO: Add Time Message
                sender.sendMessage(Message.TEMPMUTE.get(target));

            }
        }
    }

}

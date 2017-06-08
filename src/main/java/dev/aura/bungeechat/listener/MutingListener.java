package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;

public class MutingListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

        if (account.isMuted()) {
            if (ChatUtils.isCommand(e.getMessage())) {
                List<String> blockCommand = BungeecordModuleManager.MUTING_MODULE.getModuleSection().getStringList("blockedcommands");
                for (String s : blockCommand) {
                    if (e.getMessage().startsWith("/" + s + " ")) {
                        sender.sendMessage(Message.MUTED.get());
                        e.setCancelled(true);
                        return;
                    }
                }
            } else {

                if (account.getChannelType() == ChannelType.LOCAL && !BungeecordModuleManager.isModuleActive(BungeecordModuleManager.LOCAL_CHAT_MODULE)) {
                    if (!BungeecordModuleManager.MUTING_MODULE.getModuleSection().getBoolean("ignoreBukkitLocalChat"))
                        return;
                }

                if (account.getChannelType() == ChannelType.STAFF)
                    return;

                e.setCancelled(true);
                sender.sendMessage(Message.MUTED.get());
            }
        }
    }
}

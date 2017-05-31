package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LocalChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();
        if ((AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.LOCAL) || AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.LOCAL))
                && !ChatUtils.isCommand(message)) {
            e.setCancelled(true);
            //TODO: Send Local Message.
        }
    }
}

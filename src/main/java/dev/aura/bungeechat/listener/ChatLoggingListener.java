package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.chatlog.ChatLoggingManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatLoggingListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        BungeeChatAccount sender = new Account((ProxiedPlayer) e.getSender());
        String message = e.getMessage();

        if (ChatUtils.isCommand(message))
            ChatLoggingManager.logCommand(sender, message);
    }
}

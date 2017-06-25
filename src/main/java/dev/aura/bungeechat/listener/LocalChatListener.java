package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@SuppressWarnings("unused")
public class LocalChatListener implements Listener {
    private final boolean passToClientServer = BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection()
            .getBoolean("passToClientServer");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();
        String message = e.getMessage();

        if (ChatUtils.isCommand(message))
            return;

        if (account.getChannelType() == ChannelType.LOCAL) {
            e.setCancelled(!passToClientServer);
            MessagesService.sendLocalMessage(sender, message);
        }
    }
}

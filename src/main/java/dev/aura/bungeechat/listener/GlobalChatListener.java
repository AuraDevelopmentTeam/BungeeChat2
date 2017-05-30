package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.ModuleManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GlobalChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if (AccountManager.getUserAccount(sender).getChannelType().equals(ChannelType.GLOBAL)
                && !ChatUtils.isCommand(message)) {
            e.setCancelled(true);
            MessagesService.sendGlobalMessage(sender, message);

            return;
        }

        if (ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("Symbol.enabled")) {
            String symbol = ModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getString("Symbol.symbol");

            if (message.startsWith(symbol) && symbol.equals("/")) {
                e.setCancelled(true);
                MessagesService.sendGlobalMessage(sender, message.replaceFirst(symbol, ""));
            }
        }
    }
}

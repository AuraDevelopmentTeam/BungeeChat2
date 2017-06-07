package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
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

        if ((BungeecordAccountManager.getAccount(sender).get().getChannelType() == ChannelType.GLOBAL)
                && !ChatUtils.isCommand(message)) {

            if (Config.get().getBoolean("Settings.GlobalChat.Server-list.enabled")) {
                BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();
                if (!Config.get().getStringList("Settings.GlobalChat.Server-list.list").contains(account.getServerName())) {
                    sender.sendMessage(Message.NOT_IN_GLOBAL_SERVER.get());
                    return;
                }
            }

            e.setCancelled(true);
            MessagesService.sendGlobalMessage(sender, message);

            return;
        }

        if (BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getBoolean("Symbol.enabled")) {
            String symbol = BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection().getString("Symbol.symbol");

            if (message.startsWith(symbol) && !symbol.equals("/")) {

                if (Config.get().getBoolean("Settings.GlobalChat.Server-list.enabled")) {
                    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();
                    if (!Config.get().getStringList("Settings.GlobalChat.Server-list.list").contains(account.getServerName())) {
                        sender.sendMessage(Message.NOT_IN_GLOBAL_SERVER.get());
                        return;
                    }
                }

                e.setCancelled(true);
                MessagesService.sendGlobalMessage(sender, message.replaceFirst(symbol, ""));
            }
        }
    }

}

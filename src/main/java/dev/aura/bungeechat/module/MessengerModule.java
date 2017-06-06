package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.MessageCommand;
import dev.aura.bungeechat.command.ReplyCommand;
import dev.aura.bungeechat.command.ToggleCommand;
import net.md_5.bungee.api.ProxyServer;

public class MessengerModule extends Module {
    private MessageCommand messageCommand;
    private ReplyCommand replyCommand;
    private ToggleCommand toggleCommand;

    @Override
    public String getName() {
        return "Messenger";
    }

    @Override
    public void onEnable() {
        messageCommand = new MessageCommand(this);
        replyCommand = new ReplyCommand(this);
        toggleCommand = new ToggleCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), messageCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), replyCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), toggleCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(messageCommand);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(replyCommand);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(toggleCommand);
    }
}

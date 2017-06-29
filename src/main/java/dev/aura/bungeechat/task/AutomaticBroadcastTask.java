package dev.aura.bungeechat.task;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AutomaticBroadcastTask implements RepeatingTask {

    private static ArrayList<String> messages;
    private static int current = 0;

    @Override
    public void start() {
        if (BungeecordModuleManager.ALERT_MODULE.getModuleSection().getBoolean("automaticBroadcast.enabled")) {
            messages = (ArrayList<String>) BungeecordModuleManager.ALERT_MODULE.getModuleSection().getStringList("automaticBroadcast.messages");
            int interval = BungeecordModuleManager.ALERT_MODULE.getModuleSection().getInt("automaticBroadcast.interval");
            ProxyServer.getInstance().getScheduler().schedule(BungeeChat.getInstance(),
                    () -> {
                        MessagesService.sendToMatchingPlayers(PlaceHolderUtil.formatMessage(messages.get(current), new BungeeChatContext()));
                        next();
                    }, interval, interval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void next() {
        current++;
        if ((current + 1) > (messages.size())) {
            current = 0;
        }
    }

}

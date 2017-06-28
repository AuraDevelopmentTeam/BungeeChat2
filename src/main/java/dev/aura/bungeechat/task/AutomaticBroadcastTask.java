package dev.aura.bungeechat.task;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AutomaticBroadcastTask {

    private static ArrayList<String> messages;
    private static int current = 0;

    public static void startAutoBroadcast() {
        if (BungeecordModuleManager.ALERT_MODULE.getModuleSection().getBoolean("automaticBroadcast.enabled")) {
            messages = (ArrayList<String>) BungeecordModuleManager.ALERT_MODULE.getModuleSection().getStringList("automaticBroadcast.messages");
            int interval = BungeecordModuleManager.ALERT_MODULE.getModuleSection().getInt("automaticBroadcast.interval");
            startTask(interval);
        }
    }

    private static void startTask(int time) {
        ProxyServer.getInstance().getScheduler().schedule(BungeeChat.getInstance(),
                () -> {
                    MessagesService.sendToMatchingPlayers(PlaceHolderUtil.formatMessage(messages.get(current), new BungeeChatContext()));
                    next();
                }, time, time, TimeUnit.SECONDS);
    }

    private static void next() {
        current++;
        if ((current + 1) > (messages.size())) {
            current = 0;
        }
    }

}

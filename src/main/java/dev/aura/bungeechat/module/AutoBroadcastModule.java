package dev.aura.bungeechat.module;

import java.util.concurrent.TimeUnit;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.task.AutomaticBroadcastTask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

public class AutoBroadcastModule extends Module {
    private ScheduledTask automaticBroadcastTask;

    @Override
    public String getName() {
        return "AutoBroadcast";
    }

    @Override
    public void onEnable() {
        Configuration section = getModuleSection();

        int interval = section.getInt("interval");
        int delay = Math.min(10, interval / 2);

        automaticBroadcastTask = ProxyServer.getInstance().getScheduler().schedule(BungeeChat.getInstance(),
                new AutomaticBroadcastTask(section.getStringList("messages"), section.getBoolean("random")), delay,
                interval, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getScheduler().cancel(automaticBroadcastTask);
    }
}

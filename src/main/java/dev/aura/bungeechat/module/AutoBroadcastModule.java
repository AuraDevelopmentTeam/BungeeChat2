package dev.aura.bungeechat.module;

import com.typesafe.config.Config;
import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.task.AutomaticBroadcastTask;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class AutoBroadcastModule extends Module {
  private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

  private ScheduledTask automaticBroadcastTask;

  @Override
  public String getName() {
    return "AutoBroadcast";
  }

  @Override
  public void onEnable() {
    Config section = getModuleSection();

    long interval = section.getDuration("interval", TIME_UNIT);
    long delay = Math.min(10, interval / 2);

    automaticBroadcastTask =
        ProxyServer.getInstance()
            .getScheduler()
            .schedule(
                BungeeChat.getInstance(),
                new AutomaticBroadcastTask(
                    MessagesService.getServerListPredicate(section.getConfig("serverList")),
                    section.getStringList("messages"),
                    section.getBoolean("random")),
                delay,
                interval,
                TIME_UNIT);
  }

  @Override
  public void onDisable() {
    ProxyServer.getInstance().getScheduler().cancel(automaticBroadcastTask);
  }
}

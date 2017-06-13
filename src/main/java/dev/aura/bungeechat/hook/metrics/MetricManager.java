package dev.aura.bungeechat.hook.metrics;

import org.bstats.Metrics;

import dev.aura.bungeechat.BungeeChat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetricManager {
    public static void sendMetrics(BungeeChat plugin) {
        Metrics metrics = new BungeeChatMetrics(plugin);

        metrics.addCustomChart(new ModuleData());
    }
}

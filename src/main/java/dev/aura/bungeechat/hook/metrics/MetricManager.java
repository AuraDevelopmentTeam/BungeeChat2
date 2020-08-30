package dev.aura.bungeechat.hook.metrics;

import dev.aura.bungeechat.BungeeChat;
import lombok.experimental.UtilityClass;
import org.bstats.bungeecord.Metrics;

@UtilityClass
public class MetricManager {
  public static void sendMetrics(BungeeChat plugin) {
    Metrics metrics = new Metrics(plugin, 927);

    metrics.addCustomChart(new ModuleData());
    metrics.addCustomChart(new LanguageData());
  }
}

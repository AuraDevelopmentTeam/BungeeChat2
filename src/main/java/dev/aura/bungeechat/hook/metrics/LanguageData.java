package dev.aura.bungeechat.hook.metrics;

import dev.aura.bungeechat.config.Configuration;
import org.bstats.bungeecord.Metrics.SimplePie;

public class LanguageData extends SimplePie {
  public LanguageData() {
    super("languages", LanguageData::getLanguage);
  }
  
  private static String getLanguage() {
    return Configuration.get().getString("Language");
  }
}

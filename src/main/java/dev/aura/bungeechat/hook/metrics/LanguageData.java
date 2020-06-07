package dev.aura.bungeechat.hook.metrics;

import com.google.common.annotations.VisibleForTesting;
import dev.aura.bungeechat.config.Configuration;
import java.util.Locale;
import java.util.MissingResourceException;
import org.bstats.bungeecord.Metrics.SimplePie;

public class LanguageData extends SimplePie {
  public LanguageData() {
    super("languages", LanguageData::getLanguage);
  }

  private static String getLanguage() {
    final String configLanguage = Configuration.get().getString("Language");

    return isValidLangauge(configLanguage) ? configLanguage : "custom";
  }

  @VisibleForTesting
  static boolean isValidLangauge(String lang) {
    String[] parts = lang.split("_", 3);

    if (parts.length != 2) return false;

    try {
      final Locale loc = new Locale(parts[0], parts[1]);

      return (loc.getISO3Language() != null) && (loc.getISO3Country() != null);
    } catch (MissingResourceException e) {
      return false;
    }
  }
}

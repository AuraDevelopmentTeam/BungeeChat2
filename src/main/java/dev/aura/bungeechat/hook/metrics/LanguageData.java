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

    return isValidLanguage(configLanguage) ? configLanguage : "custom";
  }

  /**
   * Checks if a given language string is valid. Supports xx_yy and xx_yy_z, where xx is the
   * language code, yy is the country code and z is any positive integer
   *
   * @param lang language string to check
   * @return If the passed language string is correct
   */
  @VisibleForTesting
  static boolean isValidLanguage(String lang) {
    String[] parts = lang.split("_", 4);

    if (parts.length < 2 || parts.length > 3) return false;

    try {
      if (parts.length == 3 && !parts[2].chars().allMatch(Character::isDigit)) return false;

      final Locale loc = new Locale(parts[0], parts[1]);

      return (loc.getISO3Language() != null) && (loc.getISO3Country() != null);
    } catch (MissingResourceException e) {
      return false;
    }
  }
}

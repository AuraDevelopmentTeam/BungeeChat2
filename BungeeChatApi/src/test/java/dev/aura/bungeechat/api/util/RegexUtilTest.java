package dev.aura.bungeechat.api.util;

import static org.junit.Assert.assertTrue;

import dev.aura.bungeechat.api.utils.RegexUtil;
import java.util.regex.Pattern;
import org.junit.Test;

public class RegexUtilTest {
  @Test
  public void leetSpeakPatterns() {
    for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
      String letter = leet.getLetter();
      Pattern pattern = Pattern.compile(leet.getPattern());

      for (String replacement : leet.getLeetAlternatives()) {
        assertTrue(
            "Replacement \""
                + replacement
                + "\" doesn't match as a alternative for "
                + letter
                + "!",
            pattern.matcher(replacement).matches());
      }
    }
  }

  @Test
  public void leetSpeakWildcard() {
    for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
      String letter = leet.getLetter();
      Pattern pattern =
          RegexUtil.parseWildcardToPattern(
              leet.getLetter(), Pattern.CASE_INSENSITIVE, false, true, false, false);

      for (String replacement : leet.getLeetAlternatives()) {
        assertTrue(
            "Replacement \""
                + replacement
                + "\" doesn't match as a alternative for "
                + letter
                + "!",
            pattern.matcher(replacement).matches());
      }
    }
  }

  @Test
  public void specialChars() {
    final String testString = "dhäöüßÄÖÜàÎ学校hg";
    Pattern pattern;

    // Iterate over all possible flag combinations
    for (int i = 0; i < (1 << 4); ++i) {
      pattern =
          RegexUtil.parseWildcardToPattern(
              testString,
              Pattern.CASE_INSENSITIVE,
              (i & (1 << 0)) > 0,
              (i & (1 << 1)) > 0,
              (i & (1 << 2)) > 0,
              (i & (1 << 3)) > 0);

      assertTrue("Pattern doesn't match itself!", pattern.matcher(testString).matches());
    }
  }

  // TODO: More tests!
}

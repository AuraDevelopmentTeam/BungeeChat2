package dev.aura.bungeechat.api.util;

import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

import dev.aura.bungeechat.api.utils.RegexUtil;

public class RegexUtilTest {
    @Test
    public void leetSpeakPatterns() {
        for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
            String letter = leet.getLetter();
            Pattern pattern = Pattern.compile(leet.getPattern());

            for (String replacement : leet.getLeetAlternatives()) {
                assertTrue("Replacement \"" + replacement + "\" doesn't match as a alternative for " + letter + "!",
                        pattern.matcher(replacement).matches());
            }
        }
    }

    @Test
    public void leetSpeakWildcard() {
        for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
            String letter = leet.getLetter();
            Pattern pattern = RegexUtil.parseWildcardToPattern(leet.getLetter(), Pattern.CASE_INSENSITIVE, false, true,
                    false, false);

            for (String replacement : leet.getLeetAlternatives()) {
                assertTrue("Replacement \"" + replacement + "\" doesn't match as a alternative for " + letter + "!",
                        pattern.matcher(replacement).matches());
            }
        }
    }

    // TODO: More tests!
}

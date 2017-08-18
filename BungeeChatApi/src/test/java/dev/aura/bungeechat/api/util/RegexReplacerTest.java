package dev.aura.bungeechat.api.util;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import org.junit.Test;

import dev.aura.bungeechat.api.utils.RegexReplacer;

public class RegexReplacerTest {
    private static final String PATTERN_SIMPLE = "foo";
    private static final String REPLACEMENT_SIMPLE = "bar";
    private static final String PATTERN_COMPLEX = "<([A-Z][A-Z0-9]*)\\b[^>]*>(.*?)</\\1>";
    private static final String REPLACEMENT_COMPLEX = "<$1_yay>$2</$1_yay>";

    @Test
    public void complexPatternMatching() {
        final RegexReplacer pattern = new RegexReplacer(PATTERN_COMPLEX, Pattern.CASE_INSENSITIVE, REPLACEMENT_COMPLEX);

        assertEquals("Complex matching", "<f0o_yay>bar</f0o_yay>", pattern.apply("<f0o tags>bar</f0o>"));
    }

    @Test
    public void equalsTest() {
        final RegexReplacer pattern = new RegexReplacer(Pattern.compile(PATTERN_SIMPLE), REPLACEMENT_SIMPLE);
        final RegexReplacer string = new RegexReplacer(PATTERN_SIMPLE, REPLACEMENT_SIMPLE);

        assertEquals("Two pattern strings not equal", pattern.getPatternStr(), string.getPatternStr());
        assertEquals("Two replacement strings not equal", pattern.getReplacement(), string.getReplacement());
        assertEquals("Two patterns not equal", pattern.getPattern().toString(), string.getPattern().toString());
    }

    @Test
    public void flagsTest() {
        final RegexReplacer caseSensitive = new RegexReplacer(PATTERN_SIMPLE, REPLACEMENT_SIMPLE);
        final RegexReplacer caseInsensitive = new RegexReplacer(PATTERN_SIMPLE, Pattern.CASE_INSENSITIVE,
                REPLACEMENT_SIMPLE);

        final String in = "FoO";
        final String replaced = REPLACEMENT_SIMPLE;
        final String notReplaced = in;

        assertEquals("caseSensitive original", notReplaced, caseSensitive.apply(in));
        assertEquals("caseSensitive reflagged", replaced, caseSensitive.apply(in, Pattern.CASE_INSENSITIVE));
        assertEquals("caseInsensitive original", replaced, caseInsensitive.apply(in));
        assertEquals("caseInsensitive reflagged", notReplaced, caseInsensitive.apply(in, 0));

        // Repeat to check cache
        assertEquals("caseSensitive original", notReplaced, caseSensitive.apply(in));
        assertEquals("caseSensitive reflagged", replaced, caseSensitive.apply(in, Pattern.CASE_INSENSITIVE));
        assertEquals("caseInsensitive original", replaced, caseInsensitive.apply(in));
        assertEquals("caseInsensitive reflagged", notReplaced, caseInsensitive.apply(in, 0));
    }
}

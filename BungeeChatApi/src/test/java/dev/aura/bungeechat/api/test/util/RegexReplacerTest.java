package dev.aura.bungeechat.api.test.util;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import org.junit.Test;

import dev.aura.bungeechat.api.utils.RegexReplacer;

public class RegexReplacerTest {
    private static final String PATTERN = "foo";
    private static final String REPLACEMENT = "bar";
    
    @Test
    public void equalsTest() {
        RegexReplacer pattern = new RegexReplacer(Pattern.compile(PATTERN), REPLACEMENT);
        RegexReplacer string = new RegexReplacer(PATTERN, REPLACEMENT);

        assertEquals("Two pattern strings not equal", pattern.getPatternStr(), string.getPatternStr());
        assertEquals("Two replacement strings not equal", pattern.getReplacement(), string.getReplacement());
        assertEquals("Two objects not equal", pattern, string);
    }
}

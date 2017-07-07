package dev.aura.bungeechat.api.test.util;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import dev.aura.bungeechat.api.utils.RegexUtil;

public class RegexUtilTest {
    @BeforeClass
    public static void precompileRegex() {
        Object dummy = RegexUtil.LEET_PATTERNS;
        dummy.hashCode();
    }

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

    @Test
    public void testLeetSpeakPatterns() {
        leetSpeakPatterns();
    }

    @Test
    public void testLeetSpeakWildcard() {
        leetSpeakWildcard();
    }

    @Test
    public void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder().include(this.getClass().getName() + ".*").mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS).warmupTime(TimeValue.seconds(1)).warmupIterations(2)
                .measurementTime(TimeValue.seconds(1)).measurementIterations(2).threads(2).forks(1)
                .shouldFailOnError(true).shouldDoGC(true).build();

        new Runner(opt).run();
    }

    @Benchmark
    public void benchmarkLeetSpeakPatterns() {
        leetSpeakPatterns();
    }

    @Benchmark
    public void benchmarkLeetSpeakWildcard() {
        leetSpeakWildcard();
    }

    // TODO: More tests!
}

package dev.aura.bungeechat.api.jmh.util;

import java.util.regex.Pattern;

import org.openjdk.jmh.annotations.Benchmark;

import dev.aura.bungeechat.api.utils.RegexUtil;

public class RegexUtilBenchmark {
    @Benchmark
    public void leetSpeakPatterns() {
        for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
            Pattern pattern = Pattern.compile(leet.getPattern());

            for (String replacement : leet.getLeetAlternatives()) {
                pattern.matcher(replacement).matches();
            }
        }
    }

    @Benchmark
    public void leetSpeakWildcard() {
        for (RegexUtil.LeetSpeakPattern leet : RegexUtil.LEET_PATTERNS.values()) {
            Pattern pattern = RegexUtil.parseWildcardToPattern(leet.getLetter(), Pattern.CASE_INSENSITIVE, false, true,
                    false, false);

            for (String replacement : leet.getLeetAlternatives()) {
                pattern.matcher(replacement).matches();
            }
        }
    }

    // TODO: More benchmarks!
}

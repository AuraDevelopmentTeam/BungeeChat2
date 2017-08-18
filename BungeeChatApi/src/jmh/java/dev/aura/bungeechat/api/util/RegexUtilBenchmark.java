package dev.aura.bungeechat.api.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import dev.aura.bungeechat.api.utils.RegexUtil;
import net.moznion.random.string.RandomStringGenerator;

public class RegexUtilBenchmark {
    @Benchmark
    public void compilation(CompilationState state, Blackhole blackhole) {
        Pattern pattern = RegexUtil.parseWildcardToPattern(CompilationState.pattern, Pattern.CASE_INSENSITIVE,
                state.freeMatching, state.leetSpeak, state.ignoreSpaces, state.ignoreDuplicateLetters);

        blackhole.consume(pattern);
    }

    @Benchmark
    public void matching(MatchingState state, Blackhole blackhole) {
        Matcher match = state.pattern.matcher(state.stringToMatch);

        blackhole.consume(match);
    }

    @State(Scope.Thread)
    public static class CompilationState {
        @Param({ "false", "true" })
        public boolean freeMatching;
        @Param({ "false", "true" })
        public boolean leetSpeak;
        @Param({ "false", "true" })
        public boolean ignoreSpaces;
        @Param({ "false", "true" })
        public boolean ignoreDuplicateLetters;

        public static final String pattern = "abcdefghijklmnopqrstuvwxyz*ABCDEFGHIJKLMNOPQRSTUVWXYZ?";
    }

    @State(Scope.Thread)
    public static class MatchingState extends CompilationState {
        @Param({ "10", "100", "1000" })
        public int length;

        public Pattern pattern;
        public String stringToMatch;
        public RandomStringGenerator randStrGen = new RandomStringGenerator(new Random(0));

        @Setup(Level.Trial)
        public void compilePattern() {
            pattern = RegexUtil.parseWildcardToPattern(CompilationState.pattern, Pattern.CASE_INSENSITIVE, freeMatching,
                    leetSpeak, ignoreSpaces, ignoreDuplicateLetters);
            stringToMatch = randStrGen.generateByRegex(".{" + length + '}');
        }
    }

    // TODO: More benchmarks!
}

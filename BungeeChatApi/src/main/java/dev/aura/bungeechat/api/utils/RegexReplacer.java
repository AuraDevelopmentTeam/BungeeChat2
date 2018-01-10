package dev.aura.bungeechat.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class RegexReplacer {
  private final String patternStr;
  private final Pattern pattern;
  private final String replacement;

  @Getter(AccessLevel.NONE)
  private final int defaultFlags;

  @Getter(AccessLevel.NONE)
  private final Map<Integer, Pattern> patternCache;

  public RegexReplacer(Pattern pattern, String replacement) {
    patternStr = pattern.pattern();
    this.pattern = pattern;
    this.replacement = replacement;

    defaultFlags = pattern.flags();
    patternCache = new HashMap<>();

    patternCache.put(defaultFlags, pattern);
  }

  public RegexReplacer(String pattern, String replacement) {
    this(pattern, 0, replacement);
  }

  public RegexReplacer(String pattern, int regexFlags, String replacement) {
    this(Pattern.compile(pattern, regexFlags), replacement);
  }

  public String apply(String input) {
    return replaceAll(pattern, input);
  }

  public String apply(String input, int flags) {
    if (!patternCache.containsKey(flags)) {
      patternCache.put(flags, Pattern.compile(patternStr, flags));
    }

    return replaceAll(patternCache.get(flags), input);
  }

  private String replaceAll(Pattern pattern, String input) {
    return pattern.matcher(input).replaceAll(replacement);
  }
}

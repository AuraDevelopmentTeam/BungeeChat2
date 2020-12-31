package dev.aura.bungeechat.api.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {
  private static final Pattern TOKENIZER =
      Pattern.compile(
          "(?<!^\\\\)(?<![^\\\\]\\\\)(?<!\\\\x)(?<!\\\\x\\{)(?<!\\\\x\\{[0-9a-f])(?<!\\\\x\\{[0-9a-f]{2})(?<!\\\\x\\{[0-9a-f]{3})(?<!\\\\x\\{[0-9a-f]{4})",
          Pattern.CASE_INSENSITIVE);
  private static final RegexReplacer REGEX_ESCAPER =
      new RegexReplacer("[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)‌​\\?\\*\\+\\.\\>]", "\\\\$0");
  private static final RegexReplacer WILDCARD_STAR = new RegexReplacer("^\\\\\\*$", ".*?");
  private static final RegexReplacer WILDCARD_QUESTION_MARK = new RegexReplacer("^\\\\\\?$", ".?");

  /**
   * A map containing all used leet speak alternatives. The key is a string of the uppercase letter.
   * <br>
   * Current Mapping:
   *
   * <pre>
   * A: 4 /\ @ /-\ ^ aye (L Д
   * B: I3 8 13 |3 &szlig; !3 (3 /3 )3 |-] j3 6
   * C: [ &cent; { &lt; ( &copy;
   * D: ) |) (| [) I&gt; |&gt; ? T) I7 cl |} &gt; |]
   * E: 3 &amp; &pound; &euro; &euml; [- |=-
   * F: |= &fnof; |# ph /= v
   * G: &amp; 6 (_+ 9 C- gee (?, [, {, &lt;- (.
   * H: # /-/ [-] ]-[ )-( (-) :-: |~| |-| ]~[ }{ !-! 1-1 \-/ I+I /-\
   * I: 1 [] | ! eye 3y3 ][
   * J: ,_| _| ._| ._] _] ,_] ] ; 1
   * K: &gt;| |&lt; /&lt; 1&lt; |c |( |{
   * L: 1 &pound; 7 |_ |
   * M: /\/\ /V\ JVI [V] []V[] |\/| ^^ &lt;\/&gt; {V} (v) (V) |V| nn IVI |\|\ ]\/[ 1^1 ITI JTI
   * N: ^/ |\| /\/ [\] &lt;\&gt; {\} |V /V И ^ ท
   * O: 0 Q () oh []
   * P: &lt;&gt; &Oslash; |* |o |&ordm; ? |^ |&gt; |&quot; 9 []D |&deg; |7
   * Q: (_,) 9 ()_ 2 0_ &lt;| &amp;
   * R: I2 |` |~ |? /2 |^ lz |9 2 12 &reg; [z Я .- |2 |-
   * S: 5 $ z &sect; ehs es 2
   * T: 7 + -|- '][' &dagger; &quot;|&quot; ~|~
   * U: (_) |_| v L| &micro; บ
   * V: \/ |/ \|
   * W: \/\/ VV \N '// \\' \^/ (n) \V/ \X/ \|/ \_|_/ \_:_/ Ш Щ uu 2u \\//\\// พ v&sup2;
   * X: &gt;&lt; Ж }{ ecks &times; ? )( ][
   * Y: j `/ Ч 7 \|/ &yen; \//
   * Z: 2 7_ -/_ % &gt;_ s ~/_ -\_ -|_
   * </pre>
   */
  public static final Map<String, LeetSpeakPattern> LEET_PATTERNS =
      Stream.of(
              new LeetSpeakPattern("A", "4", "/\\", "@", "/-\\", "^", "aye", "(L", "Д"),
              new LeetSpeakPattern(
                  "B", "I3", "8", "13", "|3", "ß", "!3", "(3", "/3", ")3", "|-]", "j3", "6"),
              new LeetSpeakPattern("C", "[", "¢", "{", "<", "(", "©"),
              new LeetSpeakPattern(
                  "D", ")", "|)", "(|", "[)", "I>", "|>", "?", "T)", "I7", "cl", "|}", ">", "|]"),
              new LeetSpeakPattern("E", "3", "&", "£", "€", "ë", "[-", "|=-"),
              new LeetSpeakPattern("F", "|=", "ƒ", "|#", "ph", "/=", "v"),
              new LeetSpeakPattern(
                  "G", "&", "6", "(_+", "9", "C-", "gee", "(?,", "[,", "{,", "<-", "(."),
              new LeetSpeakPattern(
                  "H", "#", "/-/", "[-]", "]-[", ")-(", "(-)", ":-:", "|~|", "|-|", "]~[", "}{",
                  "!-!", "1-1", "\\-/", "I+I", "/-\\"),
              new LeetSpeakPattern("I", "1", "[]", "|", "!", "eye", "3y3", "]["),
              new LeetSpeakPattern("J", ",_|", "_|", "._|", "._]", "_]", ",_]", "]", ";", "1"),
              new LeetSpeakPattern("K", ">|", "|<", "/<", "1<", "|c", "|(", "|{"),
              new LeetSpeakPattern("L", "1", "£", "7", "|_", "|"),
              new LeetSpeakPattern(
                  "M", "/\\/\\", "/V\\", "JVI", "[V]", "[]V[]", "|\\/|", "^^", "<\\/>", "{V}",
                  "(v)", "(V)", "|V|", "nn", "IVI", "|\\|\\", "]\\/[", "1^1", "ITI", "JTI"),
              new LeetSpeakPattern(
                  "N", "^/", "|\\|", "/\\/", "[\\]", "<\\>", "{\\}", "|V", "/V", "И", "^", "ท"),
              new LeetSpeakPattern("O", "0", "Q", "()", "oh", "[]"),
              new LeetSpeakPattern(
                  "P", "<>", "Ø", "|*", "|o", "|º", "?", "|^", "|>", "|\"", "9", "[]D", "|°", "|7"),
              new LeetSpeakPattern("Q", "(_,)", "9", "()_", "2", "0_", "<|", "&"),
              new LeetSpeakPattern(
                  "R", "I2", "|`", "|~", "|?", "/2", "|^", "lz", "|9", "2", "12", "®", "[z", "Я",
                  ".-", "|2", "|-"),
              new LeetSpeakPattern("S", "5", "$", "z", "§", "ehs", "es", "2"),
              new LeetSpeakPattern("T", "7", "+", "-|-", "']['", "†", "\"|\"", "~|~"),
              new LeetSpeakPattern("U", "(_)", "|_|", "v", "L|", "µ", "บ"),
              new LeetSpeakPattern("V", "\\/", "|/", "\\|"),
              new LeetSpeakPattern(
                  "W",
                  "\\/\\/",
                  "VV",
                  "\\N",
                  "'//",
                  "\\\\'",
                  "\\^/",
                  "(n)",
                  "\\V/",
                  "\\X/",
                  "\\|/",
                  "\\_|_/",
                  "\\_:_/",
                  "Ш",
                  "Щ",
                  "uu",
                  "2u",
                  "\\\\//\\\\//",
                  "พ",
                  "v²"),
              new LeetSpeakPattern("X", "><", "Ж", "}{", "ecks", "×", "?", ")(", "]["),
              new LeetSpeakPattern("Y", "j", "`/", "Ч", "7", "\\|/", "¥", "\\//"),
              new LeetSpeakPattern("Z", "2", "7_", "-/_", "%", ">_", "s", "~/_", "-\\_", "-|_"))
          .collect(Collectors.toMap(LeetSpeakPattern::getLetter, pattern -> pattern));

  /**
   * Escapes a string into a regex that matches this string literally.<br>
   * <b>Note:</b> This method does not use the cheap <code>\Q...\E</code> variant that {@link
   * Pattern#quote(String)} uses!
   *
   * @param literal The string to be escaped
   * @return A regex string that matches the passed string literally.
   */
  public static String escapeRegex(String literal) {
    StringBuilder b = new StringBuilder();

    for (char c : REGEX_ESCAPER.apply(literal).toCharArray()) {
      if (c >= 0x20 && c <= 0x7E) {
        b.append(c);
      } else {
        b.append(String.format("\\x{%04X}", (int) c));
      }
    }

    return b.toString();
  }

  /**
   * This method turns a string that might contain wildcards into a case insensitive regex {@link
   * Pattern}.<br>
   * A string can either be treated as normal wildcard string or a regex string.<br>
   * <br>
   * <b>Wildcard String</b><br>
   * All <code>*</code>s will match zero to infinite many characters.<br>
   * All <code>?</code>s will match zero or one character.<br>
   * <br>
   * <b>Regex String</b><br>
   * If a passed string starts with <code>R=</code>, then the rest will be interpreted as an actual
   * regex.<br>
   * <br>
   * This is the same as {@link RegexUtil#parseWildcardToPattern(String, int)
   * StringUtil.parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE)}
   *
   * @param wildcard The string that is to be parsed.
   * @return A regex {@link Pattern} that has been compiled from the string.
   * @throws PatternSyntaxException If the regex syntax is invalid.
   * @see RegexUtil#parseWildcardToPattern(String, int)
   */
  public static Pattern parseWildcardToPattern(String wildcard) throws PatternSyntaxException {
    return parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE);
  }

  /**
   * This method turns a string that might contain wildcards into a regex {@link Pattern} with the
   * specified flags.<br>
   * A string can either be treated as normal wildcard string or a regex string.<br>
   * <br>
   * <b>Wildcard String</b><br>
   * All <code>*</code>s will match zero to infinite many characters.<br>
   * All <code>?</code>s will match zero or one character.<br>
   * <br>
   * <b>Regex String</b><br>
   * If a passed string starts with <code>R=</code>, then the rest will be interpreted as an actual
   * regex.<br>
   * <br>
   * This is the same as {@link RegexUtil#parseWildcardToPattern(String, int, boolean, boolean,
   * boolean, boolean) StringUtil.parseWildcardToPattern(wildcard, flags, false, false, false,
   * false)}
   *
   * @param wildcard The string that is to be parsed.
   * @param flags Regex flags. See: {@link Pattern#compile(String, int)}
   * @return A regex {@link Pattern} that has been compiled from the string and has the flags set.
   * @throws PatternSyntaxException If the regex syntax is invalid.
   * @see RegexUtil#parseWildcardToPattern(String, int, boolean, boolean, boolean, boolean)
   */
  public static Pattern parseWildcardToPattern(String wildcard, int flags)
      throws PatternSyntaxException {
    return parseWildcardToPattern(wildcard, flags, false, false, false, false);
  }

  /**
   * This method turns a string that might contain wildcards into a regex {@link Pattern} with the
   * specified flags.<br>
   * A string can either be treated as normal wildcard string or a regex string.<br>
   * <br>
   * <b>Wildcard String</b><br>
   * All <code>*</code>s will match zero to infinite many characters.<br>
   * All <code>?</code>s will match zero or one character.<br>
   * <br>
   * Additionally the following options may be applied <i>(the string <code>test</code> will be used
   * an an example here)</i>:
   *
   * <ul>
   *   <li><u>freeMatching</u>: The match will not be bound to word borders. So <code>abctestabc
   *       </code> is still a (partial) match (Only <code>test</code> itself will be matched
   *       though).
   *   <li><u>leetSpeak</u>: Expands all letters to also match leet speak: <a
   *       href="https://qntm.org/l33t">https://qntm.org/l33t</a>. So <code>+3$t</code> is still a
   *       match. See {@link RegexUtil#LEET_PATTERNS}. This flags also implied case insensitive
   *       matching for wildcard strings!
   *   <li><u>ignoreSpaces</u>: This allows infinitely many whitespaces between the letters. So
   *       <code>t es       t</code> is still a match.
   *   <li><u>ignoreDuplicateLetters</u>: This allows letters to be duplicated infinitely many
   *       times. So <code>tteeeeeeesttt</code> is still a match.
   * </ul>
   *
   * <b>Regex String</b><br>
   * If a passed string starts with <code>R=</code>, then the rest will be interpreted as an actual
   * regex.<br>
   *
   * @param wildcard The string that is to be parsed.
   * @param flags Regex flags. See: {@link Pattern#compile(String, int)}
   * @param freeMatching Determines if the match has to be a free standing word.
   * @param leetSpeak Determines if leet speak also matches. Like a 5 for a S.
   * @param ignoreSpaces Determines if spaces may be ignored.
   * @param ignoreDuplicateLetters Determines if letters may be duplicated (infinitely many times)
   *     and the pattern still matches.
   * @return A regex {@link Pattern} that has been compiled from the string and has the flags set
   *     and options applied if it is not a regex string.
   * @throws PatternSyntaxException If the regex syntax is invalid.
   * @see Pattern#compile(String, int)
   */
  public static Pattern parseWildcardToPattern(
      String wildcard,
      int flags,
      boolean freeMatching,
      boolean leetSpeak,
      boolean ignoreSpaces,
      boolean ignoreDuplicateLetters)
      throws PatternSyntaxException {
    if (wildcard.startsWith("R=")) return Pattern.compile(wildcard.substring(2), flags);
    else {
      flags |= Pattern.CASE_INSENSITIVE;

      Stream<String> stream = TOKENIZER.splitAsStream(escapeRegex(wildcard));

      if (ignoreDuplicateLetters) {
        stream =
            stream.map(
                token -> {
                  if (token.length() > 1) return token;
                  else return token + "+";
                });
      }

      if (leetSpeak) {
        stream =
            stream.map(
                token -> {
                  String firstLetter = Character.toString(token.charAt(0)).toUpperCase();

                  if (LEET_PATTERNS.containsKey(firstLetter)) {
                    token = LEET_PATTERNS.get(firstLetter).apply(token);
                  }

                  return token;
                });
      }

      stream = stream.map(WILDCARD_STAR::apply).map(WILDCARD_QUESTION_MARK::apply);

      String delimiter = ignoreSpaces ? "\\s*" : "";
      String start = freeMatching ? "" : "(?<=^|\\s|\\p{Punct})";
      String end = freeMatching ? "" : "(?=\\p{Punct}|\\s|$)";

      return Pattern.compile(stream.collect(Collectors.joining(delimiter, start, end)), flags);
    }
  }

  /**
   * A Helper class for leet speak patterns.<br>
   * It produces a regex that can be used for matching.
   */
  public static class LeetSpeakPattern {
    @Getter private final String letter;
    private final Pattern letterPattern;
    private final List<String> leetAlternatives;
    @Getter private final String pattern;
    @Getter private final String escapedPattern;

    public LeetSpeakPattern(String letter, String... leetAlternatives) {
      this.letter = letter;
      this.letterPattern = Pattern.compile(escapeRegex(letter), Pattern.CASE_INSENSITIVE);
      this.leetAlternatives = Arrays.asList(leetAlternatives);

      List<String> processingList = new LinkedList<>();

      processingList.add(letter);
      processingList.addAll(Arrays.asList(leetAlternatives));

      pattern =
          processingList.stream()
              .map(RegexUtil::escapeRegex)
              .collect(Collectors.joining("|", "(?:", ")"));
      escapedPattern = Matcher.quoteReplacement(pattern);
    }

    public List<String> getLeetAlternatives() {
      return Collections.unmodifiableList(leetAlternatives);
    }

    public String apply(String input) {
      return letterPattern.matcher(input).replaceAll(escapedPattern);
    }
  }
}

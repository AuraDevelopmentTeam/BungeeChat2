package dev.aura.bungeechat.api.utils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {
    private static Pattern TOKENIZER = Pattern.compile("(?<!\\\\)");
    private static RegexReplacer REGEX_ESCAPER = new RegexReplacer(
            "[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)‌​\\?\\*\\+\\.\\>]", "\\\\$0");
    private static RegexReplacer WILDCARD_STAR = new RegexReplacer("^\\\\\\*$", ".*?");
    private static RegexReplacer WILDCARD_QUESTIONMARK = new RegexReplacer("^\\\\\\?$", ".?");

    public static String escapeRegex(String literal) {
        return REGEX_ESCAPER.apply(literal);
    }

    /**
     * This method turns a string that might contain wildcards into a case
     * insensitive regex {@link Pattern}.<br>
     * A string can either be treated as normal wildcard string or a regex
     * string.<br>
     * <br>
     * <b>Wildcard String</b><br>
     * All <code>*</code>s will match zero to infinite many characters.<br>
     * All <code>?</code>s will match zero or one character.<br>
     * <br>
     * <b>Regex String</b><br>
     * If a passed string starts with <code>R=</code>, then the rest will be
     * interpreted as an actual regex.<br>
     * <br>
     * This is the same as {@link RegexUtil#parseWildcardToPattern(String, int)
     * StringUtil.parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE)}
     *
     * @param wildcard
     *            The string that is to be parsed.
     * @return A regex {@link Pattern} that has been compiled from the string.
     * @throws PatternSyntaxException
     *             If the regex syntax is invalid.
     * @see RegexUtil#parseWildcardToPattern(String, int)
     */
    public static Pattern parseWildcardToPattern(String wildcard) throws PatternSyntaxException {
        return parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE);
    }

    /**
     * This method turns a string that might contain wildcards into a regex
     * {@link Pattern} with the specified flags.<br>
     * A string can either be treated as normal wildcard string or a regex
     * string.<br>
     * <br>
     * <b>Wildcard String</b><br>
     * All <code>*</code>s will match zero to infinite many characters.<br>
     * All <code>?</code>s will match zero or one character.<br>
     * <br>
     * <b>Regex String</b><br>
     * If a passed string starts with <code>R=</code>, then the rest will be
     * interpreted as an actual regex.<br>
     * <br>
     * This is the same as
     * {@link RegexUtil#parseWildcardToPattern(String, int, boolean, boolean, boolean, boolean)
     * StringUtil.parseWildcardToPattern(wildcard, flags, false, false, false)}
     *
     * @param wildcard
     *            The string that is to be parsed.
     * @param flags
     *            Regex flags. See: {@link Pattern#compile}
     * @return A regex {@link Pattern} that has been compiled from the string
     *         and has the flags set.
     * @throws PatternSyntaxException
     *             If the regex syntax is invalid.
     * @see RegexUtil#parseWildcardToPattern(String, int, boolean, boolean,
     *      boolean, boolean)
     */
    public static Pattern parseWildcardToPattern(String wildcard, int flags) throws PatternSyntaxException {
        return parseWildcardToPattern(wildcard, flags, false, false, false, false);
    }

    /**
     * This method turns a string that might contain wildcards into a regex
     * {@link Pattern} with the specified flags.<br>
     * A string can either be treated as normal wildcard string or a regex
     * string.<br>
     * <br>
     * <b>Wildcard String</b><br>
     * All <code>*</code>s will match zero to infinite many characters.<br>
     * All <code>?</code>s will match zero or one character.<br>
     * <br>
     * Additionally the following options may be applied <i>(the string
     * <code>test</code> will be used an an example here)</i>:
     * <ul>
     * <li><u>freeMatching</u>: The match will not be bound to word borders. So
     * <code>abctestabc</code> is still a (partial) match (Only
     * <code>test</code> itself will be matched though).
     * <li><u>leetSpeak</u>: Expands all letters to also match leet speak:
     * <a href="https://qntm.org/l33t">https://qntm.org/l33t</a>. So
     * <code>+3$t</code> is still a match.
     * <li><u>ignoreSpaces</u>: This allows infinitely many whitespaces between
     * the letters. So <code>t es       t</code> is still a match.
     * <li><u>ignoreDuplicateLetters</u>: This allows letters to be duplicated
     * infinitely many times. So <code>tteeeeeeesttt</code> is still a match.
     * </ul>
     * <b>Regex String</b><br>
     * If a passed string starts with <code>R=</code>, then the rest will be
     * interpreted as an actual regex.<br>
     * 
     * @param wildcard
     *            The string that is to be parsed.
     * @param flags
     *            Regex flags. See: {@link Pattern#compile}
     * @param freeMatching
     *            Determines if the match has to be a free standing word.
     * @param leetSpeak
     *            Determines if leet speak also matches. Like a 5 for a S.
     * @param ignoreSpaces
     *            Determines if spaces may be ignored.
     * @param ignoreDuplicateLetters
     *            Determines if letters may be duplicated (infinitely many
     *            times) and the pattern still matches.
     * @return A regex {@link Pattern} that has been compiled from the string
     *         and has the flags set and options applied if it is not a regex
     *         string.
     * @throws PatternSyntaxException
     *             If the regex syntax is invalid.
     * @see Pattern#compile
     */
    public static Pattern parseWildcardToPattern(String wildcard, int flags, boolean freeMatching, boolean leetSpeak,
            boolean ignoreSpaces, boolean ignoreDuplicateLetters) throws PatternSyntaxException {
        if (wildcard.startsWith("R="))
            return Pattern.compile(wildcard.substring(2), flags);
        else {
            Stream<String> stream = TOKENIZER.splitAsStream(escapeRegex(wildcard));

            if (ignoreDuplicateLetters) {
                stream = stream.map(token -> {
                    if (token.length() > 1)
                        return token;
                    else
                        return token + "+";
                });
            }

            if (leetSpeak) {
                // TODO: Find leet patterns and apply them.
            }

            stream = stream.map(WILDCARD_STAR::apply).map(WILDCARD_QUESTIONMARK::apply);

            String delimiter = ignoreSpaces ? "\\s*" : "";
            String around = freeMatching ? "" : "\\b";

            return Pattern.compile(stream.collect(Collectors.joining(delimiter, around, around)), flags);
        }
    }
}

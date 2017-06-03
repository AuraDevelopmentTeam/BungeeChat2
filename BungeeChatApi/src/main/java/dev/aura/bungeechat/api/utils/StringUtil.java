package dev.aura.bungeechat.api.utils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    /**
     * Regex: <code>\\(\*|\?)</code><br>
     * Mathces either <code>\*</code> or <code>\?</code> and captures the char
     * so it can be used in the replacement.
     * 
     * @see StringUtil#WILDCARDS_REPLACEMENT
     */
    private static Pattern WILDCARDS = Pattern.compile("\\\\(\\*|\\?)");
    /**
     * Replaces the wildcard match with the appropriate regex.
     * 
     * @see StringUtil#WILDCARDS
     */
    private static String WILDCARDS_REPLACEMENT = ".$1";

    /**
     * This method turns a string that might contain wildcards into a case
     * insensitive regex {@link Pattern}.<br>
     * A string can either be treated as normal wirldcard string or a regex
     * string.<br>
     * <br>
     * <b>Wildcard String</b><br>
     * All <code>*</code>s will match zero to infinite many characters.<br>
     * All <code>?</code>s will match zero or one character.<br>
     * <br>
     * <b>Regex String</b><br>
     * If a passed string starts with <code>R=</code>, then the rest will be
     * interpreted as an actual regex.
     * 
     * This is the same as {@link StringUtil#parseWildcardToPattern(String, int)
     * StringUtil.parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE)}
     *
     * @param wildcard
     *            The string that is to be parsed.
     * @return A regex {@link Pattern} that has been compiled from the string.
     * @throws PatternSyntaxException
     *             If the regex syntax is invalid.
     * @see StringUtil#parseWildcardToPattern(String, int)
     */
    public static Pattern parseWildcardToPattern(String wildcard) throws PatternSyntaxException {
        return parseWildcardToPattern(wildcard, Pattern.CASE_INSENSITIVE);
    }

    /**
     * This method turns a string that might contain wildcards into a regex
     * {@link Pattern} with the specified flags.<br>
     * A string can either be treated as normal wirldcard string or a regex
     * string.<br>
     * <br>
     * <b>Wildcard String</b><br>
     * All <tt>*</tt>s will match zero to infinite many characters.<br>
     * All <tt>?</tt>s will match zero or one character.<br>
     * <br>
     * <b>Regex String</b><br>
     * If a passed string starts with <tt>R=</tt>, then the rest will be
     * interpreted as an actual regex.
     *
     * @param wildcard
     *            The string that is to be parsed.
     * @param flags
     *            Regex flags. See: {@link Pattern#compile}
     * @return A regex {@link Pattern} that has been compiled from the string.
     * @throws PatternSyntaxException
     *             If the regex syntax is invalid.
     * @see StringUtil#parseWildcardToPattern(String)
     * @see Pattern#compile
     */
    public static Pattern parseWildcardToPattern(String wildcard, int flags) throws PatternSyntaxException {
        if (wildcard.startsWith("R="))
            return Pattern.compile(wildcard.substring(2), flags);
        else
            return Pattern.compile(WILDCARDS.matcher(Pattern.quote(wildcard)).replaceAll(WILDCARDS_REPLACEMENT), flags);
    }
}

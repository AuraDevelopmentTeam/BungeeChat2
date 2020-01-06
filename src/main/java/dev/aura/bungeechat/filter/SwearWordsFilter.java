package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.utils.RegexUtil;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SwearWordsFilter implements BungeeChatFilter {
  private final List<Pattern> swearWords;
  private final String replacement;

  public SwearWordsFilter(
      List<String> swearWords,
      String replacement,
      boolean freeMatching,
      boolean leetSpeak,
      boolean ignoreSpaces,
      boolean ignoreDuplicateLetters) {
    this.swearWords =
        swearWords.stream()
            .map(
                word ->
                    RegexUtil.parseWildcardToPattern(
                        word,
                        Pattern.CASE_INSENSITIVE,
                        freeMatching,
                        leetSpeak,
                        ignoreSpaces,
                        ignoreDuplicateLetters))
            .collect(Collectors.toList());
    this.replacement = replacement;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) {
    if (PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_SWEAR)) return message;

    for (Pattern p : swearWords) {
      message = p.matcher(message).replaceAll(replacement);
    }

    return message;
  }

  @Override
  public int getPriority() {
    return FilterManager.SWEAR_FILTER_PRIORITY;
  }
}

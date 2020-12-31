package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.api.utils.RegexUtil;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvertisingFilter implements BungeeChatFilter {
  /**
   * Regex from <a href=
   * "https://gist.github.com/dperini/729294">https://gist.github.com/dperini/729294</a>. <br>
   * Slightly modified. Allowed dropping of the protocol. So <code>google.com</code> still matches
   * and removed the start and end anchors!
   */
  private static final Pattern url =
      Pattern.compile(
          "(?:(?:https?|ftp)://)?(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?",
          Pattern.CASE_INSENSITIVE);

  private final Predicate<String> whitelisted;
  private final boolean noPermissions;

  public AdvertisingFilter(List<String> whitelisted) {
    this(whitelisted, false);
  }

  public AdvertisingFilter(List<String> whitelisted, boolean noPermissions) {
    this.whitelisted =
        whitelisted.stream()
            .map(
                wildcard ->
                    RegexUtil.parseWildcardToPattern(
                            wildcard, Pattern.CASE_INSENSITIVE, true, false, false, false)
                        .asPredicate())
            .reduce(Predicate::or)
            .orElse(x -> false);
    this.noPermissions = noPermissions;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
    if (!noPermissions
        && PermissionManager.hasPermission(sender, Permission.BYPASS_ANTI_ADVERTISEMENT))
      return message;

    Matcher matches = url.matcher(message);
    boolean matchOk;
    String match;

    while (matches.find()) {
      match = matches.group();
      matchOk = whitelisted.test(match);

      if (!matchOk)
        throw new ExtendedBlockMessageException(Messages.ANTI_ADVERTISE, sender, message);
    }

    return message;
  }

  @Override
  public int getPriority() {
    return FilterManager.ADVERTISING_FILTER_PRIORITY;
  }
}

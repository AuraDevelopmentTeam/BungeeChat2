package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import java.util.Map;

public class SymbolSubstitutionFilter implements BungeeChatFilter {
  private final Map<String, String> replacementMapping;

  public SymbolSubstitutionFilter(Map<String, String> replacements) {
    replacementMapping = replacements;
  }

  @Override
  public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
    String finalMessage = message;

    for (Map.Entry<String, String> entry : replacementMapping.entrySet()) {
      finalMessage = finalMessage.replace(entry.getKey(), entry.getValue());
    }

    return finalMessage;
  }

  @Override
  public int getPriority() {
    return FilterManager.SYMBOL_SUBSTITUTION_PRIORITY;
  }
}

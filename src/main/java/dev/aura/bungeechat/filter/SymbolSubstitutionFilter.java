package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;

import java.util.HashMap;
import java.util.Map;

public class SymbolSubstitutionFilter implements BungeeChatFilter {

    private static Map<String, String> replacementMapping = new HashMap<>();
    public  SymbolSubstitutionFilter(Map<String, String> replacements) {
        replacementMapping = replacements;
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        String finalmessage = message;
        for (Map.Entry<String, String> entry : replacementMapping.entrySet()) {
            finalmessage = message.replace(entry.getKey(), entry.getValue());
        }
        return finalmessage;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int compareTo(BungeeChatFilter other) {
        return 0;
    }
}

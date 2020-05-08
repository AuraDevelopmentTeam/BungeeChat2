package dev.aura.bungeechat.filter;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;

public class SymbolSubstitutionFilter implements BungeeChatFilter {
    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        String finalMessage = message.replaceAll("-->","→");
        return finalMessage;
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

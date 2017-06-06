package dev.aura.bungeechat.listener;

import java.util.stream.Stream;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.module.ModuleManager;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class TabCompletionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent e) {
        if (e.isCancelled())
            return;

        String partialPlayerName = e.getCursor().toLowerCase();
        int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');

        if (lastSpaceIndex >= 0) {
            partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
        }
        
        Stream<BungeeChatAccount> stream = AccountManager.getAccountsForPartialName(partialPlayerName).stream();
        
        if(ModuleManager.isModuleActive(ModuleManager.VANISHER_MODULE)) {
            stream = stream.filter(account -> !account.isVanished());
        }
        
        stream.forEach(account -> e.getSuggestions().add(account.getName()));
    }
}

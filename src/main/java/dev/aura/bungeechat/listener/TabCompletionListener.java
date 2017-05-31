package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.account.AccountManager;
import dev.aura.bungeechat.module.ModuleManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (ModuleManager.isModuleActive(ModuleManager.VANISHER_MODULE)) {
                if(!AccountManager.getUserAccount(p).isVanished()) {
                    if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                        e.getSuggestions().add(p.getName());
                    }
                }
            } else {
                if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                    e.getSuggestions().add(p.getName());
                }
            }
        }
    }
}

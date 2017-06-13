package dev.aura.bungeechat.listener;

import java.util.stream.Stream;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class TabCompletionListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTabComplete(TabCompleteEvent e) {
        if (e.isCancelled())
            return;

        if (!PermissionManager.hasPermission((CommandSender) e.getSender(), Permission.USE_TAB_COMPLETE))
            return;

        String partialPlayerName = e.getCursor();
        int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');

        if (lastSpaceIndex >= 0) {
            partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
        }

        Stream<BungeeChatAccount> stream = AccountManager.getAccountsForPartialName(partialPlayerName).stream();

        if (ModuleManager.isModuleActive(BungeecordModuleManager.VANISHER_MODULE)
                && !PermissionManager.hasPermission((CommandSender) e.getSender(), Permission.COMMAND_VANISH_VIEW)) {
            stream = stream.filter(account -> !account.isVanished());
        }

        stream.forEach(account -> e.getSuggestions().add(account.getName()));
    }
}

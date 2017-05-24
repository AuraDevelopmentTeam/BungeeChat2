package dev.aura.bungeechat.placeholder;

import java.util.UUID;

import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Context extends BungeeChatContext {
    public Context(ProxiedPlayer player) {
        super(new Account(player));
    }
    
    public Context(UUID player) {
        super(new Account(player));
    }
    
    public Context(ProxiedPlayer sender, ProxiedPlayer target) {
        super(new Account(sender), new Account(target));
    }
    
    public Context(UUID sender, UUID target) {
        super(new Account(sender), new Account(target));
    }
}

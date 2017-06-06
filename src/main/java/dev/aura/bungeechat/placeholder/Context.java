package dev.aura.bungeechat.placeholder;

import java.util.UUID;

import dev.aura.bungeechat.account.Account;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Context extends BungeeChatContext {
    public Context() {
        super();
    }

    public Context(BungeeChatAccount player) {
        super(player);
    }

    public Context(BungeeChatAccount sender, BungeeChatAccount target) {
        super(sender, target);
    }

    public Context(ProxiedPlayer player) {
        super(new Account(player));
    }

    public Context(ProxiedPlayer sender, ProxiedPlayer target) {
        super(new Account(sender), new Account(target));
    }

    public Context(UUID player) {
        super(new Account(player));
    }

    public Context(UUID sender, UUID target) {
        super(new Account(sender), new Account(target));
    }

    public Context(CommandSender player) {
        super();

        if (player instanceof ProxiedPlayer) {
            setSender(new Account((ProxiedPlayer) player));
        }
    }

    public Context(CommandSender player, String message) {
        this(player);

        setMessage(message);
    }

    public Context(CommandSender sender, CommandSender target) {
        super();

        if (sender instanceof ProxiedPlayer) {
            setSender(new Account((ProxiedPlayer) sender));
        }
        if (target instanceof ProxiedPlayer) {
            setTarget(new Account((ProxiedPlayer) target));
        }
    }

    public Context(CommandSender sender, CommandSender target, String message) {
        this(sender, target);

        setMessage(message);
    }
}

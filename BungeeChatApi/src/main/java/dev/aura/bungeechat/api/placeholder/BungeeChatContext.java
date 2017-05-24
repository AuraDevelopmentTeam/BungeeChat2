package dev.aura.bungeechat.api.placeholder;

import java.util.Optional;
import java.util.function.Predicate;

import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.Getter;

public class BungeeChatContext {
    public static final Predicate<BungeeChatContext> HAS_PLAYER = context -> context.hasPlayer();
    public static final Predicate<BungeeChatContext> HAS_SENDER = context -> context.hasSender();
    public static final Predicate<BungeeChatContext> HAS_TARGET = context -> context.hasTarget();

    public static final Predicate<BungeeChatContext> HAS_NO_PLAYER = HAS_PLAYER.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_SENDER = HAS_SENDER.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_TARGET = HAS_TARGET.negate();

    @Getter
    protected final Optional<BungeeChatAccount> player;
    @Getter
    protected final Optional<BungeeChatAccount> sender;
    @Getter
    protected final Optional<BungeeChatAccount> target;

    public BungeeChatContext(BungeeChatAccount player) {
        this.player = Optional.of(player);
        sender = Optional.empty();
        target = Optional.empty();
    }

    public BungeeChatContext(BungeeChatAccount sender, BungeeChatAccount target) {
        player = Optional.empty();
        this.sender = Optional.of(sender);
        this.target = Optional.of(target);
    }

    public boolean hasPlayer() {
        return player.isPresent();
    }

    public boolean hasSender() {
        return sender.isPresent();
    }

    public boolean hasTarget() {
        return target.isPresent();
    }
}

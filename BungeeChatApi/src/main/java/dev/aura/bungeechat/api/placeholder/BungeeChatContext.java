package dev.aura.bungeechat.api.placeholder;

import java.util.Optional;
import java.util.function.Predicate;

import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
public class BungeeChatContext {
    public static final Predicate<BungeeChatContext> HAS_PLAYER = context -> context.hasPlayer();
    public static final Predicate<BungeeChatContext> HAS_SENDER = context -> context.hasSender();
    public static final Predicate<BungeeChatContext> HAS_TARGET = context -> context.hasTarget();
    public static final Predicate<BungeeChatContext> HAS_MESSAGE = context -> context.hasMessage();

    public static final Predicate<BungeeChatContext> HAS_NO_PLAYER = HAS_PLAYER.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_SENDER = HAS_SENDER.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_TARGET = HAS_TARGET.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_MESSAGE = HAS_MESSAGE.negate();

    private Optional<BungeeChatAccount> player;
    private Optional<BungeeChatAccount> sender;
    private Optional<BungeeChatAccount> target;
    private Optional<String> message;

    public BungeeChatContext() {
        player = Optional.empty();
        sender = Optional.empty();
        target = Optional.empty();
        message = Optional.empty();
    }

    public BungeeChatContext(BungeeChatAccount player) {
        this();

        this.player = Optional.of(player);
    }

    public BungeeChatContext(BungeeChatAccount sender, BungeeChatAccount target) {
        this();

        this.sender = Optional.of(sender);
        this.target = Optional.of(target);
    }

    public BungeeChatContext(BungeeChatAccount sender, BungeeChatAccount target, String message) {
        this(sender, target);

        this.message = Optional.of(message);
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

    public boolean hasMessage() {
        return message.isPresent();
    }

    @Tolerate
    public void setPlayer(BungeeChatAccount player) {
        setPlayer(Optional.ofNullable(player));
    }

    @Tolerate
    public void setSender(BungeeChatAccount sender) {
        setSender(Optional.ofNullable(sender));
    }

    @Tolerate
    public void setTarget(BungeeChatAccount target) {
        setTarget(Optional.ofNullable(target));
    }

    @Tolerate
    public void setMessage(String message) {
        setMessage(Optional.ofNullable(message));
    }
}

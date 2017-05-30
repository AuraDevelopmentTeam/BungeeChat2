package dev.aura.bungeechat.api.placeholder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import dev.aura.bungeechat.api.exception.InvalidContextException;
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

    private static final Map<Predicate<BungeeChatContext>, String> requirementsNameCache = new HashMap<>(8);

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

    @SafeVarargs
    public final void require(Predicate<? super BungeeChatContext>... requirements) throws InvalidContextException {
        for (Predicate<? super BungeeChatContext> requirement : requirements) {
            if (!requirement.test(this)) {
                if (requirementsNameCache.containsKey(requirement))
                    throw new InvalidContextException(requirementsNameCache.get(requirement));

                throw new InvalidContextException("Context does not meet all requirements!");
            }
        }
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

    static {
        final int modifers = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

        for (Field field : BungeeChatContext.class.getDeclaredFields()) {
            try {
                if ((field.getModifiers() & modifers) == modifers) {
                    @SuppressWarnings("unchecked")
                    Predicate<BungeeChatContext> filter = (Predicate<BungeeChatContext>) field.get(null);

                    requirementsNameCache.put(filter, "Context does not meet requirement " + field.getName() + "!");
                }

            } catch (IllegalArgumentException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

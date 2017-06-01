package dev.aura.bungeechat.api.placeholder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * This class represents a context for a message or other chat related
 * action.<br>
 * It may contain the acting player (sender), the receiver (target), the message
 * and possibly more in the future.
 */
@Getter
@Setter
public class BungeeChatContext {
    public static final Predicate<BungeeChatContext> HAS_SENDER = BungeeChatContext::hasSender;
    public static final Predicate<BungeeChatContext> HAS_TARGET = BungeeChatContext::hasTarget;
    public static final Predicate<BungeeChatContext> HAS_MESSAGE = BungeeChatContext::hasMessage;

    public static final Predicate<BungeeChatContext> HAS_NO_SENDER = HAS_SENDER.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_TARGET = HAS_TARGET.negate();
    public static final Predicate<BungeeChatContext> HAS_NO_MESSAGE = HAS_MESSAGE.negate();

    private static final Map<Predicate<BungeeChatContext>, String> requirementsNameCache = new HashMap<>(6);

    private Optional<BungeeChatAccount> sender;
    private Optional<BungeeChatAccount> target;
    private Optional<String> message;

    public BungeeChatContext() {
        sender = Optional.empty();
        target = Optional.empty();
        message = Optional.empty();
    }

    public BungeeChatContext(BungeeChatAccount sender) {
        this();

        this.sender = Optional.of(sender);
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

    /**
     * This method is used to verify if a context is valid. All passed
     * requirements must be true in order for this test to pass. If it fails an
     * {@link InvalidContextError} is thrown.<br>
     * It is recommended to use the static predefined {@link Predicate}s like
     * {@link BungeeChatContext#HAS_SENDER}.
     *
     * @param requirements
     *            An array of requirements which all must be true for this
     *            context to be valid.
     * @throws InvalidContextError
     *             This assertion error gets thrown when one (or more)
     *             requirements are not met. If it is a predefined
     *             {@link Predicate} from {@link BungeeChatContext} the name
     *             will be included in the error message. If not a generic
     *             message will be put.
     * @see BungeeChatContext#HAS_SENDER
     * @see BungeeChatContext#HAS_TARGET
     * @see BungeeChatContext#HAS_MESSAGE
     * @see BungeeChatContext#HAS_NO_SENDER
     * @see BungeeChatContext#HAS_NO_TARGET
     * @see BungeeChatContext#HAS_NO_MESSAGE
     */
    @SafeVarargs
    public final void require(Predicate<? super BungeeChatContext>... requirements) throws InvalidContextError {
        for (Predicate<? super BungeeChatContext> requirement : requirements) {
            if (!requirement.test(this)) {
                if (requirementsNameCache.containsKey(requirement))
                    throw new InvalidContextError(requirementsNameCache.get(requirement));

                throw new InvalidContextError("Context does not meet all requirements!");
            }
        }
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

    // Fill the requirementsNameCache
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
                e.printStackTrace();
            }
        }
    }
}

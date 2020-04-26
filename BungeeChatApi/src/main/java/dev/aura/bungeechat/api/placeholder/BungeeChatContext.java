package dev.aura.bungeechat.api.placeholder;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * This class represents a context for a message or other chat related action.<br>
 * It may contain the acting player (sender), the receiver (target), the message and possibly more
 * in the future.
 */
@Data
public class BungeeChatContext {
  /**
   * Predefined Predicate to check if a context has a sender.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_SENDER = BungeeChatContext::hasSender;
  /**
   * Predefined Predicate to check if a context has a target.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_TARGET = BungeeChatContext::hasTarget;
  /**
   * Predefined Predicate to check if a context has a message.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_MESSAGE = BungeeChatContext::hasMessage;
  /**
   * Predefined Predicate to check if a context has a channel.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_CHANNEL = BungeeChatContext::hasChannel;
  /**
   * Predefined Predicate to check if a context has a server.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_SERVER = BungeeChatContext::hasServer;

  /**
   * Predefined Predicate to check if a context does not have a sender.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_NO_SENDER = HAS_SENDER.negate();
  /**
   * Predefined Predicate to check if a context does not have a target.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_NO_TARGET = HAS_TARGET.negate();
  /**
   * Predefined Predicate to check if a context does not have a message.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_NO_MESSAGE = HAS_MESSAGE.negate();
  /**
   * Predefined Predicate to check if a context does not have a channel.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_NO_CHANNEL = HAS_CHANNEL.negate();
  /**
   * Predefined Predicate to check if a context does not have a server.
   *
   * @see BungeeChatContext#require(Predicate...)
   */
  public static final Predicate<BungeeChatContext> HAS_NO_SERVER = HAS_SERVER.negate();

  private static final Map<Predicate<BungeeChatContext>, String> requirementsNameCache =
      new HashMap<>(8);

  private Optional<BungeeChatAccount> sender;
  private Optional<BungeeChatAccount> target;
  private Optional<String> message;
  private Optional<String> channel;
  private Optional<String> server;

  public BungeeChatContext() {
    sender = Optional.empty();
    target = Optional.empty();
    message = Optional.empty();
    channel = Optional.empty();
    server = Optional.empty();
  }

  public BungeeChatContext(BungeeChatAccount sender) {
    this();

    this.sender = Optional.ofNullable(sender);
  }

  public BungeeChatContext(String message) {
    this();

    this.message = Optional.ofNullable(message);
  }

  public BungeeChatContext(BungeeChatAccount sender, String message) {
    this(sender);

    this.message = Optional.ofNullable(message);
  }

  public BungeeChatContext(BungeeChatAccount sender, BungeeChatAccount target) {
    this(sender);

    this.target = Optional.ofNullable(target);
  }

  public BungeeChatContext(BungeeChatAccount sender, BungeeChatAccount target, String message) {
    this(sender, target);

    this.message = Optional.ofNullable(message);
  }

  public BungeeChatContext(BungeeChatAccount sender, String message, String server) {
    this(sender, message);

    this.server = Optional.ofNullable(server);
  }

  /**
   * This method is used to verify if a context is valid. All passed requirements must be true in
   * order for this test to pass. If it fails an {@link InvalidContextError} is thrown.<br>
   * It is recommended to use the static predefined {@link Predicate}s like {@link
   * BungeeChatContext#HAS_SENDER}.
   *
   * @param requirements An array of requirements which all must be true for this context to be
   *     valid.
   * @throws InvalidContextError This assertion error gets thrown when one (or more) requirements
   *     are not met. If it is a predefined {@link Predicate} from {@link BungeeChatContext} the
   *     name will be included in the error message. If not a generic message will be put.
   * @see BungeeChatContext#HAS_SENDER
   * @see BungeeChatContext#HAS_TARGET
   * @see BungeeChatContext#HAS_MESSAGE
   * @see BungeeChatContext#HAS_CHANNEL
   * @see BungeeChatContext#HAS_NO_SENDER
   * @see BungeeChatContext#HAS_NO_TARGET
   * @see BungeeChatContext#HAS_NO_MESSAGE
   * @see BungeeChatContext#HAS_NO_CHANNEL
   */
  @SafeVarargs
  public final void require(Predicate<? super BungeeChatContext>... requirements)
      throws InvalidContextError {
    for (Predicate<? super BungeeChatContext> requirement : requirements) {
      if (!requirement.test(this)) {
        if (requirementsNameCache.containsKey(requirement))
          throw new InvalidContextError(requirementsNameCache.get(requirement));

        throw new InvalidContextError();
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

  public boolean hasChannel() {
    return channel.isPresent();
  }

  public boolean hasServer() {
    return server.isPresent();
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

  @Tolerate
  public void setChannel(String channel) {
    setChannel(Optional.ofNullable(channel));
  }

  @Tolerate
  public void setServer(String server) {
    setServer(Optional.ofNullable(server));
  }

  // Fill the requirementsNameCache
  static {
    final int modifers = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

    for (Field field : BungeeChatContext.class.getDeclaredFields()) {
      try {
        if ((field.getModifiers() & modifers) == modifers) {
          @SuppressWarnings("unchecked")
          Predicate<BungeeChatContext> filter = (Predicate<BungeeChatContext>) field.get(null);

          requirementsNameCache.put(
              filter, "Context does not meet requirement " + field.getName() + "!");
        }

      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}

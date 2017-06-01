package dev.aura.bungeechat.api.placeholder;

/**
 * This exception is used to indicate that a passed context did not fulfill the
 * requirements placed on it.<br>
 * It is adviced that when throwing this exception to provide information on
 * what exact requirements were not fulfilled as that simpliefies debugging!
 */
public class InvalidContextException extends RuntimeException {
    private static final long serialVersionUID = -7826893842156075019L;

    /**
     * @see RuntimeException#RuntimeException()
     */
    public InvalidContextException() {
        super();
    }

    /**
     * @see RuntimeException#RuntimeException(String message)
     */
    public InvalidContextException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable cause)
     */
    public InvalidContextException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String message, Throwable cause)
     */
    public InvalidContextException(String message, Throwable cause) {
        super(message, cause);
    }
}

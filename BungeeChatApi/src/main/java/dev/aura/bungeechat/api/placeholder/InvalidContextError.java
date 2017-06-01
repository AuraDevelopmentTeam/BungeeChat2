package dev.aura.bungeechat.api.placeholder;

/**
 * This exception is used to indicate that a passed context did not fulfill the
 * requirements placed on it.<br>
 * It is adviced that when throwing this exception to provide information on
 * what exact requirements were not fulfilled as that simpliefies debugging!
 */
public class InvalidContextError extends AssertionError {
    private static final long serialVersionUID = -7826893842156075019L;

    /**
     * @see AssertionError#AssertionError()
     */
    public InvalidContextError() {
        super();
    }

    /**
     * @see AssertionError#AssertionError(String message)
     */
    public InvalidContextError(String message) {
        super(message);
    }

    /**
     * @see AssertionError#AssertionError(Throwable cause)
     */
    public InvalidContextError(Throwable cause) {
        super(cause);
    }

    /**
     * @see AssertionError#AssertionError(String message, Throwable cause)
     */
    public InvalidContextError(String message, Throwable cause) {
        super(message, cause);
    }
}

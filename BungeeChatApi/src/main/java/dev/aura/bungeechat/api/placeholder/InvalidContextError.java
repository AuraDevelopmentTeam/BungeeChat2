package dev.aura.bungeechat.api.placeholder;

/**
 * This exception is used to indicate that a passed context did not fulfill the
 * requirements placed on it.<br>
 * It is advised that when throwing this exception to provide information on
 * what exact requirements were not fulfilled as that simplifies debugging!
 */
public class InvalidContextError extends AssertionError {
    private static final long serialVersionUID = -7826893842156075019L;

    /**
     * Constructs a new error to indicate that a certain assertion or requirement
     * of an {@link BungeeChatContext} failed.
     * 
     * @param message
     *            A message specifying what is wrong about the context, if
     *            possible
     */
    public InvalidContextError(String message) {
        super(message);
    }

    /**
     * Equivalent to calling InvalidContextError("Context does not meet all
     * requirements!")
     * 
     * @see InvalidContextError#InvalidContextError(String)
     */
    public InvalidContextError() {
        this("Context does not meet all requirements!");
    }
}

package dev.aura.bungeechat.api.exception;

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

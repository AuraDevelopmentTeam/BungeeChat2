package dev.aura.bungeechat.api.filter;

/**
 * This exception is not really an exception. It is used in filters to indicate
 * that the message should not be sent and instead that the sending user should
 * be warned with the passed message.
 */
public class BlockMessageException extends Exception {
    private static final long serialVersionUID = -2629372445468034714L;

    /**
     * Construct a new {@link BlockMessageException} to indicate that the
     * message should be blocked and not sent but instead the sending user
     * should be warned with the message passed.
     * 
     * @param message
     *            The warning displayed to the user.
     */
    public BlockMessageException(String message) {
        super(message);
    }
}

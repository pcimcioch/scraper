package scraper.exception;

/**
 * Exception indicating that resource couldn't not be found.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8504440463162665413L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(String msgFormat, Throwable cause, Object... args) {
        super(String.format(msgFormat, args), cause);
    }
}

package scraper.exception;

/**
 * Exception indicating that unexpected exception was cought, and it wasn't handled.
 */
public class UnexpectedException extends RuntimeException {

    private static final long serialVersionUID = -7124227246427519128L;

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(String msg) {
        super(msg);
    }

    public UnexpectedException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    public UnexpectedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnexpectedException(String msgFormat, Throwable cause, Object... args) {
        super(String.format(msgFormat, args), cause);
    }
}

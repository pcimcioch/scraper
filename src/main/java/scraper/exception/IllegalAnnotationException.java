package scraper.exception;

/**
 * Exception indicating illegal annotation placement or usage.
 */
public class IllegalAnnotationException extends RuntimeException {

    private static final long serialVersionUID = 7523738815035368747L;

    public IllegalAnnotationException() {
        super();
    }

    public IllegalAnnotationException(String msg) {
        super(msg);
    }

    public IllegalAnnotationException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    public IllegalAnnotationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalAnnotationException(String msgFormat, Throwable cause, Object... args) {
        super(String.format(msgFormat, args), cause);
    }
}

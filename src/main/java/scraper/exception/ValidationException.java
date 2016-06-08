package scraper.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -6776472626295249831L;

    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ValidationException(String msgFormat, Throwable cause, Object... args) {
        super(String.format(msgFormat, args), cause);
    }
}

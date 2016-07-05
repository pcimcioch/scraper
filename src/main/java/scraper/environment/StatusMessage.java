package scraper.environment;

/**
 * Simple status message class wrapping messages.
 */
public class StatusMessage {

    private String message;

    protected StatusMessage() {
    }

    public StatusMessage(String message) {
        this.message = message;
    }

    public StatusMessage(String messageFormat, Object... args) {
        this.message = String.format(messageFormat, args);
    }

    public String getMessage() {
        return message;
    }
}

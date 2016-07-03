package scraper.environment;

/**
 * Simple status message class wrapping messages.
 */
public class StatusMessage {

    private String message;

    public StatusMessage() {
    }

    public StatusMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

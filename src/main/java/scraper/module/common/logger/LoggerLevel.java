package scraper.module.common.logger;

/**
 * Type representing different levels of the logger.
 */
public enum LoggerLevel {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4);

    private final int order;

    LoggerLevel(int order) {
        this.order = order;
    }

    public int order() {
        return order;
    }
}

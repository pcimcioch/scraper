package scraper.module.common.logger;

import java.util.logging.Level;

/**
 * Type representing different levels of the logger.
 */
public enum LoggerLevel {
    TRACE(0, Level.FINE),
    INFO(1, Level.INFO),
    WARNING(2, Level.WARNING),
    ERROR(3, Level.SEVERE);

    private final Level commonLevel;

    private final int order;

    LoggerLevel(int order, Level commonLevel) {
        this.order = order;
        this.commonLevel = commonLevel;
    }

    public Level commonLevel() {
        return commonLevel;
    }

    public int order() {
        return order;
    }
}

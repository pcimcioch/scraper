package scraper.module.common.logger;

import java.util.logging.Level;

public enum LoggerLevel {
    TRACE(Level.FINE),
    INFO(Level.INFO),
    WARNING(Level.WARNING),
    ERROR(Level.SEVERE);

    private final Level commonLevel;

    LoggerLevel(Level commonLevel) {
        this.commonLevel = commonLevel;
    }

    public Level commonLevel() {
        return commonLevel;
    }
}

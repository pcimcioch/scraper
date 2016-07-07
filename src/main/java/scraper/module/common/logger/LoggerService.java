package scraper.module.common.logger;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;

import java.util.Date;

/**
 * Service used for logging logger messages.
 */
@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LoggerService {

    private final ModuleContext moduleContext;

    private final LogEntryDsRepository logRepository;

    private final LoggerLevel consoleThreshold;

    private final LoggerLevel dbThreshold;

    @Autowired
    public LoggerService(ModuleContext moduleContext, LogEntryDsRepository logRepository, @Value("${logger.console:TRACE}") LoggerLevel consoleThreshold,
            @Value("${logger.db:WARNING}") LoggerLevel dbThreshold) {
        this.moduleContext = moduleContext;
        this.logRepository = logRepository;
        this.consoleThreshold = consoleThreshold;
        this.dbThreshold = dbThreshold;
    }

    public void log(LoggerLevel level, String message) {
        doLog(level, message, null);
    }

    public void log(LoggerLevel level, String messageFormat, Object... args) {
        doLog(level, String.format(messageFormat, args), null);
    }

    public void log(LoggerLevel level, String message, Throwable cause) {
        doLog(level, message, cause);
    }

    public void log(LoggerLevel level, String messageFormat, Throwable cause, Object... args) {
        doLog(level, String.format(messageFormat, args), cause);
    }

    public void trace(String message) {
        doLog(LoggerLevel.TRACE, message, null);
    }

    public void trace(String messageFormat, Object... args) {
        doLog(LoggerLevel.TRACE, String.format(messageFormat, args), null);
    }

    public void trace(String message, Throwable cause) {
        doLog(LoggerLevel.TRACE, message, cause);
    }

    public void trace(String messageFormat, Throwable cause, Object... args) {
        doLog(LoggerLevel.TRACE, String.format(messageFormat, args), cause);
    }

    public void debug(String message) {
        doLog(LoggerLevel.DEBUG, message, null);
    }

    public void debug(String messageFormat, Object... args) {
        doLog(LoggerLevel.DEBUG, String.format(messageFormat, args), null);
    }

    public void debug(String message, Throwable cause) {
        doLog(LoggerLevel.DEBUG, message, cause);
    }

    public void debug(String messageFormat, Throwable cause, Object... args) {
        doLog(LoggerLevel.DEBUG, String.format(messageFormat, args), cause);
    }

    public void info(String message) {
        doLog(LoggerLevel.INFO, message, null);
    }

    public void info(String messageFormat, Object... args) {
        doLog(LoggerLevel.INFO, String.format(messageFormat, args), null);
    }

    public void info(String message, Throwable cause) {
        doLog(LoggerLevel.INFO, message, cause);
    }

    public void info(String messageFormat, Throwable cause, Object... args) {
        doLog(LoggerLevel.INFO, String.format(messageFormat, args), cause);
    }

    public void warn(String message) {
        doLog(LoggerLevel.WARNING, message, null);
    }

    public void warn(String messageFormat, Object... args) {
        doLog(LoggerLevel.WARNING, String.format(messageFormat, args), null);
    }

    public void warn(String message, Throwable cause) {
        doLog(LoggerLevel.WARNING, message, cause);
    }

    public void warn(String messageFormat, Throwable cause, Object... args) {
        doLog(LoggerLevel.WARNING, String.format(messageFormat, args), cause);
    }

    public void error(String message) {
        doLog(LoggerLevel.ERROR, message, null);
    }

    public void error(String messageFormat, Object... args) {
        doLog(LoggerLevel.ERROR, String.format(messageFormat, args), null);
    }

    public void error(String message, Throwable cause) {
        doLog(LoggerLevel.ERROR, message, cause);
    }

    public void error(String messageFormat, Throwable cause, Object... args) {
        doLog(LoggerLevel.ERROR, String.format(messageFormat, args), cause);
    }

    private void doLog(LoggerLevel level, String message, Throwable cause) {
        ModuleDetails moduleDetails = moduleContext.getModuleDetails();

        if (consoleThreshold != null && level.order() >= consoleThreshold.order()) {
            logToConsole(moduleDetails.getLogger(), level, message, cause);
        }

        if (dbThreshold != null && level.order() >= dbThreshold.order()) {
            logRepository.save(new LogEntryDs(level, moduleDetails.getModule(), moduleDetails.getInstance(), new Date(), message));
        }
    }

    private void logToConsole(Log logger, LoggerLevel level, String message, Throwable cause) {
        switch (level) {
            case TRACE:
                logger.trace(message, cause);
                break;
            case DEBUG:
                logger.debug(message, cause);
                break;
            case INFO:
                logger.info(message, cause);
                break;
            case WARNING:
                logger.warn(message, cause);
                break;
            case ERROR:
                logger.error(message, cause);
                break;
        }
    }
}

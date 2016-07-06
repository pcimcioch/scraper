package scraper.module.common.logger;

import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO support configuring which level of logs should be displayed and which should be saved to db
    @Autowired
    public LoggerService(ModuleContext moduleContext, LogEntryDsRepository logRepository) {
        this.moduleContext = moduleContext;
        this.logRepository = logRepository;
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

        moduleDetails.getLogger().log(level.commonLevel(), message, cause);
        logRepository.save(new LogEntryDs(level, moduleDetails.getModule(), moduleDetails.getInstance(), new Date(), message));
    }
}

package scraper.module.common.logger.view;

import scraper.module.common.logger.LogEntryDs;
import scraper.module.common.logger.LoggerLevel;
import scraper.util.Utils;

import java.util.Date;

/**
 * Json DTO describing {@link LogEntryDs}.
 * <p>
 * Used only for {@link LogEntryDs} -> {@link LogEntryJsonDto} conversions.
 */
public class LogEntryJsonDto {

    private LoggerLevel level;

    private String module;

    private String instance;

    private Date date;

    private String message;

    public LogEntryJsonDto(LoggerLevel level, String module, String instance, Date date, String message) {
        setLevel(level);
        setModule(module);
        setInstance(instance);
        setDate(date);
        setMessage(message);
    }

    public LogEntryJsonDto(LogEntryDs logEntry) {
        setLevel(logEntry.getLevel());
        setModule(logEntry.getModule());
        setInstance(logEntry.getInstance());
        setDate(logEntry.getDate());
        setMessage(logEntry.getMessage());
    }

    public LoggerLevel getLevel() {
        return level;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = date == null ? null : new Date(date.getTime());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogEntryJsonDto other = (LogEntryJsonDto) o;

        return Utils.computeEq(level, other.level, module, other.module, instance, other.instance, date, other.date, message, other.message);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(level, module, instance, date, message);
    }
}

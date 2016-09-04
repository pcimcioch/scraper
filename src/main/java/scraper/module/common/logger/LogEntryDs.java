package scraper.module.common.logger;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import scraper.util.Utils;

import java.util.Date;

/**
 * Data source for log entry.
 * <p>
 * This class represents entity, saved in neo4j database. It contains all properties for one log entry.
 */
@NodeEntity
public class LogEntryDs {

    @GraphId
    private Long id;

    @Property(name = "level")
    private LoggerLevel level;

    @Property(name = "module")
    private String module;

    @Property(name = "instance")
    private String instance;

    @Property(name = "date")
    private Date date;

    @Property(name = "message")
    private String message;

    public LogEntryDs() {
    }

    public LogEntryDs(LoggerLevel level, String module, String instance, Date date, String message) {
        setLevel(level);
        setModule(module);
        setInstance(instance);
        setDate(date);
        setMessage(message);
    }

    public Long getId() {
        return id;
    }

    public LoggerLevel getLevel() {
        return level;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = date == null ? null : new Date(date.getTime());
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LogEntryDs other = (LogEntryDs) obj;

        return Utils.computeEq(this.level, other.level, this.module, other.module, this.instance, other.instance, this.date, other.date, this.message, other.message);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(level, module, instance, date, message);
    }
}

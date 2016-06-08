package scraper.module.common.logger;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import scraper.util.Utils;

import java.util.Date;

@NodeEntity
public class LogEntryDs {

    @GraphId
    private Long id;

    @GraphProperty(propertyName = "level")
    private LoggerLevel level;

    @GraphProperty(propertyName = "module")
    private String module;

    @GraphProperty(propertyName = "instance")
    private String instance;

    @GraphProperty(propertyName = "date")
    private Date date;

    @GraphProperty(propertyName = "message")
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogEntryDs other = (LogEntryDs) o;

        return Utils.computeEq(this.level, other.level, this.module, other.module, this.instance, other.instance, this.date, other.date, this.message, other.message);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(level, module, instance, date, message);
    }
}

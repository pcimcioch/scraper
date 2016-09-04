package scraper.module.common.management.module.store;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import scraper.util.Utils;

/**
 * Neo4j entity representing {@link ModuleInstance}.
 */
@NodeEntity
public class ModuleInstanceDs {

    @GraphId
    private Long id;

    @Property(name = "moduleName")
    private String moduleName;

    @Property(name = "instanceName")
    private String instanceName;

    @Property(name = "settings")
    private String settings;

    @Property(name = "schedule")
    private String schedule;

    public ModuleInstanceDs() {
    }

    public ModuleInstanceDs(String moduleName, String instanceName, String settings, String schedule) {
        this(null, moduleName, instanceName, settings, schedule);
    }

    public ModuleInstanceDs(Long id, String moduleName, String instanceName, String settings, String schedule) {
        this.id = id;
        this.moduleName = moduleName;
        this.instanceName = instanceName;
        this.settings = settings;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ModuleInstanceDs other = (ModuleInstanceDs) obj;

        return Utils.computeEq(id, other.id, moduleName, other.moduleName, instanceName, other.instanceName, settings, other.settings, schedule, other.schedule);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(id, moduleName, instanceName, settings, schedule);
    }
}

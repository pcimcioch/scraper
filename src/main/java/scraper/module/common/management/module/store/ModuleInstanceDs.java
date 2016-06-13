package scraper.module.common.management.module.store;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import scraper.util.Utils;

@NodeEntity
public class ModuleInstanceDs {

    @GraphId
    private Long id;

    @GraphProperty(propertyName = "moduleName")
    private String moduleName;

    @GraphProperty(propertyName = "instanceName")
    private String instanceName;

    @GraphProperty(propertyName = "settings")
    private String settings;

    public ModuleInstanceDs() {
    }

    public ModuleInstanceDs(String moduleName, String instanceName, String settings) {
        this.moduleName = moduleName;
        this.instanceName = instanceName;
        this.settings = settings;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModuleInstanceDs other = (ModuleInstanceDs) o;

        return Utils.computeEq(id, other.id, moduleName, other.moduleName, instanceName, other.instanceName, settings, other.settings);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(id, moduleName, instanceName, settings);
    }
}

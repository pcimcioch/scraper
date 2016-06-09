package scraper.module.common.management.module.store;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class ModuleInstanceDs {

    @GraphId
    private Long id;

    @GraphProperty(propertyName = "module")
    private String module;

    @GraphProperty(propertyName = "instance")
    private String instance;

    @GraphProperty(propertyName = "settings")
    private String settings;

    public ModuleInstanceDs() {
    }

    public ModuleInstanceDs(String module, String instance, String settings) {
        this.module = module;
        this.instance = instance;
        this.settings = settings;
    }

    public Long getId() {
        return id;
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

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }
}

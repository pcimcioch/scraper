package scraper.module.common.management.module.view;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ModuleInstanceJsonWriteDto {

    private String moduleName;

    private String instanceName;

    private ObjectNode settings;

    private String schedule;

    public ModuleInstanceJsonWriteDto() {
    }

    public ModuleInstanceJsonWriteDto(String moduleName, String instanceName, ObjectNode settings, String schedule) {
        this.moduleName = moduleName;
        this.instanceName = instanceName;
        this.settings = settings;
        this.schedule = schedule;
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

    public ObjectNode getSettings() {
        return settings;
    }

    public void setSettings(ObjectNode settings) {
        this.settings = settings;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

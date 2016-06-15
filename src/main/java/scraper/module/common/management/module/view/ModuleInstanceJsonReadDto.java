package scraper.module.common.management.module.view;

import scraper.module.common.management.module.store.ModuleInstance;
import scraper.util.Utils;

public class ModuleInstanceJsonReadDto {

    private long id;

    private String moduleName;

    private String instanceName;

    private Object settings;

    private String schedule;

    public ModuleInstanceJsonReadDto() {
    }

    public ModuleInstanceJsonReadDto(ModuleInstance instance) {
        this(instance.getId(), instance.getModuleName(), instance.getInstanceName(), instance.getSettings(), instance.getSchedule());
    }

    public ModuleInstanceJsonReadDto(long id, String moduleName, String instanceName, Object settings, String schedule) {
        this.id = id;
        this.moduleName = moduleName;
        this.instanceName = instanceName;
        this.settings = settings;
        this.schedule = schedule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Object getSettings() {
        return settings;
    }

    public void setSettings(Object settings) {
        this.settings = settings;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModuleInstanceJsonReadDto other = (ModuleInstanceJsonReadDto) o;

        return Utils.computeEq(id, other.id, moduleName, other.moduleName, instanceName, other.instanceName, settings, other.settings, schedule, other.schedule);
    }

    @Override
    public int hashCode() {
        return  Utils.computeHash(id, moduleName, instanceName, settings, schedule);
    }
}

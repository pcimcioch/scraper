package scraper.module.common.management.module.view;

import scraper.module.common.management.module.store.ModuleInstance;

public class ModuleInstanceJsonDto {

    private long id;

    private String module;

    private String instance;

    private Object settings;

    public ModuleInstanceJsonDto() {
    }

    public ModuleInstanceJsonDto(ModuleInstance instance){
        this(instance.getId(), instance.getModule(), instance.getInstance(), instance.getSettings());
    }

    public ModuleInstanceJsonDto(long id, String module, String instance, Object settings) {
        this.id = id;
        this.module = module;
        this.instance = instance;
        this.settings = settings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Object getSettings() {
        return settings;
    }

    public void setSettings(Object settings) {
        this.settings = settings;
    }
}

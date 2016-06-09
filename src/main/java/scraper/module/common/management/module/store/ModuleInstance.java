package scraper.module.common.management.module.store;

public class ModuleInstance {

    private long id;

    private String module;

    private String instance;

    private Object settings;

    public ModuleInstance() {
    }

    public ModuleInstance(String module, String instance, Object settings) {
        this(0, module, instance, settings);
    }

    public ModuleInstance(long id, String module, String instance, Object settings) {
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

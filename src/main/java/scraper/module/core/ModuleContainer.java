package scraper.module.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for storing and managing all modules available in current application instance.
 */
@Service
public class ModuleContainer {

    private final Map<String, WorkerModule<?>> workerModules = new HashMap<>();

    private final Map<String, ServiceModule> serviceModules = new HashMap<>();

    private final Map<String, StandaloneModule> standaloneModules = new HashMap<>();

    private final Map<String, Module> modules = new HashMap<>();

    @Autowired(required = false)
    public ModuleContainer(List<WorkerModule<?>> workerModules, List<ServiceModule> serviceModules, List<StandaloneModule> standaloneModules) {
        addWorkerModules(workerModules);
        addServiceModules(serviceModules);
        addStandaloneModules(standaloneModules);
        validateAllDependencies();
    }

    private void addWorkerModules(List<WorkerModule<?>> toAdd) {
        for (WorkerModule<?> workerModule : toAdd) {
            addWorkerModule(workerModule);
        }
    }

    private void addServiceModules(List<ServiceModule> toAdd) {
        for (ServiceModule serviceModule : toAdd) {
            addServiceModule(serviceModule);
        }
    }

    private void addStandaloneModules(List<StandaloneModule> toAdd) {
        for (StandaloneModule standaloneModule : toAdd) {
            addStandaloneModule(standaloneModule);
        }
    }

    private void addWorkerModule(WorkerModule<?> workerModule) {
        validate(workerModule);

        workerModules.put(workerModule.name(), workerModule);
        modules.put(workerModule.name(), workerModule);
    }

    private void addServiceModule(ServiceModule serviceModule) {
        validate(serviceModule);

        serviceModules.put(serviceModule.name(), serviceModule);
        modules.put(serviceModule.name(), serviceModule);
    }

    private void addStandaloneModule(StandaloneModule standaloneModule) {
        validate(standaloneModule);

        standaloneModules.put(standaloneModule.name(), standaloneModule);
        modules.put(standaloneModule.name(), standaloneModule);
    }

    private void validate(Module module) {
        String moduleName = module.name();
        if (existsModule(moduleName)) {
            throw new IllegalArgumentException("Duplicated module: " + moduleName);
        }
    }

    private void validateAllDependencies() {
        for (Module module : modules.values()) {
            for (String dependency : module.dependencies()) {
                if (!existsModule(dependency)) {
                    String msg = String.format("Module [%s] dependency [%s] not found", module.name(), dependency);
                    throw new IllegalStateException(msg);
                }
            }
        }
    }

    /**
     * Returns if module with given name exists.
     *
     * @param moduleName module name
     * @return <tt>true</tt> if module with given name exists, <tt>false</tt> otherwise.
     */
    public boolean existsModule(String moduleName) {
        return modules.containsKey(moduleName);
    }

    /**
     * Returns worker module with given name.
     *
     * @param moduleName module name
     * @return worker module with given name or <tt>null</tt> if such worker module doesn't exists
     */
    public WorkerModule getWorkerModule(String moduleName) {
        return workerModules.get(moduleName);
    }

    /**
     * Returns service module with given name.
     *
     * @param moduleName module name
     * @return service module with given name or <tt>null</tt> if such service module doesn't exists
     */
    public ServiceModule getServiceModule(String moduleName) {
        return serviceModules.get(moduleName);
    }

    /**
     * Returns standalone module with given name.
     *
     * @param moduleName module name
     * @return standalone module with given name or <tt>null</tt> if such standalone module doesn't exists
     */
    public StandaloneModule getStandaloneModule(String moduleName) {
        return standaloneModules.get(moduleName);
    }

    /**
     * Returns map of all modules.
     *
     * @return map of all modules. Key of the map is module name, value is module itself.
     */
    public Map<String, Module> getModules() {
        return Collections.unmodifiableMap(modules);
    }

    /**
     * Returns map of all service modules.
     *
     * @return map of all service modules. Key of the map is module name, value is module itself.
     */
    public Map<String, ServiceModule> getServiceModules() {
        return Collections.unmodifiableMap(serviceModules);
    }

    /**
     * Returns map of all worker modules.
     *
     * @return map of all worker modules. Key of the map is module name, value is module itself.
     */
    public Map<String, WorkerModule> getWorkerModules() {
        return Collections.unmodifiableMap(workerModules);
    }

    /**
     * Returns map of all standalone modules.
     *
     * @return map of all standalone modules. Key of the map is module name, value is module itself.
     */
    public Map<String, StandaloneModule> getStandaloneModules() {
        return Collections.unmodifiableMap(standaloneModules);
    }
}

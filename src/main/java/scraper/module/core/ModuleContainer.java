package scraper.module.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    String msg = String.format("Module %s dependency %s not found", module.name(), dependency);
                    throw new IllegalStateException(msg);
                }
            }
        }
    }

    public boolean existsModule(String moduleName) {
        return modules.containsKey(moduleName);
    }

    public WorkerModule getWorkerModule(String moduleName) {
        return workerModules.get(moduleName);
    }

    public ServiceModule getServiceModule(String moduleName) {
        return serviceModules.get(moduleName);
    }

    public StandaloneModule getStandaloneModule(String moduleName) {
        return standaloneModules.get(moduleName);
    }

    public Map<String, Module> getModules() {
        return Collections.unmodifiableMap(modules);
    }

    public Map<String, Module> getServiceModules() {
        return Collections.unmodifiableMap(serviceModules);
    }

    public Map<String, Module> getWorkerModules() {
        return Collections.unmodifiableMap(workerModules);
    }

    public Map<String, Module> getStandaloneModules() {
        return Collections.unmodifiableMap(standaloneModules);
    }
}

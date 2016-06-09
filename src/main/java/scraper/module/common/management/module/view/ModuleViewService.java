package scraper.module.common.management.module.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.common.management.module.runner.WorkerDescriptor;
import scraper.module.common.management.module.store.ModuleInstance;
import scraper.module.common.management.module.store.ModuleStoreService;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ModuleDetails;

import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
// TODO add mising tests
public class ModuleViewService {

    private final ModuleContainer moduleContainer;

    private final ModuleRunner moduleRunner;

    private final ModuleStoreService moduleStoreService;

    @Autowired
    public ModuleViewService(ModuleContainer moduleContainer, ModuleRunner moduleRunner, ModuleStoreService moduleStoreService) {
        this.moduleContainer = moduleContainer;
        this.moduleRunner = moduleRunner;
        this.moduleStoreService = moduleStoreService;
    }

    public List<ModuleDescriptorJsonDto> getModules() {
        return map(moduleContainer.getModules().values(), ModuleDescriptorJsonDto::new);
    }

    public List<ModuleInstanceJsonDto> getModuleInstances() {
        return map(moduleStoreService.getModuleInstances(), ModuleInstanceJsonDto::new);
    }

    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleRunner.getWorkingWorkers();
    }

    public void stopWorkerModule(String workerId) {
        moduleRunner.stopWorker(workerId);
    }

    public void addModuleInstance(String moduleName, String instance, ObjectNode settingsJson) {
        Object settings = buildSettings(moduleName, settingsJson);
        moduleStoreService.addModuleInstance(new ModuleInstance(moduleName, instance, settings));
    }

    public void runModuleInstance(long id) {
        ModuleInstance moduleInstance = moduleStoreService.getModuleInstance(id);
        ModuleDetails moduleDetails = new ModuleDetails(moduleInstance.getModule(), moduleInstance.getInstance());

        moduleRunner.runWorkerAsync(moduleDetails, moduleInstance.getSettings());
    }

    public void deleteModuleInstance(long id) {
        moduleStoreService.deleteModuleInstance(id);
    }

    private Object buildSettings(String moduleName, ObjectNode settingsJson) {
        Class<?> settingsType = getSettingsType(moduleName);

        return transform(settingsJson, settingsType);
    }

    private Class<?> getSettingsType(String moduleName) {
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleName);
        if (module == null) {
            throw new IllegalArgumentException("Worker Module " + moduleName + " not found");
        }

        return module.getSettingsClass();
    }

    private <T> T transform(ObjectNode settingsJson, Class<T> settingsType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(settingsJson, settingsType);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Incorrect settings format", ex);
        }
    }
}

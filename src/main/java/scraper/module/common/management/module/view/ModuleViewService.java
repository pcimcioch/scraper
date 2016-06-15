package scraper.module.common.management.module.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.exception.ResourceNotFoundException;
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
// TODO add some synchronization
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

    public List<ModuleInstanceJsonReadDto> getModuleInstances() {
        return map(moduleStoreService.getModuleInstances(), ModuleInstanceJsonReadDto::new);
    }

    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleRunner.getWorkingWorkers();
    }

    public void stopWorkerModule(String workerId) {
        moduleRunner.stopWorker(workerId);
    }

    public void addModuleInstance(ModuleInstanceJsonWriteDto moduleInstanceDto) {
        Object settings = buildSettings(moduleInstanceDto.getModuleName(), moduleInstanceDto.getSettings());
        moduleStoreService.addModuleInstance(new ModuleInstance(moduleInstanceDto.getModuleName(), moduleInstanceDto.getInstanceName(), settings, moduleInstanceDto.getSchedule()));
    }

    public void updateModuleInstanceSettings(long instanceId, ObjectNode settingsJson) {
        ModuleInstance instance = moduleStoreService.getModuleInstance(instanceId);
        if (instance == null) {
            throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
        }

        Object settings = buildSettings(instance.getModuleName(), settingsJson);
        moduleStoreService.updateSettings(instanceId, settings);
    }

    public void updateModuleInstanceSchedule(long instanceId, String schedule) {
        moduleStoreService.updateSchedule(instanceId, schedule);
    }

    public void runModuleInstance(long instanceId) {
        ModuleInstance instance = moduleStoreService.getModuleInstance(instanceId);
        if (instance == null) {
            throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
        }
        ModuleDetails moduleDetails = new ModuleDetails(instance.getModuleName(), instance.getInstanceName());

        moduleRunner.runWorkerAsync(moduleDetails, instance.getSettings());
    }

    public void deleteModuleInstance(long instanceId) {
        moduleStoreService.deleteModuleInstance(instanceId);
    }

    private Object buildSettings(String moduleName, ObjectNode settingsJson) {
        Class<?> settingsType = getSettingsType(moduleName);

        return transform(settingsJson, settingsType);
    }

    private Class<?> getSettingsType(String moduleName) {
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleName);
        if (module == null) {
            throw new ResourceNotFoundException("Worker Module %s not found", moduleName);
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

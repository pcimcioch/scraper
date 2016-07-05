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

import java.util.List;

import static scraper.util.FuncUtils.map;

/**
 * Service used to manage modules and their instances.
 */
@Service
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

    /**
     * Gets list of all {@link scraper.module.core.Module} available in application.
     *
     * @return list of available modules represented as json DTOs
     */
    public List<ModuleDescriptorJsonDto> getModules() {
        return map(moduleContainer.getModules().values(), ModuleDescriptorJsonDto::new);
    }

    /**
     * Gets list of all {@link WorkerModule} instances.
     *
     * @return list of all worker mdule instances represented as json DTOs
     */
    public List<ModuleInstanceJsonReadDto> getModuleInstances() {
        return map(moduleStoreService.getModuleInstances(), ModuleInstanceJsonReadDto::new);
    }

    /**
     * Gets list of currently running {@link WorkerModule}.
     *
     * @return list of running worker modules
     */
    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleRunner.getWorkingWorkers();
    }

    /**
     * Requests stop of the worker module.
     *
     * @param workerId worker id
     */
    public void stopWorkerModule(String workerId) {
        moduleRunner.stopWorker(workerId);
    }

    /**
     * Creates new {@link WorkerModule} instance.
     *
     * @param moduleInstanceDto json DTO representing new module instance
     * @throws ResourceNotFoundException             if {@link WorkerModule} with given name can not be found
     * @throws IllegalArgumentException              if settings are in incorrect format
     * @throws scraper.exception.ValidationException if settings have incorrect values
     */
    public void addModuleInstance(ModuleInstanceJsonWriteDto moduleInstanceDto) {
        Object settings = buildSettings(moduleInstanceDto.getModuleName(), moduleInstanceDto.getSettings());
        moduleStoreService.addModuleInstance(new ModuleInstance(moduleInstanceDto.getModuleName(), moduleInstanceDto.getInstanceName(), settings, moduleInstanceDto.getSchedule()));
    }

    /**
     * Updates settings in existing {@link WorkerModule} instance.
     *
     * @param instanceId   worker module instance id
     * @param settingsJson new settings represented by json DTO
     * @throws ResourceNotFoundException             if {@link WorkerModule} with given name or instance of worker module can not be found
     * @throws IllegalArgumentException              if settings are in incorrect format
     * @throws scraper.exception.ValidationException if settings have incorrect values
     */
    public void updateModuleInstanceSettings(long instanceId, ObjectNode settingsJson) {
        ModuleInstance instance = moduleStoreService.getModuleInstance(instanceId);
        if (instance == null) {
            throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
        }

        Object settings = buildSettings(instance.getModuleName(), settingsJson);
        moduleStoreService.updateSettings(instanceId, settings);
    }

    /**
     * Updates schedule in existing {@link WorkerModule} instance.
     *
     * @param instanceId worker module instance id
     * @param schedule   new schedule
     * @throws ResourceNotFoundException             if instance of worker module can not be found
     * @throws scraper.exception.ValidationException if schedule is in incorrect format
     */
    public void updateModuleInstanceSchedule(long instanceId, String schedule) {
        moduleStoreService.updateSchedule(instanceId, schedule);
    }

    /**
     * Runs {@link WorkerModule} instance.
     *
     * @param instanceId worker module instance
     * @throws ResourceNotFoundException if instance of worker module can not be found
     */
    public void runModuleInstance(long instanceId) {
        moduleStoreService.runModuleInstance(instanceId);
    }

    /**
     * Deletes {@link WorkerModule} instance.
     *
     * @param instanceId worker module instance
     */
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
            throw new ResourceNotFoundException("Worker Module [%s] not found", moduleName);
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

package scraper.module.common.management.module.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.exception.ResourceNotFoundException;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.properties.ClassPropertyDescriptorFactory;

import java.io.IOException;
import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRED)
public class ModuleStoreService {

    private final ModuleInstanceDsRepository instanceRepository;

    private final ModuleContainer moduleContainer;

    @Autowired
    public ModuleStoreService(ModuleInstanceDsRepository instanceRepository, ModuleContainer moduleContainer) {
        this.instanceRepository = instanceRepository;
        this.moduleContainer = moduleContainer;
    }

    public ModuleInstance getModuleInstance(long instanceId) {
        ModuleInstanceDs instanceDs = instanceRepository.findOne(instanceId);
        return buildModuleInstance(instanceDs);
    }

    public List<ModuleInstance> getModuleInstances() {
        return map(instanceRepository.findAll(), this::buildModuleInstance);
    }

    public void deleteModuleInstance(long instanceId) {
        instanceRepository.delete(instanceId);
    }

    public void addModuleInstance(ModuleInstance instance) {
        validateModuleInstance(instance);
        validateSettings(instance.getModuleName(), instance.getSettings());

        instanceRepository.save(buildModuleInstanceDs(instance));
    }

    public void updateSettings(long instanceId, Object newSettings) {
        ModuleInstanceDs instanceDs = instanceRepository.findOne(instanceId);
        if (instanceDs == null) {
            throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
        }
        validateSettings(instanceDs.getModuleName(), newSettings);

        instanceDs.setSettings(toJson(newSettings));
        instanceRepository.save(instanceDs);
    }

    private ModuleInstance buildModuleInstance(ModuleInstanceDs instanceDs) {
        if (instanceDs == null) {
            return null;
        }

        Object settings = buildSettings(instanceDs.getModuleName(), instanceDs.getSettings());
        return new ModuleInstance(instanceDs.getId(), instanceDs.getModuleName(), instanceDs.getInstanceName(), settings);
    }

    private ModuleInstanceDs buildModuleInstanceDs(ModuleInstance instance) {
        return instance == null ? null : new ModuleInstanceDs(instance.getModuleName(), instance.getInstanceName(), toJson(instance.getSettings()));
    }

    private void validateModuleInstance(ModuleInstance instance) {
        if (instanceRepository.findByModuleNameAndInstanceName(instance.getModuleName(), instance.getInstanceName()) != null) {
            throw new IllegalArgumentException("Instance " + instance.getInstanceName() + " of worker module " + instance.getModuleName() + " already exists");
        }
    }

    private void validateSettings(String moduleName, Object settings) {
        Class<?> settingsType = getSettingsType(moduleName);
        if (!settingsType.isAssignableFrom(settings.getClass())) {
            throw new IllegalArgumentException("Incorrect settings type passed");
        }

        ClassPropertyDescriptorFactory.validate(settings);
    }

    private Object buildSettings(String moduleName, String settingsJson) {
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

    private <T> T transform(String settingsJson, Class<T> settingsType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(settingsJson, settingsType);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect settings format", ex);
        }
    }

    private String toJson(Object settingsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(settingsJson);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Incorrect json model");
        }
    }
}

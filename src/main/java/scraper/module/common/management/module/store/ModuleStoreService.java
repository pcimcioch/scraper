package scraper.module.common.management.module.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.properties.ClassPropertyDescriptorFactory;

import java.io.IOException;
import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRED)
// TODO add tests
public class ModuleStoreService {

    private final ModuleInstanceDsRepository instanceRepository;

    private final ModuleContainer moduleContainer;

    @Autowired
    public ModuleStoreService(ModuleInstanceDsRepository instanceRepository, ModuleContainer moduleContainer) {
        this.instanceRepository = instanceRepository;
        this.moduleContainer = moduleContainer;
    }

    public ModuleInstance getModuleInstance(long id) {
        ModuleInstanceDs instanceDs = instanceRepository.findOne(id);
        return buildModuleInstance(instanceDs);
    }

    public List<ModuleInstance> getModuleInstances() {
        return map(instanceRepository.findAll(), this::buildModuleInstance);
    }

    public void deleteModuleInstance(long id) {
        instanceRepository.delete(id);
    }

    public void addModuleInstance(ModuleInstance instance) {
        validateModuleInstance(instance);
        validateSettings(instance);

        instanceRepository.save(buildModuleInstanceDs(instance));
    }

    private ModuleInstance buildModuleInstance(ModuleInstanceDs instanceDs) {
        if (instanceDs == null) {
            return null;
        }

        Object settings = buildSettings(instanceDs.getModule(), instanceDs.getSettings());
        return new ModuleInstance(instanceDs.getId(), instanceDs.getModule(), instanceDs.getInstance(), settings);
    }

    private ModuleInstanceDs buildModuleInstanceDs(ModuleInstance instance) {
        return instance == null ? null : new ModuleInstanceDs(instance.getModule(), instance.getInstance(), toJson(instance.getSettings()));
    }

    private void validateModuleInstance(ModuleInstance instance) {
        if (instanceRepository.findByModuleAndInstance(instance.getModule(), instance.getInstance()) != null) {
            throw new IllegalArgumentException("Instance " + instance.getInstance() + " of worker module " + instance.getModule() + " already exists");
        }
    }

    private void validateSettings(ModuleInstance instance){
        ClassPropertyDescriptorFactory.validate(instance.getSettings());
    }

    private Object buildSettings(String moduleName, String settingsJson) {
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

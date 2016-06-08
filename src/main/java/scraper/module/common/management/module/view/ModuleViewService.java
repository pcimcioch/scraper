package scraper.module.common.management.module.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.common.management.module.runner.WorkerDescriptor;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ModuleDetails;

import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRED)
public class ModuleViewService {

    private final ModuleContainer moduleContainer;

    private final ModuleRunner moduleRunner;

    @Autowired
    public ModuleViewService(ModuleContainer moduleContainer, ModuleRunner moduleRunner) {
        this.moduleContainer = moduleContainer;
        this.moduleRunner = moduleRunner;
    }

    public List<ModuleDescriptorJsonDto> getModules() {
        return map(moduleContainer.getModules().values(), ModuleDescriptorJsonDto::new);
    }

    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleRunner.getWorkingWorkers();
    }

    public void stopWorkerModule(String workerId) {
        moduleRunner.stopWorker(workerId);
    }

    public void runModule(String moduleName, ObjectNode settingsJson, String instance) {
        ModuleDetails moduleDetails = new ModuleDetails(moduleName, instance);
        Object settings = buildSettings(moduleName, settingsJson);

        moduleRunner.runWorkerAsync(moduleDetails, settings);
    }

    private Object buildSettings(String moduleName, ObjectNode settingsJson) {
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleName);
        if (module == null) {
            throw new IllegalArgumentException("Worker Module " + moduleName + " not found");
        }
        Class<?> settingsType = module.getSettingsClass();

        return transform(settingsJson, settingsType);
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

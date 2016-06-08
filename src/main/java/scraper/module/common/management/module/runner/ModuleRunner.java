package scraper.module.common.management.module.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ExecutionFlow;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.module.core.scope.InModuleScope;
import scraper.util.FuncUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ModuleRunner {

    public static final String STATUS_TOPIC = ModuleRunnerModule.NAME + "/topic/status";

    private final ModuleContext moduleContext;

    private final LoggerService logger;

    private final ModuleContainer moduleContainer;

    private final SimpMessagingTemplate template;

    private final List<WorkerDescriptor> workingWorkers = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    public ModuleRunner(ModuleContext moduleContext, LoggerService logger, ModuleContainer moduleContainer, SimpMessagingTemplate template) {
        this.moduleContext = moduleContext;
        this.logger = logger;
        this.moduleContainer = moduleContainer;
        this.template = template;
    }

    public boolean stopWorker(String id) {
        synchronized (workingWorkers) {
            for (WorkerDescriptor descriptor : workingWorkers) {
                if (descriptor.getId().equals(id)) {
                    descriptor.stop();
                    return true;
                }
            }
        }

        return false;
    }

    public List<WorkerDescriptor> getWorkingWorkers() {
        synchronized (workingWorkers) {
            return FuncUtils.map(workingWorkers, WorkerDescriptor::new);
        }
    }

    @InModuleScope
    @Async
    public void runWorkerAsync(ModuleDetails moduleDetails, Object settings) {
        runWorker(moduleDetails, settings);
    }

    @InModuleScope
    public void runWorker(ModuleDetails moduleDetails, Object settings) {
        initWorkerRun(moduleDetails);
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleDetails.getModule());
        if (module == null) {
            throw new IllegalArgumentException("Worker module " + moduleDetails.getModule() + " not found");
        }

        runWorker(module, settings);
    }

    private void initWorkerRun(ModuleDetails moduleDetails) {
        moduleContext.setModuleDetails(moduleDetails);
    }

    private void runWorker(WorkerModule<?> module, Object settings) {
        WorkerDescriptor descriptor = registerWorker();

        try {
            module.call(settings);
        } catch (Throwable th) {
            logger.error("Error running worker module: %s", th, th.getMessage());
        } finally {
            unregisterWorker(descriptor);
        }
    }

    private WorkerDescriptor registerWorker() {
        ExecutionFlow executionFlow = moduleContext.getExecutionFlow();
        WorkerDescriptor descriptor = new WorkerDescriptor(moduleContext.getModuleDetails(), executionFlow);

        synchronized (workingWorkers) {
            ModuleDetails currentModuleDetails = moduleContext.getModuleDetails();
            if (workingWorkers.stream().map(WorkerDescriptor::getModuleDetails).anyMatch(currentModuleDetails::equals)) {
                throw new IllegalStateException(String.format("Worker %s instance %s already in progress", currentModuleDetails.getModule(), currentModuleDetails.getInstance()));
            }
            workingWorkers.add(descriptor);
        }
        executionFlow.setObservator(flow -> this.handleStatusChange(new WorkerDescriptor(descriptor)));

        return descriptor;
    }

    private void unregisterWorker(WorkerDescriptor descriptor) {
        ExecutionFlow executionFlow = descriptor.getExecutionFlow();

        workingWorkers.remove(descriptor);
        executionFlow.setObservator(null);
    }

    private void handleStatusChange(WorkerDescriptor descriptor) {
        try {
            template.convertAndSend(STATUS_TOPIC, descriptor);
        } catch (Exception ex) {
            logger.error("Unable to send message: %s", ex, ex.getMessage());
        }
    }
}

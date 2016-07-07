package scraper.module.common.management.module.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scraper.exception.ResourceNotFoundException;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ExecutionFlow;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.module.core.scope.InModuleScope;
import scraper.util.FuncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Service used to run and manage runninag {@link WorkerModule}.
 */
@Service
public class ModuleRunner {

    public static final String STATUS_TOPIC = ModuleRunnerModule.NAME + "/topic/status";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final ModuleContext moduleContext;

    private final LoggerService logger;

    private final ModuleContainer moduleContainer;

    private final SimpMessagingTemplate template;

    private final List<WorkerDescriptor> workingWorkers = new ArrayList<>();

    @Autowired
    public ModuleRunner(ModuleContext moduleContext, LoggerService logger, ModuleContainer moduleContainer, SimpMessagingTemplate template) {
        this.moduleContext = moduleContext;
        this.logger = logger;
        this.moduleContainer = moduleContainer;
        this.template = template;
    }

    /**
     * Requests stop of the running {@link WorkerModule}.
     *
     * @param id id of the running worker. Can be otained from {@link WorkerDescriptor#getId()}
     * @return <tt>true</tt> if worker was found and stopped, <tt>false</tt> if worker can not be found
     */
    public boolean stopWorker(String id) {
        lock.readLock().lock();
        try {
            for (WorkerDescriptor descriptor : workingWorkers) {
                if (descriptor.getId().equals(id)) {
                    descriptor.stop();
                    return true;
                }
            }
        } finally {
            lock.readLock().unlock();
        }

        return false;
    }

    /**
     * Gets list of descriptors o running {@link WorkerModule}.
     *
     * @return list of running workers
     */
    public List<WorkerDescriptor> getWorkingWorkers() {
        lock.readLock().lock();
        try {
            return FuncUtils.map(workingWorkers, WorkerDescriptor::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Run worker asynchronously.
     * <p>
     * Run is performed in {@link scraper.module.core.scope.ModuleScope}. This method will return immediately, as it is asynchronous. To wait for worker finish, method {@link
     * #runWorker(ModuleDetails, Object)} can be used.
     *
     * @param moduleDetails module details describing module and instance of the worker
     * @param settings      worker settings
     * @throws ResourceNotFoundException if worker module can not be found
     * @throws IllegalStateException     if worker instance with the same name is already running
     */
    @InModuleScope
    @Async
    public void runWorkerAsync(ModuleDetails moduleDetails, Object settings) {
        runWorker(moduleDetails, settings);
    }

    /**
     * Run worker.
     * <p>
     * Run is performed in {@link scraper.module.core.scope.ModuleScope}. This method will wait till the worker finished its work. To run asynchronously, method {@link
     * #runWorkerAsync(ModuleDetails, Object)} can be used.
     *
     * @param moduleDetails module details describing module and instance of the worker
     * @param settings      worker settings
     * @throws ResourceNotFoundException if worker module can not be found
     * @throws IllegalStateException     if worker instance with the same name is already running
     */
    @InModuleScope
    public void runWorker(ModuleDetails moduleDetails, Object settings) {
        initWorkerRun(moduleDetails);
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleDetails.getModule());
        if (module == null) {
            throw new ResourceNotFoundException("Worker module [%s] not found", moduleDetails.getModule());
        }

        runWorker(module, settings);
    }

    private void initWorkerRun(ModuleDetails moduleDetails) {
        moduleContext.setModuleDetails(moduleDetails);
    }

    private void runWorker(WorkerModule<?> module, Object settings) {
        WorkerDescriptor descriptor = registerWorker();

        logger.info("Started worker module [%s]", module.name());
        try {
            module.call(settings);
        } catch (Exception ex) {
            logger.error("Error running worker module [%s]: %s", ex, module.name(), ex.getMessage());
        } finally {
            unregisterWorker(descriptor);
            logger.info("Finished worker module [%s]", module.name());
        }
    }

    private WorkerDescriptor registerWorker() {
        ExecutionFlow executionFlow = moduleContext.getExecutionFlow();
        WorkerDescriptor descriptor = new WorkerDescriptor(moduleContext.getModuleDetails(), executionFlow);

        lock.writeLock().lock();
        try {
            ModuleDetails currentModuleDetails = moduleContext.getModuleDetails();
            if (workingWorkers.stream().map(WorkerDescriptor::getModuleDetails).anyMatch(currentModuleDetails::equals)) {
                throw new IllegalStateException(
                        String.format("Worker [%s] instance [%s] already in progress", currentModuleDetails.getModule(), currentModuleDetails.getInstance()));
            }
            workingWorkers.add(descriptor);
        } finally {
            lock.writeLock().unlock();
        }
        executionFlow.setObserver(flow -> this.handleStatusChange(new WorkerDescriptor(descriptor)));

        return descriptor;
    }

    private void unregisterWorker(WorkerDescriptor descriptor) {
        ExecutionFlow executionFlow = descriptor.getExecutionFlow();

        lock.writeLock().lock();
        try {
            workingWorkers.remove(descriptor);
        } finally {
            lock.writeLock().unlock();
        }
        executionFlow.setObserver(null);
    }

    private void handleStatusChange(WorkerDescriptor descriptor) {
        try {
            template.convertAndSend(STATUS_TOPIC, descriptor);
        } catch (Exception ex) {
            logger.error("Unable to send message: %s", ex, ex.getMessage());
        }
    }
}

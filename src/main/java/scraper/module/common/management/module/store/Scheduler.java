package scraper.module.common.management.module.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.scope.InModuleScope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class Scheduler {

    private final LoggerService logger;

    private final TaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Autowired
    public Scheduler(TaskScheduler taskScheduler, LoggerService logger) {
        this.taskScheduler = taskScheduler;
        this.logger = logger;
    }

    public synchronized void schedule(long instanceId, Trigger trigger, Runnable callback) {
        ScheduledFuture<?> newTask = taskScheduler.schedule(() -> this.safeRun(callback), trigger);
        ScheduledFuture previousTask = tasks.put(instanceId, newTask);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }

    public synchronized void cancel(long instanceId) {
        ScheduledFuture previousTask = tasks.remove(instanceId);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }

    public synchronized boolean isScheduled(long instanceId) {
        return tasks.containsKey(instanceId);
    }

    @InModuleScope(module = ModuleStoreModule.NAME)
    public void safeRun(Runnable callback) {
        try {
            callback.run();
        } catch (Exception ex) {
            logger.error("Scheduled task failure: %s", ex, ex.getMessage());
            // TODO check if this in module scope works
            // TODO check if logger view correctly handles log entries without instance
        }
    }
}

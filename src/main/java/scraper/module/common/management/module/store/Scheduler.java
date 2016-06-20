package scraper.module.common.management.module.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;
import scraper.module.common.management.module.runner.ModuleRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
// TODO add tests
public class Scheduler {

    private final TaskScheduler taskScheduler;

    private final ModuleRunner moduleRunner;

    private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Autowired
    public Scheduler(TaskScheduler taskScheduler, ModuleRunner moduleRunner) {
        this.taskScheduler = taskScheduler;
        this.moduleRunner = moduleRunner;
    }

    public synchronized void schedule(Long instanceId, Trigger trigger, Runnable callback) {
        ScheduledFuture<?> newTask = taskScheduler.schedule(callback, trigger);
        ScheduledFuture previousTask = tasks.put(instanceId, newTask);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }

    public synchronized void cancel(Long instanceId) {
        ScheduledFuture previousTask = tasks.remove(instanceId);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }
}

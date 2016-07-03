package scraper.module.common.management.module.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class Scheduler {

    private final SchedulerRunner runner;

    private final TaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Autowired
    public Scheduler(TaskScheduler taskScheduler, SchedulerRunner runner) {
        this.taskScheduler = taskScheduler;
        this.runner = runner;
    }

    public synchronized void schedule(long instanceId, Trigger trigger, Runnable callback) {
        ScheduledFuture<?> newTask = taskScheduler.schedule(() -> runner.safeRun(callback), trigger);
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
}

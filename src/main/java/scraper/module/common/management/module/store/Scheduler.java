package scraper.module.common.management.module.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * Service used to schedule events and manage already scheduled tasks.
 */
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

    /**
     * Schedules {@code callback} to run according to the {@code trigger}.
     * <p>
     * If task with {@code taskId} is already scheduled, it will be cancelled.
     *
     * @param taskId   taskId used to identify task
     * @param trigger  trigger
     * @param callback callback
     */
    public synchronized void schedule(long taskId, Trigger trigger, Runnable callback) {
        ScheduledFuture<?> newTask = taskScheduler.schedule(() -> runner.safeRun(callback), trigger);
        ScheduledFuture<?> previousTask = tasks.put(taskId, newTask);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }

    /**
     * Cancels task with given {@code taskId}.
     * <p>
     * Is such task is not scheduled, it will be ignored
     *
     * @param taskId task id
     */
    // TODO maybe one-shot tasks should be automatically removed from "tasks" map?
    public synchronized void cancel(long taskId) {
        ScheduledFuture<?> previousTask = tasks.remove(taskId);
        if (previousTask != null) {
            previousTask.cancel(false);
        }
    }

    /**
     * Checks if task with given {@code taskId} is scheduled.
     *
     * @param taskId task id
     * @return <tt>true</tt> if task with given id is scheduled, <tt>false</tt> otherwise
     */
    public synchronized boolean isScheduled(long taskId) {
        return tasks.containsKey(taskId);
    }
}

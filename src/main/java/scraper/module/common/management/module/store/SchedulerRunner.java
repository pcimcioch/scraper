package scraper.module.common.management.module.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.scope.InModuleScope;

/**
 * Simple service responsible for safely running scheduled callback in {@link scraper.module.core.scope.ModuleScope}.
 */
@Service
public class SchedulerRunner {

    private final LoggerService logger;

    @Autowired
    public SchedulerRunner(LoggerService logger) {
        this.logger = logger;
    }

    /**
     * Runs given {@code callback}.
     * <p>
     * Handles all exceptions thrown by {@code callback}. Runs it in {@link scraper.module.core.scope.ModuleScope}.
     *
     * @param callback callback method
     */
    @InModuleScope(module = ModuleStoreModule.NAME)
    public void safeRun(Runnable callback) {
        logger.trace("Starting scheduled task");
        try {
            callback.run();
        } catch (Exception ex) {
            logger.error("Scheduled task failure: %s", ex, ex.getMessage());
        } finally {
            logger.trace("Finished scheduled task");
        }
    }
}
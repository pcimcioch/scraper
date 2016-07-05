package scraper.module.common.management.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.environment.LifeCycle;
import scraper.module.common.logger.LoggerService;

/**
 * Service for managing application lifecycle.
 */
@Service
public class ApplicationLifecycleService {

    private final LoggerService logger;

    private final LifeCycle lifecycle;

    @Autowired
    public ApplicationLifecycleService(LoggerService logger, LifeCycle lifecycle) {
        this.logger = logger;
        this.lifecycle = lifecycle;
    }

    /**
     * Requests application stop.
     */
    // TODO graceful shutdown (for instance when workers are working, cancell them and wait for them. Don'e allow start new ones, block all http requests etc)
    public void stopApplication() {
        logger.info("Stopping application");
        lifecycle.finish();
    }
}

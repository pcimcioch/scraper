package scraper.environment;

import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

/**
 * Represents whole application lifecycle.
 */
@Service
public class LifeCycle {

    private final Semaphore lock = new Semaphore(0);

    /**
     * Requests shutdown of the application.
     */
    public void finish() {
        lock.release();
    }

    /**
     * This method will block until application should be shutdown.
     * <p>
     * Application can be requested to shutdown using {@link #finish()} method.
     *
     * @throws InterruptedException if this thread was interrupted
     */
    public void waitForFinish() throws InterruptedException {
        lock.acquire();
    }
}

package scraper.environment;

import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Service;

@Service
public class LifeCycle {

    private final Semaphore lock = new Semaphore(0);

    public void finish() {
        lock.release();
    }

    public void waitForFinish() throws InterruptedException {
        lock.acquire();
    }
}

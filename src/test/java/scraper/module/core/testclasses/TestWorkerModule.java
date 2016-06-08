package scraper.module.core.testclasses;

import scraper.module.common.logger.LoggerService;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ModuleContext;
import scraper.util.Utils;

import java.util.Set;

public class TestWorkerModule extends WorkerModule<TestWorkerSettings> {

    private final String name;

    private final String description;

    private final Set<String> dependencies;

    private volatile boolean stopped;

    public TestWorkerModule(String name, String description, String... dependencies) {
        super(null, null);
        this.name = name;
        this.description = description;
        this.dependencies = Utils.set(dependencies);
    }

    public TestWorkerModule(LoggerService logger, ModuleContext moduleContext, String name, String description, String... dependencies) {
        super(logger, moduleContext);
        this.name = name;
        this.description = description;
        this.dependencies = Utils.set(dependencies);
    }

    public void stop() {
        stopped = true;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Set<String> dependencies() {
        return dependencies;
    }

    @Override
    public Class<TestWorkerSettings> getSettingsClass() {
        return TestWorkerSettings.class;
    }

    @Override
    public TestWorkerSettings getSettingsDefaultObject() {
        return new TestWorkerSettings();
    }

    @Override
    protected void run(TestWorkerSettings settings) {
        while (!stopped) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}

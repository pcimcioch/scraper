package scraper.module.chan.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ModuleContext;
import scraper.util.Utils;

import java.util.Collections;
import java.util.Set;

/**
 * Worker module that connect to 4chan board archive, then accesses all archived threads, and provided they are not already saved, saves all thread information it can extract. Main
 * functionality is available in {@link ChanCollectorWorker}.
 */
@Service
public class ChanCollectorModule extends WorkerModule<ChanCollectorModuleSettings> {

    public static final String NAME = "4chan.collector";

    public static final Set<String> DEPENDENCIES = Collections.unmodifiableSet(Utils.set("http", "workspace", "logger"));

    public static final String DESCRIPTION = "4chan Collector";

    private final ChanCollectorWorker worker;

    @Autowired
    public ChanCollectorModule(LoggerService logger, ModuleContext moduleContext, ChanCollectorWorker worker) {
        super(logger, moduleContext);
        this.worker = worker;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Set<String> dependencies() {
        return DEPENDENCIES;
    }

    @Override
    public void run(ChanCollectorModuleSettings settings) {
        worker.doWork(settings);
    }

    @Override
    public Class<ChanCollectorModuleSettings> getSettingsClass() {
        return ChanCollectorModuleSettings.class;
    }

    @Override
    public ChanCollectorModuleSettings getSettingsDefaultObject() {
        return new ChanCollectorModuleSettings();
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}

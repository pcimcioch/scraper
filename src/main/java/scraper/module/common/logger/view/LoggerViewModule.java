package scraper.module.common.logger.view;

import org.springframework.stereotype.Service;
import scraper.module.core.StandaloneModule;
import scraper.util.Utils;

import java.util.Collections;
import java.util.Set;

/**
 * Standalone module used to display rest interface for saved in database {@link scraper.module.common.logger.LogEntryDs} operations. Main endpoint available in this module is
 * {@link LoggerViewController}.
 */
@Service
public class LoggerViewModule implements StandaloneModule {

    public static final String NAME = "logger.view";

    public static final Set<String> DEPENDENCIES = Collections.unmodifiableSet(Utils.set("logger", "common.web"));

    public static final String DESCRIPTION = "Logger View";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Set<String> dependencies() {
        return DEPENDENCIES;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}

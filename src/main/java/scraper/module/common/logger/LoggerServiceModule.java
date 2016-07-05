package scraper.module.common.logger;

import org.springframework.stereotype.Service;
import scraper.module.core.ServiceModule;

import java.util.Collections;
import java.util.Set;

/**
 * Service module used for logging. Main service available in this module is {@link LoggerService}.
 */
@Service
public class LoggerServiceModule implements ServiceModule {

    public static final String NAME = "logger";

    public static final Set<String> DEPENDENCIES = Collections.emptySet();

    public static final String DESCRIPTION = "Logger";

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

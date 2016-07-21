package scraper.module.common.management.application;

import org.springframework.stereotype.Service;
import scraper.module.core.StandaloneModule;
import scraper.util.Utils;

import java.util.Collections;
import java.util.Set;

/**
 * Standalone module displaying endpoint and webpage used to manage application lifecycle. Main endpoint available in this module is {@link ApplicationLifecycleController}.
 */
@Service
public class ApplicationLifecycleModule implements StandaloneModule {

    public static final String NAME = "application.lifecycle";

    public static final Set<String> DEPENDENCIES = Collections.unmodifiableSet(Utils.set("common.web"));

    public static final String DESCRIPTION = "Application Lifecycle View";

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

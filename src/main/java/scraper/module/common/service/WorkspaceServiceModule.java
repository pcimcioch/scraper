package scraper.module.common.service;

import org.springframework.stereotype.Service;
import scraper.module.core.ServiceModule;

import java.util.Collections;
import java.util.Set;

/**
 * Service module for workspace file I/O operations. Main service available in this module is {@link WorkspaceService}.
 */
@Service
public class WorkspaceServiceModule implements ServiceModule {

    public static final String NAME = "workspace";

    public static final Set<String> DEPENDENCIES = Collections.emptySet();

    public static final String DESCRIPTION = "Workspace Service";

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

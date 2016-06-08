package scraper.module.common.management.module.view;

import org.springframework.stereotype.Service;
import scraper.module.core.StandaloneModule;
import scraper.util.Utils;

import java.util.Collections;
import java.util.Set;

@Service
public class ModuleViewModule implements StandaloneModule {

    public static final String NAME = "module.view";

    public static final Set<String> DEPENDENCIES = Collections.unmodifiableSet(Utils.set("module.runner", "common.web"));

    public static final String DESCRIPTION = "Module View";

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

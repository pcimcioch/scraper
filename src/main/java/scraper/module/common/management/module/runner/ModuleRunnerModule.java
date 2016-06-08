package scraper.module.common.management.module.runner;

import org.springframework.stereotype.Service;
import scraper.module.core.ServiceModule;
import scraper.util.Utils;

import java.util.Collections;
import java.util.Set;

@Service
public class ModuleRunnerModule implements ServiceModule {

    public static final String NAME = "module.runner";

    public static final Set<String> DEPENDENCIES = Collections.unmodifiableSet(Utils.set("logger"));

    public static final String DESCRIPTION = "Module Runner";

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

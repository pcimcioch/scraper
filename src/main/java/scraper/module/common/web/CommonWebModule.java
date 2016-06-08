package scraper.module.common.web;

import org.springframework.stereotype.Service;
import scraper.module.core.StandaloneModule;

import java.util.Collections;
import java.util.Set;

@Service
public class CommonWebModule implements StandaloneModule {

    public static final String NAME = "common.web";

    public static final Set<String> DEPENDENCIES = Collections.emptySet();

    public static final String DESCRIPTION = "Common web components";

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

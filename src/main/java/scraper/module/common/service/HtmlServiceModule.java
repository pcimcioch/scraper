package scraper.module.common.service;

import org.springframework.stereotype.Service;
import scraper.module.core.ServiceModule;

import java.util.Collections;
import java.util.Set;

@Service
public class HtmlServiceModule implements ServiceModule {

    public static final String NAME = "http";

    public static final Set<String> DEPENDENCIES = Collections.emptySet();

    public static final String DESCRIPTION = "Http Service";

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

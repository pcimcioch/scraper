package scraper.module.core.testclasses;

import scraper.module.core.ServiceModule;
import scraper.util.Utils;

import java.util.Set;

public class TestServiceModule implements ServiceModule {

    private final String name;

    private final String description;

    private final Set<String> dependencies;

    public TestServiceModule(String name, String description, String... dependencies) {
        this.name = name;
        this.description = description;
        this.dependencies = Utils.set(dependencies);
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
}

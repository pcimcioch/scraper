package scraper.module.core;

import java.util.Set;

/**
 * Main entity in the application. Represents set of plug-able functionality.
 */
public interface Module {

    /**
     * Description of this module.
     *
     * @return description
     */
    String description();

    /**
     * Name of this module.
     * <p>
     * Must be unique. If two modules with same name are used, application will not start.
     *
     * @return name of this module
     */
    String name();

    /**
     * Set of the names of different modules, that this module depends on.
     * <p>
     * All modules from this set must be present in the application, or application will not start.
     * <p>
     * If this module uses any other module functionality, it should be listed here.
     *
     * @return set of dependencies
     */
    Set<String> dependencies();
}

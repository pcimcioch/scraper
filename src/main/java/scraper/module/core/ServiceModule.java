package scraper.module.core;

/**
 * Type of the {@link Module} that represents service modules.
 * <p>
 * Service modules are designed to provide some service and functionality, used often by other modules. Commonly, they are not run in {@link scraper.module.core.scope.ModuleScope},
 * but use scope of calling module.
 */
public interface ServiceModule extends Module {

}

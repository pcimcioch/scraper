package scraper.module.core;

/**
 * Type of the {@link Module} that represents standalone modules.
 * <p>
 * Standalone modules are called from outside of application. For instance, all modules that are REST endpoints, and their functions are fired by http calls should be standalone
 * modules.
 * <p>
 * Each standalone module should worry about being run in correct {@link scraper.module.core.scope.ModuleScope}, using for example {@link scraper.module.core.scope.InModuleScope}
 * annotation
 */
public interface StandaloneModule extends Module {

}

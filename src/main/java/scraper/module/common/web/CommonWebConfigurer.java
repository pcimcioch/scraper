package scraper.module.common.web;

/**
 * By implementing this interface, module can register its webpage.
 * <p>
 * For this to work, all module endpoints must be under path <tt>/{MODULE.NAME}/</tt>, where <tt>MODULE.NAME</tt> is name of the module.
 */
public interface CommonWebConfigurer {

    // TODO maybe add support for url's different then module name
    String getModuleName();

    String getModuleDescription();
}

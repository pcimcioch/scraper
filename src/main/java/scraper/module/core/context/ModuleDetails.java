package scraper.module.core.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import scraper.util.StringUtils;
import scraper.util.Utils;

import java.util.logging.Logger;

/**
 * Class representing details of the module in current module context.
 * <p>
 * Contains information on module name, instance name, and physical logger used by the {@link scraper.module.core.Module} instance.
 */
public class ModuleDetails {

    // TODO make sure module name and instance names match some pattern, so no forbidden chars are allowed
    private final String module;

    private final String instance;

    private final Logger logger;

    public ModuleDetails(String module) {
        this(module, null);
    }

    public ModuleDetails(ModuleDetails moduleDetails) {
        this(moduleDetails.module, moduleDetails.instance);
    }

    public ModuleDetails(String module, String instance) {
        this.module = module;
        this.instance = instance;
        logger = Logger.getLogger(getLoggerName(module, instance));
    }

    /**
     * Returns module name.
     *
     * @return module name
     */
    public String getModule() {
        return module;
    }

    /**
     * Returns module instance name.
     *
     * @return module instance name. May be <tt>null</tt>
     */
    public String getInstance() {
        return instance;
    }

    /**
     * Returns physical logger that should be used by module described by this details.
     *
     * @return logger to use
     */
    @JsonIgnore
    public Logger getLogger() {
        return logger;
    }

    private static String getLoggerName(String module, String instance) {
        return StringUtils.isBlank(instance) ? module : String.format("%s:%s", module, instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModuleDetails other = (ModuleDetails) o;

        return Utils.computeEq(module, other.module, instance, other.instance);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(module, instance);
    }
}

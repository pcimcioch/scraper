package scraper.module.core.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import scraper.exception.ValidationException;
import scraper.util.StringUtils;
import scraper.util.Utils;

/**
 * Class representing details of the module in current module context.
 * <p>
 * Contains information on module name, instance name, and physical logger used by the {@link scraper.module.core.Module} instance.
 */
public class ModuleDetails {

    private static final String MODULE_PATTERN = "[a-zA-Z0-9\\.]+";

    private static final String INSTANCE_PATTERN = "[a-zA-Z0-9\\.]+";

    private final String module;

    private final String instance;

    private final Log logger;

    public ModuleDetails(String module) {
        this(module, null);
    }

    public ModuleDetails(ModuleDetails moduleDetails) {
        this(moduleDetails.module, moduleDetails.instance);
    }

    /**
     * Module details constructor.
     *
     * @param module   module name. Must match {@link #MODULE_PATTERN} pattern
     * @param instance instance name. May be null. Must match {@link #INSTANCE_PATTERN} pattern
     * @throws ValidationException if module {@code name} or {@code instance} name is incorrect
     */
    public ModuleDetails(String module, String instance) {
        validateModule(module);
        validateInstance(instance);

        this.module = module;
        this.instance = instance;
        this.logger = LogFactory.getLog(getLoggerName(module, instance));
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
    public Log getLogger() {
        return logger;
    }

    private static String getLoggerName(String module, String instance) {
        return StringUtils.isBlank(instance) ? module : String.format("%s:%s", module, instance);
    }

    /**
     * Validates if given {@code module} is correct module name.
     *
     * @param module module name
     * @throws ValidationException id {@code module} is not correct module name
     */
    public static void validateModule(String module) {
        if (module == null || !module.matches(MODULE_PATTERN)) {
            throw new ValidationException("Module name [%s] doesn't match allowed pattern: %s", module, MODULE_PATTERN);
        }
    }

    /**
     * Validates if given {@code instance} is correct worker module instance name.
     *
     * @param instance instance name
     * @throws ValidationException id {@code instance} is not correct worker module instance name
     */
    public static void validateInstance(String instance) {
        if (instance != null && !instance.matches(INSTANCE_PATTERN)) {
            throw new ValidationException("Instance name [%s] doesn't match allowed pattern: %s", instance, INSTANCE_PATTERN);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ModuleDetails other = (ModuleDetails) obj;

        return Utils.computeEq(module, other.module, instance, other.instance);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(module, instance);
    }
}

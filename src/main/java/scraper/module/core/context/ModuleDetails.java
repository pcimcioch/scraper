package scraper.module.core.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import scraper.util.StringUtils;
import scraper.util.Utils;

import java.util.logging.Logger;

public class ModuleDetails {

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

    public String getModule() {
        return module;
    }

    public String getInstance() {
        return instance;
    }

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

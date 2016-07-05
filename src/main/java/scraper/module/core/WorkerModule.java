package scraper.module.core;

import org.springframework.beans.factory.annotation.Autowired;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.properties.ClassPropertyDescriptor;
import scraper.module.core.properties.ClassPropertyDescriptorFactory;

/**
 * Type of the {@link Module} that represents worker modules.
 * <p>
 * Workers are modules that performs some, often cyclic and repeatable, operations. Worker modules are called by application itself, so it shouldn't worry about being in correct
 * {@link scraper.module.core.scope.ModuleScope}. It's other application components responsibility to provide it.
 * <p>
 * Worker execution if highly often dependant on some execution settings.
 *
 * @param <T> type of the worker settings.
 */
public abstract class WorkerModule<T> implements Module {

    private final LoggerService logger;

    private final ModuleContext moduleContext;

    private final ClassPropertyDescriptor settingsClassPropertyDescriptor;

    @Autowired
    protected WorkerModule(LoggerService logger, ModuleContext moduleContext) {
        this.logger = logger;
        this.moduleContext = moduleContext;
        this.settingsClassPropertyDescriptor = ClassPropertyDescriptorFactory.buildClassPropertyDescriptor(getSettingsClass(), getSettingsDefaultObject());
    }

    /**
     * Calls worker module.
     *
     * @param settingsObj settings used by this worker
     * @throws IllegalArgumentException              if given settings class type is incorrect
     * @throws scraper.exception.ValidationException if given settings class does not pass validation process. See {@link ClassPropertyDescriptorFactory#validate(Object)}
     */
    @SuppressWarnings("unchecked")
    public void call(Object settingsObj) {
        validateSettingsType(settingsObj);
        T settings = (T) settingsObj;
        validateSettingsContent(settings);

        try {
            moduleContext.getExecutionFlow().setRunning(true);
            run(settings);
        } catch (Exception ex) {
            logger.error("Error running module: %s", ex, ex.getMessage());
        } finally {
            moduleContext.getExecutionFlow().setRunning(false);
        }
    }

    /**
     * Returns this worker settings class type.
     *
     * @return settings class type
     */
    public abstract Class<T> getSettingsClass();

    /**
     * Returns this worker default settings instance.
     * <p>
     * Returned object doesn't have to contain correct values, in the meaning that it will not necessarily validate.
     *
     * @return default settings instance.
     */
    public abstract T getSettingsDefaultObject();

    /**
     * Returns {@link ClassPropertyDescriptor} of this workers settings class.
     *
     * @return class property descriptor of this workers settings class
     * @see #getSettingsClass()
     */
    public ClassPropertyDescriptor getSettingsClassPropertyDescriptor() {
        return settingsClassPropertyDescriptor;
    }

    protected abstract void run(T settings);

    private void validateSettingsType(Object settings) {
        Class<?> actualSettingsClass = settings.getClass();
        Class<?> expectedSettingsClass = getSettingsClass();

        if (!actualSettingsClass.equals(expectedSettingsClass)) {
            String msg = String.format("Expected Settings class: %s. Actual: %s", expectedSettingsClass.getName(), actualSettingsClass.getName());
            throw new IllegalArgumentException(msg);
        }
    }

    private void validateSettingsContent(T settings) {
        ClassPropertyDescriptorFactory.validate(settings);
    }
}

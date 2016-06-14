package scraper.module.core;

import org.springframework.beans.factory.annotation.Autowired;
import scraper.exception.IllegalAnnotationException;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.properties.ClassPropertyDescriptor;
import scraper.module.core.properties.ClassPropertyDescriptorFactory;

public abstract class WorkerModule<T> implements Module {

    private final LoggerService logger;

    private final ModuleContext moduleContext;

    private final ClassPropertyDescriptor classPropertyDescriptor;

    @Autowired
    protected WorkerModule(LoggerService logger, ModuleContext moduleContext) {
        this.logger = logger;
        this.moduleContext = moduleContext;
        this.classPropertyDescriptor = ClassPropertyDescriptorFactory.buildClassPropertyDescriptor(getSettingsClass(), getSettingsDefaultObject());
    }

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

    public abstract Class<T> getSettingsClass();

    public abstract T getSettingsDefaultObject();

    public ClassPropertyDescriptor getClassPropertyDescriptor() {
        return classPropertyDescriptor;
    }

    protected abstract void run(T settings);

    private void validateSettingsType(Object settings) {
        Class<?> actualSettingsClass = settings.getClass();
        Class<?> expectedSettingsClass = getSettingsClass();

        if (!actualSettingsClass.equals(expectedSettingsClass)) {
            throw new IllegalAnnotationException("Expected Settings class: %s. Actual: %s", expectedSettingsClass.getName(), actualSettingsClass.getName());
        }
    }

    private void validateSettingsContent(T settings) {
        ClassPropertyDescriptorFactory.validate(settings);
    }
}

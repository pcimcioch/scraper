package scraper.module.core.context;

import org.springframework.stereotype.Component;
import scraper.module.core.scope.ModuleScoped;

/**
 * Context in which runs the {@link scraper.module.core.Module}.
 * <p>
 * Contains information about module name, instance and execution flow.
 */
@Component
@ModuleScoped
public class ModuleContext {

    private ModuleDetails moduleDetails;

    private final ExecutionFlow executionFlow = new ExecutionFlow();

    /**
     * Sets module details.
     * <p>
     * Should be called at the beginning of the module scope.
     *
     * @param moduleDetails the module details to set
     * @see scraper.module.core.scope.InModuleScope
     * @see scraper.module.core.scope.ModuleScope
     */
    public void setModuleDetails(ModuleDetails moduleDetails) {
        this.moduleDetails = moduleDetails;
    }

    /**
     * Returns module details.
     *
     * @return module details
     * @throws IllegalStateException if module scope was never set. May happen if called before {@link #setModuleDetails(ModuleDetails)}
     */
    public ModuleDetails getModuleDetails() {
        if (moduleDetails == null) {
            throw new IllegalStateException("Module Context not initialized");
        }

        return moduleDetails;
    }

    /**
     * Return execution flow instance for this context.
     *
     * @return execution flow instance.
     */
    public ExecutionFlow getExecutionFlow() {
        return executionFlow;
    }

    /**
     * Returns if current execution was requested to stop.
     * <p>
     * Effectively bride for {@link ExecutionFlow#isStopped()} method of the {@link ModuleContext#executionFlow}.
     *
     * @return <tt>true</tt> if current scope was requested to stop, <tt>false</tt> otherwise
     */
    public boolean isStopped() {
        return executionFlow.isStopped();
    }

    /**
     * Sets number of steps in execution flow.
     * <p>
     * Effectively bridge for {@link Status#setSteps(int)} method of the {@link ModuleContext#executionFlow} {@link ExecutionFlow#status}
     *
     * @param steps number of steps to set.
     */
    public void setSteps(int steps) {
        executionFlow.getStatus().setSteps(steps);
    }

    /**
     * Increments current step.
     * <p>
     * Effectively bridge for {@link Status#incrementCurrentStep()} method of the {@link ModuleContext#executionFlow} {@link ExecutionFlow#status}
     */
    public void incrementCurrentStep() {
        executionFlow.getStatus().incrementCurrentStep();
    }

    /**
     * Sets number of sub steps in execution flow.
     * <p>
     * Effectively bridge for {@link Status#setSubSteps(int)} method of the {@link ModuleContext#executionFlow} {@link ExecutionFlow#status}
     *
     * @param subSteps number of sub steps to set.
     */
    public void setSubSteps(int subSteps) {
        executionFlow.getStatus().setSubSteps(subSteps);
    }

    /**
     * Increments current sub step.
     * <p>
     * Effectively bridge for {@link Status#incrementCurrentSubStep()} method of the {@link ModuleContext#executionFlow} {@link ExecutionFlow#status}
     */
    public void incrementCurrentSubStep() {
        executionFlow.getStatus().incrementCurrentSubStep();
    }
}

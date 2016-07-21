package scraper.module.common.management.module.runner;

import scraper.module.core.context.ExecutionFlow;
import scraper.module.core.context.ModuleDetails;
import scraper.util.Utils;

/**
 * Description of running {@link scraper.module.core.WorkerModule}.
 * <p>
 * Contains module and instance details, as well as {@link ExecutionFlow} details.
 */
public class WorkerDescriptor {

    private final String id;

    private final ModuleDetails moduleDetails;

    private final ExecutionFlow executionFlow;

    public WorkerDescriptor(ModuleDetails moduleDetails, ExecutionFlow executionFlow) {
        this.id = Utils.generateUUID();
        this.moduleDetails = moduleDetails;
        this.executionFlow = executionFlow;
    }

    public WorkerDescriptor(WorkerDescriptor descriptor) {
        this.id = descriptor.getId();
        this.moduleDetails = new ModuleDetails(descriptor.moduleDetails);
        this.executionFlow = new ExecutionFlow(descriptor.executionFlow);
    }

    /**
     * Returns id of running module, unique or each running instance.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    public ModuleDetails getModuleDetails() {
        return moduleDetails;
    }

    public ExecutionFlow getExecutionFlow() {
        return executionFlow;
    }

    /**
     * Requests stop of the running {@link scraper.module.core.WorkerModule}.
     */
    public void stop() {
        executionFlow.stop();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        WorkerDescriptor other = (WorkerDescriptor) obj;

        return Utils.computeEq(id, other.id, moduleDetails, other.moduleDetails, executionFlow, other.executionFlow);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(id, moduleDetails, executionFlow);
    }
}

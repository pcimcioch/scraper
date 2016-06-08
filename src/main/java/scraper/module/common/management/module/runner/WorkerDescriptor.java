package scraper.module.common.management.module.runner;

import scraper.module.core.context.ExecutionFlow;
import scraper.module.core.context.ModuleDetails;
import scraper.util.Utils;

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

    public String getId() {
        return id;
    }

    public ModuleDetails getModuleDetails() {
        return moduleDetails;
    }

    public ExecutionFlow getExecutionFlow() {
        return executionFlow;
    }

    public void stop() {
        executionFlow.stop();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkerDescriptor other = (WorkerDescriptor) o;

        return Utils.computeEq(id, other.id, moduleDetails, other.moduleDetails, executionFlow, other.executionFlow);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(id, moduleDetails, executionFlow);
    }
}

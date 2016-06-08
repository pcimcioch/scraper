package scraper.module.core.context;

import org.springframework.stereotype.Component;

import scraper.module.core.scope.ModuleScoped;

@Component
@ModuleScoped
public class ModuleContext {

	private ModuleDetails moduleDetails;
	
	private final ExecutionFlow executionFlow = new ExecutionFlow();

	public void setModuleDetails(ModuleDetails moduleDetails) {
		this.moduleDetails = moduleDetails;
	}

	public ModuleDetails getModuleDetails() {
		if (moduleDetails == null) {
			throw new IllegalStateException("Module Context not initialized");
		}

		return moduleDetails;
	}

	public ExecutionFlow getExecutionFlow() {
		return executionFlow;
	}

    public boolean isStopped() {
        return executionFlow.isStopped();
    }

    public void setSteps(int steps) {
        executionFlow.getStatus().setSteps(steps);
    }

    public void incrementCurrentStep() {
        executionFlow.getStatus().incrementCurrentStep();
    }

    public void setSubSteps(int subSteps) {
        executionFlow.getStatus().setSubSteps(subSteps);
    }

    public void incrementCurrentSubStep() {
        executionFlow.getStatus().incrementCurrentSubStep();
    }
}

package scraper.module.common.management.module.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.environment.StatusMessage;
import scraper.module.common.management.module.runner.WorkerDescriptor;
import scraper.module.core.scope.InModuleScope;

import java.util.List;

@RestController
@RequestMapping(ModuleViewModule.NAME + "/api")
@InModuleScope(module = ModuleViewModule.NAME)
// TODO add something to handle exceptions and return correct status codes/error messeges
public class ModuleViewController {

    private final ModuleViewService moduleService;

    @Autowired
    public ModuleViewController(ModuleViewService moduleService) {
        this.moduleService = moduleService;
    }

    @RequestMapping(path = "/module", method = RequestMethod.GET)
    public List<ModuleDescriptorJsonDto> getModules() {
        return moduleService.getModules();
    }

    @RequestMapping(path = "/module/status", method = RequestMethod.GET)
    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleService.getModuleStatuses();
    }

    @RequestMapping(path = "/module/instance", method = RequestMethod.GET)
    public List<ModuleInstanceJsonDto> getModuleInstances() {
        return moduleService.getModuleInstances();
    }

    @RequestMapping(path = "/module/instance/{instanceId}/run", method = RequestMethod.GET)
    public StatusMessage runModuleInstance(@PathVariable("instanceId") long instanceId) {
        moduleService.runModuleInstance(instanceId);
        return new StatusMessage("Started");
    }

    @RequestMapping(path = "/module/instance/{instanceId}", method = RequestMethod.DELETE)
    public StatusMessage deleteModuleInstance(@PathVariable("instanceId") long instanceId) {
        moduleService.deleteModuleInstance(instanceId);
        return new StatusMessage("Deleted");
    }

    @RequestMapping(path = "/module/instance/{instanceId}/settings", method = RequestMethod.PUT, consumes = "application/json")
    public StatusMessage updateModuleInstanceSettings(@PathVariable("instanceId") long instanceId, @RequestBody ObjectNode settings) {
        moduleService.updateModuleInstanceSettings(instanceId, settings);
        return new StatusMessage("Updated");
    }

    @RequestMapping(path = "/module/{moduleName}/{instanceName}/", method = RequestMethod.POST, consumes = "application/json")
    public StatusMessage createModuleInstance(@PathVariable("moduleName") String moduleName, @PathVariable("instanceName") String instanceName, @RequestBody ObjectNode settings) {
        moduleService.addModuleInstance(moduleName, instanceName, settings);
        return new StatusMessage("Added");
    }

    @RequestMapping(path = "/module/{workerId}/stop", method = RequestMethod.GET)
    public StatusMessage stopModule(@PathVariable("workerId") String workerId) {
        moduleService.stopWorkerModule(workerId);
        return new StatusMessage("Stopped");
    }
}

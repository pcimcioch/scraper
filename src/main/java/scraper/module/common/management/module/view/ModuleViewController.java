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

    // TODO do not start instance ad-hoc. User need to first create instance
    @RequestMapping(path = "/module/{moduleName}/{instance}/run", method = RequestMethod.POST, consumes = "application/json")
    public StatusMessage runModule(@PathVariable("moduleName") String moduleName, @PathVariable("instance") String instance, @RequestBody ObjectNode settings) {
        moduleService.runModule(moduleName, settings, instance);
        return new StatusMessage("Started");
    }

    @RequestMapping(path = "/module/{id}/stop", method = RequestMethod.GET)
    public StatusMessage stopModule(@PathVariable("id") String workerId) {
        moduleService.stopWorkerModule(workerId);
        return new StatusMessage("Stopped");
    }
}

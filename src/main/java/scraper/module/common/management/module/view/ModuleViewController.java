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

/**
 * REST Controller used to manage modules and their instances in the application.
 */
@RestController
@RequestMapping(ModuleViewModule.NAME + "/api")
@InModuleScope(module = ModuleViewModule.NAME)
public class ModuleViewController {

    private final ModuleViewService moduleService;

    @Autowired
    public ModuleViewController(ModuleViewService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Returns all available modules.
     *
     * @return list of modules described as json DTOs
     */
    @RequestMapping(path = "/module", method = RequestMethod.GET)
    public List<ModuleDescriptorJsonDto> getModules() {
        return moduleService.getModules();
    }

    /**
     * Returns current statuses of running {@link scraper.module.core.WorkerModule}.
     *
     * @return list of running workers descriptions
     */
    @RequestMapping(path = "/module/status", method = RequestMethod.GET)
    public List<WorkerDescriptor> getModuleStatuses() {
        return moduleService.getModuleStatuses();
    }

    /**
     * Returns all {@link scraper.module.core.WorkerModule} instances.
     *
     * @return list of worker instances json DTOs
     */
    @RequestMapping(path = "/module/instance", method = RequestMethod.GET)
    public List<ModuleInstanceJsonReadDto> getModuleInstances() {
        return moduleService.getModuleInstances();
    }

    /**
     * Runs {@link scraper.module.core.WorkerModule} instance.
     *
     * @param instanceId worker module instance id
     * @return status message
     */
    @RequestMapping(path = "/module/instance/{instanceId}/run", method = RequestMethod.GET)
    public StatusMessage runModuleInstance(@PathVariable("instanceId") long instanceId) {
        moduleService.runModuleInstance(instanceId);
        return new StatusMessage("Started");
    }

    /**
     * Deletes {@link scraper.module.core.WorkerModule} instance.
     *
     * @param instanceId worker module instance id
     * @return status message
     */
    @RequestMapping(path = "/module/instance/{instanceId}", method = RequestMethod.DELETE)
    public StatusMessage deleteModuleInstance(@PathVariable("instanceId") long instanceId) {
        moduleService.deleteModuleInstance(instanceId);
        return new StatusMessage("Deleted");
    }

    /**
     * Updates {@link scraper.module.core.WorkerModule} instance settings.
     *
     * @param instanceId worker module instance id
     * @param settings   new settings
     * @return status message
     */
    @RequestMapping(path = "/module/instance/{instanceId}/settings", method = RequestMethod.PUT, consumes = "application/json")
    public StatusMessage updateModuleInstanceSettings(@PathVariable("instanceId") long instanceId, @RequestBody ObjectNode settings) {
        moduleService.updateModuleInstanceSettings(instanceId, settings);
        return new StatusMessage("Settings Updated");
    }

    /**
     * Updates {@link scraper.module.core.WorkerModule} instance schedule.
     *
     * @param instanceId worker module instance id
     * @param schedule   new schedule
     * @return status message
     */
    @RequestMapping(path = "/module/instance/{instanceId}/schedule", method = RequestMethod.PUT, consumes = "text/plain")
    public StatusMessage updateModuleInstanceSchedule(@PathVariable("instanceId") long instanceId, @RequestBody(required = false) String schedule) {
        moduleService.updateModuleInstanceSchedule(instanceId, schedule);
        return new StatusMessage("Schedule Updated");
    }

    /**
     * Creates new module instance.
     *
     * @param moduleInstance new module instance json DTO.
     * @return status message
     */
    @RequestMapping(path = "/module/instance/", method = RequestMethod.POST, consumes = "application/json")
    public StatusMessage createModuleInstance(@RequestBody ModuleInstanceJsonWriteDto moduleInstance) {
        moduleService.addModuleInstance(moduleInstance);
        return new StatusMessage("Added");
    }

    /**
     * Stops running {@link scraper.module.core.WorkerModule}.
     *
     * @param workerId worker id
     * @return status message
     */
    @RequestMapping(path = "/module/{workerId}/stop", method = RequestMethod.GET)
    public StatusMessage stopModule(@PathVariable("workerId") String workerId) {
        moduleService.stopWorkerModule(workerId);
        return new StatusMessage("Stopped");
    }
}

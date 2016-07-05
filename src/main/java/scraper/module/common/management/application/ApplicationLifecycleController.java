package scraper.module.common.management.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.environment.StatusMessage;
import scraper.module.core.scope.InModuleScope;

/**
 * REST controller used to execute operations modifying application lifecycle.
 * <p>
 * All methods are called in {@link scraper.module.core.scope.ModuleScope}.
 */
@RestController
@RequestMapping(ApplicationLifecycleModule.NAME + "/api")
@InModuleScope(module = ApplicationLifecycleModule.NAME)
public class ApplicationLifecycleController {

    private final ApplicationLifecycleService applicationLifecycleService;

    @Autowired
    public ApplicationLifecycleController(ApplicationLifecycleService applicationLifecycleService) {
        this.applicationLifecycleService = applicationLifecycleService;
    }

    /**
     * Requests application stop.
     *
     * @return status message
     */
    @RequestMapping(path = "/lifecycle/stop", method = RequestMethod.GET)
    public StatusMessage stopApplication() {
        applicationLifecycleService.stopApplication();
        return new StatusMessage("Application Stopped");
    }
}

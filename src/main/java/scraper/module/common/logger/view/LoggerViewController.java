package scraper.module.common.logger.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.environment.StatusMessage;
import scraper.module.core.scope.InModuleScope;

import java.util.List;

/**
 * REST Controller for managing log entries operations.
 * <p>
 * All methods are called in {@link scraper.module.core.scope.ModuleScope}.
 */
@RestController
@RequestMapping(LoggerViewModule.NAME + "/api")
@InModuleScope(module = LoggerViewModule.NAME)
public class LoggerViewController {

    private final LoggerViewService logsService;

    @Autowired
    public LoggerViewController(LoggerViewService logsService) {
        this.logsService = logsService;
    }

    /**
     * Returns all logs from database, represented as json DTOs.
     *
     * @return list of log entry json DTOs
     */
    @RequestMapping(path = "/log", method = RequestMethod.GET)
    public List<LogEntryJsonDto> getLogs() {
        return logsService.getAllLogs();
    }

    /**
     * Removes all logs from database.
     *
     * @return operation status message
     */
    @RequestMapping(path = "/log", method = RequestMethod.DELETE)
    public StatusMessage removeAllLogs() {
        logsService.deleteAllLogs();
        return new StatusMessage("All logs removed");
    }

    /**
     * Removes all logs that apply for given {@code module}.
     *
     * @param module module name
     * @return operation status message
     */
    @RequestMapping(path = "/log/{module}", method = RequestMethod.DELETE)
    public StatusMessage removeModuleLogs(@PathVariable("module") String module) {
        logsService.deleteModuleLogs(module);
        return new StatusMessage("Module [%s] logs removed", module);
    }
}

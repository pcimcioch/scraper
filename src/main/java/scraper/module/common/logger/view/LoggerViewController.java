package scraper.module.common.logger.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.module.core.scope.InModuleScope;

import java.util.List;

@RestController
@RequestMapping(LoggerViewModule.NAME + "/api")
@InModuleScope(module = LoggerViewModule.NAME)
public class LoggerViewController {

    private final LoggerViewService logsService;

    @Autowired
    public LoggerViewController(LoggerViewService logsService) {
        this.logsService = logsService;
    }

    @RequestMapping(path = "/log", method = RequestMethod.GET)
    public List<LogEntryJsonDto> getLogs() {
        return logsService.getAllLogs();
    }

    @RequestMapping(path = "/log", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String removeAllLogs() {
        logsService.deleteAllLogs();
        return "All logs removed";
    }

    @RequestMapping(path = "/log/{module}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String removeModuleLogs(@PathVariable("module") String module) {
        logsService.removeModuleLogs(module);
        return String.format("Module %s logs removed", module);
    }
}

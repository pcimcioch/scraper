package scraper.module.common.logger.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.common.logger.LogEntryDsRepository;

import java.util.List;

import static scraper.util.FuncUtils.map;

/**
 * Service implementing operations for managing saved in database {@link scraper.module.common.logger.LogEntryDs} records.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LoggerViewService {

    private final LogEntryDsRepository repository;

    @Autowired
    public LoggerViewService(LogEntryDsRepository repository) {
        this.repository = repository;
    }

    /**
     * Gets list of all log entries from database, represented as json DTOs.
     *
     * @return list of log entry json DTOs
     */
    public List<LogEntryJsonDto> getAllLogs() {
        return map(repository.findAll(), LogEntryJsonDto::new);
    }

    /**
     * Deletes all log entries from database.
     */
    public void deleteAllLogs() {
        repository.deleteAll();
    }

    /**
     * Delete log entries for given {@code module} from database.
     *
     * @param module module name
     */
    public void deleteModuleLogs(String module) {
        // TODO fix deleteByModule
        repository.findByModule(module).forEach(repository::delete);
    }
}

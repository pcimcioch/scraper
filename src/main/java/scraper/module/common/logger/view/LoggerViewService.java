package scraper.module.common.logger.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.common.logger.LogEntryDsRepository;

import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRED)
public class LoggerViewService {

    private final LogEntryDsRepository repository;

    @Autowired
    public LoggerViewService(LogEntryDsRepository repository) {
        this.repository = repository;
    }

    public List<LogEntryJsonDto> getAllLogs() {
        return map(repository.findAll(), LogEntryJsonDto::new);
    }

    public void deleteAllLogs() {
        repository.deleteAll();
    }

    public void removeModuleLogs(String module) {
        // TODO fix deleteByModule
        repository.findByModule(module).forEach(repository::delete);
    }
}

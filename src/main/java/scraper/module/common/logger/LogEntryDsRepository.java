package scraper.module.common.logger;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LogEntryDsRepository extends CrudRepository<LogEntryDs, Long> {
    
    List<LogEntryDs> findByModule(String module);
}

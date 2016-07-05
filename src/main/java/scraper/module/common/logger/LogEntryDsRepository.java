package scraper.module.common.logger;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Neo4J repository for {@link LogEntryDs}.
 */
public interface LogEntryDsRepository extends CrudRepository<LogEntryDs, Long> {

    List<LogEntryDs> findByModule(String module);
}

package scraper.module.chan.collector.thread;

import org.springframework.data.repository.CrudRepository;

/**
 * Neo4j repository for {@link ThreadDs}.
 */
public interface ThreadDsRepository extends CrudRepository<ThreadDs, Long> {

}

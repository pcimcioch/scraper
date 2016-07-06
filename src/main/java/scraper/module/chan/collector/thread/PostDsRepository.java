package scraper.module.chan.collector.thread;

import org.springframework.data.repository.CrudRepository;

/**
 * Neo4j repository for {@link PostDs}.
 */
public interface PostDsRepository extends CrudRepository<PostDs, Long> {

}

package scraper.module.common.management.module.store;

import org.springframework.data.repository.CrudRepository;

/**
 * Neo4j repository for {@link ModuleInstanceDs}.
 */
public interface ModuleInstanceDsRepository extends CrudRepository<ModuleInstanceDs, Long> {

    ModuleInstanceDs findByModuleNameAndInstanceName(String moduleName, String instanceName);
}

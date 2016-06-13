package scraper.module.common.management.module.store;

import org.springframework.data.repository.CrudRepository;

public interface ModuleInstanceDsRepository extends CrudRepository<ModuleInstanceDs, Long> {

    ModuleInstanceDs findByModuleNameAndInstanceName(String moduleName, String instanceName);
}

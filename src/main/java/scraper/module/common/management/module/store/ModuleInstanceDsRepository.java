package scraper.module.common.management.module.store;

import org.springframework.data.repository.CrudRepository;

public interface ModuleInstanceDsRepository extends CrudRepository<ModuleInstanceDs, Long> {

    ModuleInstanceDs findByModuleAndInstance(String module, String instance);
}

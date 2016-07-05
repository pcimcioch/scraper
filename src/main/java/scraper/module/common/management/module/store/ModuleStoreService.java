package scraper.module.common.management.module.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.zebasto.spring.post.initialize.PostInitialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.exception.ResourceNotFoundException;
import scraper.exception.ValidationException;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.core.ModuleContainer;
import scraper.module.core.WorkerModule;
import scraper.module.core.context.ModuleDetails;
import scraper.module.core.properties.ClassPropertyDescriptorFactory;
import scraper.util.StringUtils;

import java.io.IOException;
import java.util.List;

import static scraper.util.FuncUtils.map;

@Service
@Neo4jTransactional
@Transactional(propagation = Propagation.REQUIRED)
public class ModuleStoreService {

    private final ModuleInstanceDsRepository instanceRepository;

    private final ModuleContainer moduleContainer;

    private final Scheduler scheduler;

    private final ModuleRunner moduleRunner;

    @Autowired
    public ModuleStoreService(ModuleInstanceDsRepository instanceRepository, ModuleContainer moduleContainer, Scheduler scheduler, ModuleRunner moduleRunner) {
        this.instanceRepository = instanceRepository;
        this.moduleContainer = moduleContainer;
        this.scheduler = scheduler;
        this.moduleRunner = moduleRunner;
    }

    @PostInitialize
    public void initScheduler() {
        for (ModuleInstance instance : getModuleInstances()) {
            reschedule(instance.getId(), instance.getSchedule());
        }
    }

    /**
     * Gets {@link WorkerModule} instance.
     *
     * @param instanceId worker module instance id
     * @return worker module instance, or <tt>null</tt> if instance with such id can not be found
     * @throws ResourceNotFoundException if module, that searched instance is refering to, do not exists
     * @throws IllegalArgumentException  if settings data, saved in module instance, is incorrect
     */
    public ModuleInstance getModuleInstance(long instanceId) {
        ModuleInstanceDs instanceDs;
        synchronized (instanceRepository) {
            instanceDs = instanceRepository.findOne(instanceId);
        }
        return buildModuleInstance(instanceDs);
    }

    /**
     * Gets list of all {@link WorkerModule} instances.
     *
     * @return list of all worker module instances
     * @throws ResourceNotFoundException if module, that any instance is refering to, do not exists
     * @throws IllegalArgumentException  if settings data, saved in any module instance, is incorrect
     */
    public List<ModuleInstance> getModuleInstances() {
        synchronized (instanceRepository) {
            return map(instanceRepository.findAll(), this::buildModuleInstance);
        }
    }

    /**
     * Deletes {@link WorkerModule} instance.
     * <p>
     * If instance can not be found, nothing will happen
     *
     * @param instanceId worker module instance id
     */
    public void deleteModuleInstance(long instanceId) {
        synchronized (instanceRepository) {
            instanceRepository.delete(instanceId);
            scheduler.cancel(instanceId);
        }
    }

    /**
     * Creates new {@link WorkerModule} instance.
     *
     * @param instance instance to create
     * @throws ResourceNotFoundException if worker module required by this instance can not be found
     * @throws IllegalArgumentException  if settings are in incorrect format
     * @throws ValidationException       if settings or schedule have incorrect values
     */
    public void addModuleInstance(ModuleInstance instance) {
        synchronized (instanceRepository) {
            validateModuleInstance(instance);
            validateSettings(instance.getModuleName(), instance.getSettings());
            validateSchedule(instance.getSchedule());

            ModuleInstanceDs instanceDs = buildModuleInstanceDs(instance);
            instanceDs = instanceRepository.save(instanceDs);
            reschedule(instanceDs.getId(), instanceDs.getSchedule());
        }
    }

    /**
     * Updates settings in existing {@link WorkerModule} instance.
     *
     * @param instanceId  worker module instance id
     * @param newSettings new settings
     * @throws ResourceNotFoundException if worker module instance can not be found
     * @throws IllegalArgumentException  if settings are in incorrect format
     * @throws ValidationException       if settings have incorrect values
     */
    public void updateSettings(long instanceId, Object newSettings) {
        synchronized (instanceRepository) {
            ModuleInstanceDs instanceDs = instanceRepository.findOne(instanceId);
            if (instanceDs == null) {
                throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
            }
            validateSettings(instanceDs.getModuleName(), newSettings);

            instanceDs.setSettings(toJson(newSettings));
            instanceRepository.save(instanceDs);
        }
    }

    /**
     * Updates schedule in existing {@link WorkerModule} instance.
     *
     * @param instanceId  worker module instance id
     * @param newSchedule new schedule
     * @throws ResourceNotFoundException if worker module instance can not be found
     * @throws ValidationException       if schedule have incorrect values
     */
    public void updateSchedule(long instanceId, String newSchedule) {
        synchronized (instanceRepository) {
            ModuleInstanceDs instanceDs = instanceRepository.findOne(instanceId);
            if (instanceDs == null) {
                throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
            }
            validateSchedule(newSchedule);

            instanceDs.setSchedule(newSchedule);
            instanceRepository.save(instanceDs);

            reschedule(instanceDs.getId(), instanceDs.getSchedule());
        }
    }

    /**
     * Runs {@link WorkerModule} instance.
     * <p>
     * Run is asynchronous, so ths method will almost immediately return.
     *
     * @param instanceId worker module instance id
     * @throws ResourceNotFoundException if worker module instance can not be found
     */
    public void runModuleInstance(long instanceId) {
        ModuleInstance instance = getModuleInstance(instanceId);
        if (instance == null) {
            throw new ResourceNotFoundException("Instance [id=%d] not found", instanceId);
        }
        ModuleDetails moduleDetails = new ModuleDetails(instance.getModuleName(), instance.getInstanceName());

        moduleRunner.runWorkerAsync(moduleDetails, instance.getSettings());
    }

    private ModuleInstance buildModuleInstance(ModuleInstanceDs instanceDs) {
        if (instanceDs == null) {
            return null;
        }

        Object settings = buildSettings(instanceDs.getModuleName(), instanceDs.getSettings());
        return new ModuleInstance(instanceDs.getId(), instanceDs.getModuleName(), instanceDs.getInstanceName(), settings, instanceDs.getSchedule());
    }

    private void validateModuleInstance(ModuleInstance instance) {
        if (instanceRepository.findByModuleNameAndInstanceName(instance.getModuleName(), instance.getInstanceName()) != null) {
            throw new IllegalArgumentException("Instance [" + instance.getInstanceName() + "] of worker module [" + instance.getModuleName() + "] already exists");
        }
    }

    private void validateSettings(String moduleName, Object settings) {
        Class<?> settingsType = getSettingsType(moduleName);
        if (!settingsType.isAssignableFrom(settings.getClass())) {
            throw new IllegalArgumentException("Incorrect settings type passed");
        }

        ClassPropertyDescriptorFactory.validate(settings);
    }

    private Object buildSettings(String moduleName, String settingsJson) {
        Class<?> settingsType = getSettingsType(moduleName);

        return transform(settingsJson, settingsType);
    }

    private Class<?> getSettingsType(String moduleName) {
        WorkerModule<?> module = moduleContainer.getWorkerModule(moduleName);
        if (module == null) {
            throw new ResourceNotFoundException("Worker Module [%s] not found", moduleName);
        }

        return module.getSettingsClass();
    }

    private void reschedule(Long instanceId, String schedule) {
        if (StringUtils.isBlank(schedule)) {
            scheduler.cancel(instanceId);
        } else {
            scheduler.schedule(instanceId, new CronTrigger(schedule), () -> runModuleInstance(instanceId));
        }
    }

    private static ModuleInstanceDs buildModuleInstanceDs(ModuleInstance instance) {
        return instance == null ? null : new ModuleInstanceDs(instance.getModuleName(), instance.getInstanceName(), toJson(instance.getSettings()), instance.getSchedule());
    }

    private static void validateSchedule(String schedule) {
        if (StringUtils.isBlank(schedule)) {
            return;
        }

        try {
            new CronTrigger(schedule);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Schedule expression [%s] is incorrect. %s", ex, schedule, ex.getMessage());
        }
    }

    private static <T> T transform(String settingsJson, Class<T> settingsType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(settingsJson, settingsType);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect settings format", ex);
        }
    }

    private static String toJson(Object settingsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(settingsJson);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Incorrect json model");
        }
    }
}

package scraper.module.common.management.module.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.support.CronTrigger;
import scraper.exception.ResourceNotFoundException;
import scraper.exception.ValidationException;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.core.ModuleContainer;
import scraper.module.core.testclasses.TestWorkerModule;
import scraper.module.core.testclasses.TestWorkerSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleStoreServiceTest {

    private ModuleStoreService service;

    @Mock
    private ModuleInstanceDsRepository instanceRepository;

    @Mock
    private ModuleContainer moduleContainer;

    @Mock
    private Scheduler scheduler;

    @Mock
    private ModuleRunner moduleRunner;

    private TestWorkerSettings correctSettings;

    private String correctSettingsStr;

    private TestSettings incorrectTypeSettings;

    private String incorrectTypeSettingsStr;

    private TestWorkerSettings incorrectValueSettings;

    @Before
    public void setUp() {
        service = new ModuleStoreService(instanceRepository, moduleContainer, scheduler, moduleRunner);

        correctSettings = new TestWorkerSettings("option");
        correctSettingsStr = toJson(correctSettings);
        incorrectTypeSettings = new TestSettings(56);
        incorrectTypeSettingsStr = toJson(incorrectTypeSettings);
        incorrectValueSettings = new TestWorkerSettings("optionTooLong");

        TestWorkerModule worker = new TestWorkerModule("module.worker", "description");
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(worker);
    }

    @Test
    public void testGetModuleInstance_noInstance() {
        // given
        when(instanceRepository.findOne(12L)).thenReturn(null);

        // when
        ModuleInstance instance = service.getModuleInstance(12L);

        // then
        assertNull(instance);
    }

    @Test
    public void testGetModuleInstance_noModule() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);

        // when
        try {
            ModuleInstance instance = service.getModuleInstance(12L);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    public void testGetModuleInstance_transformFailed() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", incorrectTypeSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        try {
            ModuleInstance instance = service.getModuleInstance(12L);
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings format"));
        }
    }

    @Test
    public void testGetModuleInstance() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        ModuleInstance instance = service.getModuleInstance(12L);

        // then
        assertEquals(new ModuleInstance(12L, "module.worker", "ins", correctSettings, "0 15 9-17 * * MON-FRI"), instance);
    }

    @Test
    public void testGetModuleInstances_noModule() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findAll()).thenReturn(Collections.singleton(instanceDs));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);

        // when
        try {
            List<ModuleInstance> instances = service.getModuleInstances();
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    public void testGetModuleInstances_noInstances() {
        // given
        when(instanceRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ModuleInstance> instances = service.getModuleInstances();

        // then
        assertTrue(instances.isEmpty());
    }

    @Test
    public void testGetModuleInstances_transformFailed() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", incorrectTypeSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findAll()).thenReturn(Collections.singletonList(instanceDs));

        // when
        try {
            List<ModuleInstance> instances = service.getModuleInstances();
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings format"));
        }
    }

    @Test
    public void testGetModuleInstances() {
        // given
        TestWorkerSettings settings1 = new TestWorkerSettings("option");
        String settingsStr1 = toJson(settings1);
        TestWorkerSettings settings2 = new TestWorkerSettings("option2");
        String settingsStr2 = toJson(settings2);
        ModuleInstanceDs instanceDs1 = new ModuleInstanceDs("module.worker", "ins", settingsStr1, "0 15 9-17 * * MON-FRI");
        ModuleInstanceDs instanceDs2 = new ModuleInstanceDs("module.worker", "ins2", settingsStr2, "0 15 9-17 * * MON-TUE");
        instanceDs1.setId(12L);
        instanceDs2.setId(13L);
        when(instanceRepository.findAll()).thenReturn(Arrays.asList(instanceDs1, instanceDs2));

        // when
        List<ModuleInstance> instances = service.getModuleInstances();

        // then
        ModuleInstance exInstance1 = new ModuleInstance(12L, "module.worker", "ins", settings1, "0 15 9-17 * * MON-FRI");
        ModuleInstance exInstance2 = new ModuleInstance(13L, "module.worker", "ins2", settings2, "0 15 9-17 * * MON-TUE");
        assertEquals(Arrays.asList(exInstance1, exInstance2), instances);
    }

    @Test
    public void testDeleteModuleInstance() {
        // when
        service.deleteModuleInstance(12L);

        // then
        verify(instanceRepository).delete(12L);
        verify(scheduler).cancel(12L);
    }

    @Test
    public void testAddModuleInstance_duplicatedInstance() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(new ModuleInstanceDs("module.worker", "ins", "", "0 15 9-17 * * MON-FRI"));

        // when
        try {
            service.addModuleInstance(new ModuleInstance("module.worker", "ins", correctSettings, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("already exists"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testAddModuleInstance_missingModule() {
        // given
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);

        // when
        try {
            service.addModuleInstance(new ModuleInstance("module.worker", "ins", correctSettings, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testAddModuleInstance_incorrectSettingsType() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);

        // when
        try {
            service.addModuleInstance(new ModuleInstance("module.worker", "ins", incorrectTypeSettings, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings type"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testAddModuleInstance_settingsValidationFailed() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);

        // when
        try {
            service.addModuleInstance(new ModuleInstance("module.worker", "ins", incorrectValueSettings, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (ValidationException ex) {
            // then
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testAddModuleInstance_incorrectSchedule() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);

        // when
        try {
            service.addModuleInstance(new ModuleInstance("module.worker", "ins", correctSettings, "incorrect"));
            fail();
        } catch (ValidationException ex) {
            // then
            assertTrue(ex.getMessage().contains("Schedule"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testAddModuleInstance_nullSchedule() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);
        when(instanceRepository.save(new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, null)))
                .thenReturn(new ModuleInstanceDs(10L, "module.worker", "ins", correctSettingsStr, null));

        // when
        service.addModuleInstance(new ModuleInstance("module.worker", "ins", correctSettings, null));

        // then
        verify(instanceRepository).save(new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, null));
        verify(scheduler).cancel(10L);
    }

    @Test
    public void testAddModuleInstance() {
        // given
        when(instanceRepository.findByModuleNameAndInstanceName("module.worker", "ins")).thenReturn(null);
        when(instanceRepository.save(new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI")))
                .thenReturn(new ModuleInstanceDs(12L, "module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI"));

        // when
        service.addModuleInstance(new ModuleInstance("module.worker", "ins", correctSettings, "0 15 9-17 * * MON-FRI"));

        // then
        verify(instanceRepository).save(new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI"));
        verify(scheduler).schedule(eq(12L), eq(new CronTrigger("0 15 9-17 * * MON-FRI")), any(Runnable.class));
    }

    @Test
    public void testUpdateSettings_missingInstnce() {
        // given
        when(instanceRepository.findOne(12L)).thenReturn(null);

        // when
        try {
            service.updateSettings(12L, correctSettings);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("Instance [id=12] not found"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
    }

    @Test
    public void testUpdateSettings_missingModule() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", toJson(oldSettings), "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        try {
            service.updateSettings(12L, correctSettings);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("Worker Module module.worker not found"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
    }

    @Test
    public void testUpdateSettings_incorrectSettingsType() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", toJson(oldSettings), "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        try {
            service.updateSettings(12L, incorrectTypeSettings);
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings type"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
    }

    @Test
    public void testUpdateSettings_settingsValidationFailed() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", toJson(oldSettings), "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        try {
            service.updateSettings(12L, incorrectValueSettings);
            fail();
        } catch (ValidationException ex) {
            // then
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
    }

    @Test
    public void testUpdateSettings() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", toJson(oldSettings), "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        service.updateSettings(12L, correctSettings);

        // then
        ModuleInstanceDs exInstance = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        exInstance.setId(12L);
        verify(instanceRepository).save(exInstance);
    }

    @Test
    public void testUpdateSchedule_missingInstance() {
        // given
        when(instanceRepository.findOne(12L)).thenReturn(null);

        // when
        try {
            service.updateSchedule(12L, "0 17 9-17 * * MON-FRI");
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("Instance [id=12] not found"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testUpdateSchedule_incorrectSchedule() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        try {
            service.updateSchedule(12L, "incorrect");
            fail();
        } catch (ValidationException ex) {
            // then
            assertTrue(ex.getMessage().contains("Schedule"));
        }

        verify(instanceRepository, never()).save(any(ModuleInstanceDs.class));
        verifyNoMoreInteractions(scheduler);
    }

    @Test
    public void testUpdateSchedule_toNull() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        service.updateSchedule(12L, null);

        // then
        ModuleInstanceDs exInstance = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, null);
        exInstance.setId(12L);
        verify(instanceRepository).save(exInstance);
        verify(scheduler).cancel(12L);
    }

    @Test
    public void testUpdateSchedule() {
        // given
        ModuleInstanceDs instanceDs = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 15 9-17 * * MON-FRI");
        instanceDs.setId(12L);
        when(instanceRepository.findOne(12L)).thenReturn(instanceDs);

        // when
        service.updateSchedule(12L, "0 17 9-17 * * MON-FRI");

        // then
        ModuleInstanceDs exInstance = new ModuleInstanceDs("module.worker", "ins", correctSettingsStr, "0 17 9-17 * * MON-FRI");
        exInstance.setId(12L);
        verify(instanceRepository).save(exInstance);
        verify(scheduler).schedule(eq(12L), eq(new CronTrigger("0 17 9-17 * * MON-FRI")), any(Runnable.class));
    }

    private String toJson(Object settingsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(settingsJson);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Incorrect json model");
        }
    }

    private static class TestSettings {

        private int value;

        public TestSettings(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
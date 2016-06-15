package scraper.module.common.management.module.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.exception.ResourceNotFoundException;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.common.management.module.runner.WorkerDescriptor;
import scraper.module.common.management.module.store.ModuleInstance;
import scraper.module.common.management.module.store.ModuleStoreService;
import scraper.module.core.ModuleContainer;
import scraper.module.core.context.ModuleDetails;
import scraper.module.core.testclasses.TestServiceModule;
import scraper.module.core.testclasses.TestStandaloneModule;
import scraper.module.core.testclasses.TestWorkerModule;
import scraper.module.core.testclasses.TestWorkerSettings;
import scraper.util.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleViewServiceTest {

    private ModuleViewService service;

    @Mock
    private ModuleContainer moduleContainer;

    @Mock
    private ModuleRunner moduleRunner;

    @Mock
    private ModuleStoreService moduleStoreService;

    private TestWorkerSettings correctSettings;

    private ObjectNode correctSettingsJson;

    private TestSettins incorrectTypeSettings;

    private ObjectNode incorrectTypeSettingsJson;

    @Before
    public void setUp() {
        service = new ModuleViewService(moduleContainer, moduleRunner, moduleStoreService);

        correctSettings = new TestWorkerSettings("option");
        correctSettingsJson = toJson(correctSettings);
        incorrectTypeSettings = new TestSettins(44);
        incorrectTypeSettingsJson = toJson(incorrectTypeSettings);

        TestWorkerModule worker = new TestWorkerModule("module.worker", "description");
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(worker);
    }

    @Test
    public void testGetModules_noModules() {
        // given
        when(moduleContainer.getModules()).thenReturn(Collections.emptyMap());

        // when
        List<ModuleDescriptorJsonDto> modules = service.getModules();

        // then
        assertTrue(modules.isEmpty());
    }

    @Test
    public void testGetModules() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("worker name", "worker desc", "worker.dep1");
        TestStandaloneModule standaloneModule = new TestStandaloneModule("standalone name", "standalone desc");
        TestServiceModule serviceModule = new TestServiceModule("service name", "service desc", "service.dep1", "service.dep2");
        when(moduleContainer.getModules()).thenReturn(Utils.map("module.worker", workerModule, "module.standalone", standaloneModule, "module.service", serviceModule));

        // when
        List<ModuleDescriptorJsonDto> modules = service.getModules();

        // then
        ModuleDescriptorJsonDto expectedWorkerJson =
                new ModuleDescriptorJsonDto("worker name", "worker desc", Utils.set("worker.dep1"), ModuleType.WORKER, workerModule.getClassPropertyDescriptor());
        ModuleDescriptorJsonDto expectedStandaloneJson = new ModuleDescriptorJsonDto("standalone name", "standalone desc", Collections.emptySet(), ModuleType.STANDALONE, null);
        ModuleDescriptorJsonDto expectedServiceJson =
                new ModuleDescriptorJsonDto("service name", "service desc", Utils.set("service.dep1", "service.dep2"), ModuleType.SERVICE, null);
        assertEquals(Utils.set(expectedWorkerJson, expectedStandaloneJson, expectedServiceJson), new HashSet<>(modules));
    }

    @Test
    public void testGetModuleInstances_noInstances() {
        // given
        when(moduleStoreService.getModuleInstances()).thenReturn(Collections.emptyList());

        // when
        List<ModuleInstanceJsonReadDto> instances = service.getModuleInstances();

        // then
        assertTrue(instances.isEmpty());
    }

    @Test
    public void testGetModuleInstances() {
        // given
        Object settings1 = "Settings";
        Object settings2 = 12L;
        Object settings3 = new TestWorkerSettings("option");

        ModuleInstance instance1 = new ModuleInstance("module.worker1", "ins1", settings1, "0 15 9-17 * * MON-FRI");
        instance1.setId(1L);
        ModuleInstance instance2 = new ModuleInstance("module.worker1", "ins2", settings2, "0 15 9-16 * * MON-FRI");
        instance2.setId(2L);
        ModuleInstance instance3 = new ModuleInstance("module.worker2", "ins1", settings3, "0 15 9-15 * * MON-FRI");
        instance3.setId(3L);
        when(moduleStoreService.getModuleInstances()).thenReturn(Arrays.asList(instance1, instance2, instance3));

        // when
        List<ModuleInstanceJsonReadDto> instances = service.getModuleInstances();

        // then
        ModuleInstanceJsonReadDto exInstance1 = new ModuleInstanceJsonReadDto(1L, "module.worker1", "ins1", settings1, "0 15 9-17 * * MON-FRI");
        ModuleInstanceJsonReadDto exInstance2 = new ModuleInstanceJsonReadDto(2L, "module.worker1", "ins2", settings2, "0 15 9-16 * * MON-FRI");
        ModuleInstanceJsonReadDto exInstance3 = new ModuleInstanceJsonReadDto(3L, "module.worker2", "ins1", settings3, "0 15 9-15 * * MON-FRI");

        assertEquals(Arrays.asList(exInstance1, exInstance2, exInstance3), instances);
    }

    @Test
    public void testModuleStatuses_noStatuses() {
        // given
        when(moduleRunner.getWorkingWorkers()).thenReturn(Collections.emptyList());

        // when
        List<WorkerDescriptor> statuses = service.getModuleStatuses();

        // then
        assertTrue(statuses.isEmpty());
    }

    @Test
    public void testModuleStatuses() {
        // given
        WorkerDescriptor status1 = mock(WorkerDescriptor.class);
        WorkerDescriptor status2 = mock(WorkerDescriptor.class);
        WorkerDescriptor status3 = mock(WorkerDescriptor.class);
        when(moduleRunner.getWorkingWorkers()).thenReturn(Arrays.asList(status1, status2, status3));

        // when
        List<WorkerDescriptor> statuses = service.getModuleStatuses();

        // then
        assertEquals(Arrays.asList(status1, status2, status3), statuses);
    }

    @Test
    public void testStopWorkerModule() {
        // when
        service.stopWorkerModule("workerId");

        // then
        verify(moduleRunner).stopWorker("workerId");
    }

    @Test
    public void testRunModuleInstance_missingInstance() {
        // given
        when(moduleStoreService.getModuleInstance(15L)).thenReturn(null);

        // when
        try {
            service.runModuleInstance(15L);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    public void testAddModuleInstance_missingModule() {
        // given
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);

        // when
        try {
            service.addModuleInstance(new ModuleInstanceJsonWriteDto("module.worker", "inst", correctSettingsJson, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    public void testAddModuleInstance_incorrectSettingsClass() {
        // when
        try {
            service.addModuleInstance(new ModuleInstanceJsonWriteDto("module.worker", "inst", incorrectTypeSettingsJson, "0 15 9-17 * * MON-FRI"));
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings format"));
        }
    }

    @Test
    public void testAddModuleInstance() {
        // when
        service.addModuleInstance(new ModuleInstanceJsonWriteDto("module.worker", "inst", correctSettingsJson, "0 15 9-17 * * MON-FRI"));

        // then
        verify(moduleStoreService).addModuleInstance(new ModuleInstance("module.worker", "inst", correctSettings, "0 15 9-17 * * MON-FRI"));
    }

    @Test
    public void testUpdateModuleInstnceSettings_missingInstance() {
        // given
        when(moduleStoreService.getModuleInstance(45L)).thenReturn(null);

        // when
        try {
            service.updateModuleInstanceSettings(45L, correctSettingsJson);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("Instance [id=45] not found"));
        }
    }

    @Test
    public void testUpdateModuleInstnceSettings_missingModule() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstance instance = new ModuleInstance("module.worker", "ins", oldSettings, "0 15 9-17 * * MON-FRI");
        when(moduleStoreService.getModuleInstance(45L)).thenReturn(instance);
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);

        // when
        try {
            service.updateModuleInstanceSettings(45L, correctSettingsJson);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("Worker Module module.worker not found"));
        }
    }

    @Test
    public void testUpdateModuleInstnceSettings_incorrectSettingsClass() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstance instance = new ModuleInstance("module.worker", "ins", oldSettings, "0 15 9-17 * * MON-FRI");
        when(moduleStoreService.getModuleInstance(45L)).thenReturn(instance);

        // when
        try {
            service.updateModuleInstanceSettings(45L, incorrectTypeSettingsJson);
            fail();
        } catch (IllegalArgumentException ex) {
            // then
            assertTrue(ex.getMessage().contains("Incorrect settings format"));
        }
    }

    @Test
    public void testUpdateModuleInstnceSettings() {
        // given
        TestWorkerSettings oldSettings = new TestWorkerSettings("oldOption");
        ModuleInstance instance = new ModuleInstance("module.worker", "ins", oldSettings, "0 15 9-17 * * MON-FRI");
        when(moduleStoreService.getModuleInstance(45L)).thenReturn(instance);

        // when
        service.updateModuleInstanceSettings(45L, correctSettingsJson);

        // then
        verify(moduleStoreService).updateSettings(45L, correctSettings);
    }

    @Test
    public void testUpdateModuleInstanceSchedule_nullSchedule() {
        // when
        service.updateModuleInstanceSchedule(45L, null);

        // then
        verify(moduleStoreService).updateSchedule(45L, null);
    }

    @Test
    public void testUpdateModuleInstanceSchedule_incorrectSettings() {
        // when
        service.updateModuleInstanceSchedule(45L, "incorrect");

        // then
        verify(moduleStoreService).updateSchedule(45L, "incorrect");
    }

    @Test
    public void testUpdateModuleInstanceSchedule() {
        // when
        service.updateModuleInstanceSchedule(45L, "0 15 9-17 * * MON-FRI");

        // then
        verify(moduleStoreService).updateSchedule(45L, "0 15 9-17 * * MON-FRI");
    }

    @Test
    public void testRunModuleInstance() {
        // given
        when(moduleStoreService.getModuleInstance(17L)).thenReturn(new ModuleInstance("module.worker", "ins1", correctSettings, "0 15 9-17 * * MON-FRI"));

        // when
        service.runModuleInstance(17L);

        // then
        verify(moduleRunner).runWorkerAsync(new ModuleDetails("module.worker", "ins1"), correctSettings);
    }

    @Test
    public void testDeleteModuleInstance() {
        // when
        service.deleteModuleInstance(12L);

        // then
        verify(moduleStoreService).deleteModuleInstance(12L);
    }

    private ObjectNode toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(obj);
    }

    private static class TestSettins {

        private int value;

        public TestSettins(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestSettins other = (TestSettins) o;

            return Utils.computeEq(value, other.value);
        }

        @Override
        public int hashCode() {
            return Utils.computeHash(value);
        }
    }
}
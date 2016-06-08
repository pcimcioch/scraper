package scraper.module.common.management.module.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.common.management.module.runner.ModuleRunner;
import scraper.module.common.management.module.runner.WorkerDescriptor;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleViewServiceTest {

    private ModuleViewService service;

    @Mock
    private ModuleContainer moduleContainer;

    @Mock
    private ModuleRunner moduleRunner;

    @Before
    public void setUp() {
        service = new ModuleViewService(moduleContainer, moduleRunner);
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
    public void testRunModule_moduleMissing() {
        // given
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);

        // when
        try {
            service.runModule("module.worker", toJson(new TestWorkerSettings("test")), "ins");
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }

        // then
        verifyNoMoreInteractions(moduleRunner);
    }

    @Test
    public void testRunModule_incorrectSettings() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("module.worker", "description");
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);

        // when
        try {
            service.runModule("module.worker", toJson(new TestIncorrectSettings("test", 12)), "ins");
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Incorrect settings"));
        }

        // then
        verifyNoMoreInteractions(moduleRunner);
    }

    @Test
    public void testRunModule() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("module.worker", "description");
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);

        // when
        service.runModule("module.worker", toJson(new TestWorkerSettings("test")), "ins");

        // then
        verify(moduleRunner).runWorkerAsync(new ModuleDetails("module.worker", "ins"), new TestWorkerSettings("test"));
    }

    private ObjectNode toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(obj);
    }

    private static class TestIncorrectSettings {

        private String value;

        private int number;

        public TestIncorrectSettings() {
        }

        public TestIncorrectSettings(String value, int number) {
            this.value = value;
            this.number = number;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestIncorrectSettings other = (TestIncorrectSettings) o;

            return Utils.computeEq(value, other.value, number, other.number);
        }

        @Override
        public int hashCode() {
            return Utils.computeHash(value, number);
        }
    }
}
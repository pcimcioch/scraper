package scraper.module.core;

import org.junit.Test;
import scraper.exception.ValidationException;
import scraper.module.core.testclasses.TestServiceModule;
import scraper.module.core.testclasses.TestStandaloneModule;
import scraper.module.core.testclasses.TestWorkerModule;
import scraper.util.Utils;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ModuleContainerTest {

    @Test
    public void testNoModules() {
        // given
        ModuleContainer container = new ModuleContainer(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        // when then
        assertFalse(container.existsModule("module"));
        assertNull(container.getWorkerModule("module"));
        assertNull(container.getServiceModule("module"));
        assertNull(container.getStandaloneModule("module"));
        assertTrue(container.getModules().isEmpty());
        assertTrue(container.getWorkerModules().isEmpty());
        assertTrue(container.getServiceModules().isEmpty());
        assertTrue(container.getStandaloneModules().isEmpty());
    }

    @Test
    public void testOneWorkerModules() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("module", "description");
        ModuleContainer container = new ModuleContainer(Collections.singletonList(workerModule), Collections.emptyList(), Collections.emptyList());

        // when then
        assertTrue(container.existsModule("module"));
        assertSame(workerModule, container.getWorkerModule("module"));
        assertNull(container.getServiceModule("module"));
        assertNull(container.getStandaloneModule("module"));
        assertEquals(Utils.map("module", workerModule), container.getModules());
        assertEquals(Utils.map("module", workerModule), container.getWorkerModules());
        assertTrue(container.getServiceModules().isEmpty());
        assertTrue(container.getStandaloneModules().isEmpty());
    }

    @Test
    public void testOneServiceModules() {
        // given
        TestServiceModule serviceModule = new TestServiceModule("module", "description");
        ModuleContainer container = new ModuleContainer(Collections.emptyList(), Collections.singletonList(serviceModule), Collections.emptyList());

        // when then
        assertTrue(container.existsModule("module"));
        assertNull(container.getWorkerModule("module"));
        assertSame(serviceModule, container.getServiceModule("module"));
        assertNull(container.getStandaloneModule("module"));
        assertEquals(Utils.map("module", serviceModule), container.getModules());
        assertTrue(container.getWorkerModules().isEmpty());
        assertEquals(Utils.map("module", serviceModule), container.getServiceModules());
        assertTrue(container.getStandaloneModules().isEmpty());
    }

    @Test
    public void testOneStandaloneModules() {
        // given
        TestStandaloneModule standaloneModule = new TestStandaloneModule("module", "description");
        ModuleContainer container = new ModuleContainer(Collections.emptyList(), Collections.emptyList(), Collections.singletonList(standaloneModule));

        // when then
        assertTrue(container.existsModule("module"));
        assertNull(container.getWorkerModule("module"));
        assertNull(container.getServiceModule("module"));
        assertSame(standaloneModule, container.getStandaloneModule("module"));
        assertEquals(Utils.map("module", standaloneModule), container.getModules());
        assertTrue(container.getWorkerModules().isEmpty());
        assertTrue(container.getServiceModules().isEmpty());
        assertEquals(Utils.map("module", standaloneModule), container.getStandaloneModules());
    }

    @Test
    public void testOneOfEachModule() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("module.worker", "description 1", "module.service");
        TestServiceModule serviceModule = new TestServiceModule("module.service", "description 2", "module.standalone");
        TestStandaloneModule standaloneModule = new TestStandaloneModule("module.standalone", "description 3");
        ModuleContainer container =
                new ModuleContainer(Collections.singletonList(workerModule), Collections.singletonList(serviceModule), Collections.singletonList(standaloneModule));

        // when then
        assertTrue(container.existsModule("module.worker"));
        assertTrue(container.existsModule("module.service"));
        assertTrue(container.existsModule("module.standalone"));
        assertFalse(container.existsModule("module"));

        assertSame(workerModule, container.getWorkerModule("module.worker"));
        assertSame(serviceModule, container.getServiceModule("module.service"));
        assertSame(standaloneModule, container.getStandaloneModule("module.standalone"));
        assertEquals(Utils.map("module.worker", workerModule, "module.service", serviceModule, "module.standalone", standaloneModule), container.getModules());
        assertEquals(Utils.map("module.worker", workerModule), container.getWorkerModules());
        assertEquals(Utils.map("module.service", serviceModule), container.getServiceModules());
        assertEquals(Utils.map("module.standalone", standaloneModule), container.getStandaloneModules());
    }

    @Test
    public void testMultipleOfEachModule() {
        // given
        TestWorkerModule workerModule1 = new TestWorkerModule("module.worker.1", "description 1", "module.standalone.1");
        TestServiceModule serviceModule1 = new TestServiceModule("module.service.1", "description 2");
        TestStandaloneModule standaloneModule1 = new TestStandaloneModule("module.standalone.1", "description 3", "module.service.2", "module.standalone.2");
        TestWorkerModule workerModule2 = new TestWorkerModule("module.worker.2", "description 4");
        TestServiceModule serviceModule2 = new TestServiceModule("module.service.2", "description 5", "module.service.1");
        TestStandaloneModule standaloneModule2 = new TestStandaloneModule("module.standalone.2", "description 6");
        ModuleContainer container = new ModuleContainer(Arrays.asList(workerModule1, workerModule2), Arrays.asList(serviceModule1, serviceModule2),
                Arrays.asList(standaloneModule1, standaloneModule2));

        // when then
        assertTrue(container.existsModule("module.worker.1"));
        assertTrue(container.existsModule("module.service.1"));
        assertTrue(container.existsModule("module.standalone.1"));
        assertTrue(container.existsModule("module.worker.2"));
        assertTrue(container.existsModule("module.service.2"));
        assertTrue(container.existsModule("module.standalone.2"));
        assertFalse(container.existsModule("module"));

        assertSame(workerModule1, container.getWorkerModule("module.worker.1"));
        assertSame(serviceModule1, container.getServiceModule("module.service.1"));
        assertSame(standaloneModule1, container.getStandaloneModule("module.standalone.1"));
        assertSame(workerModule2, container.getWorkerModule("module.worker.2"));
        assertSame(serviceModule2, container.getServiceModule("module.service.2"));
        assertSame(standaloneModule2, container.getStandaloneModule("module.standalone.2"));

        assertEquals(Utils.map("module.worker.1", workerModule1, "module.service.1", serviceModule1, "module.standalone.1", standaloneModule1, "module.worker.2", workerModule2,
                "module.service.2", serviceModule2, "module.standalone.2", standaloneModule2), container.getModules());
        assertEquals(Utils.map("module.worker.1", workerModule1, "module.worker.2", workerModule2), container.getWorkerModules());
        assertEquals(Utils.map("module.service.1", serviceModule1, "module.service.2", serviceModule2), container.getServiceModules());
        assertEquals(Utils.map("module.standalone.1", standaloneModule1, "module.standalone.2", standaloneModule2), container.getStandaloneModules());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingDependencies() {
        // given
        TestWorkerModule workerModule = new TestWorkerModule("module.worker", "description 1", "module.service");
        TestServiceModule serviceModule = new TestServiceModule("module.service", "description 2", "module.standalone");
        ModuleContainer ignored = new ModuleContainer(Collections.singletonList(workerModule), Collections.singletonList(serviceModule), Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleModulesWithSameNameAndSameType() {
        // given
        TestServiceModule serviceModule1 = new TestServiceModule("module", "description 1");
        TestServiceModule serviceModule2 = new TestServiceModule("module", "description 2");
        ModuleContainer ignored = new ModuleContainer(Collections.emptyList(), Arrays.asList(serviceModule1, serviceModule2), Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleModulesWithSameNameAndDifferentType() {
        // given
        TestServiceModule serviceModule = new TestServiceModule("module", "description 1");
        TestStandaloneModule standaloneModule = new TestStandaloneModule("module", "description 2");
        ModuleContainer ignored = new ModuleContainer(Collections.emptyList(), Collections.singletonList(serviceModule), Collections.singletonList(standaloneModule));
    }

    @Test(expected = ValidationException.class)
    public void testIncorrectModuleName() {
        // given
        TestServiceModule serviceModule = new TestServiceModule("module_name", "description 1");
        TestStandaloneModule standaloneModule = new TestStandaloneModule("module", "description 2");
        ModuleContainer ignored = new ModuleContainer(Collections.emptyList(), Collections.singletonList(serviceModule), Collections.singletonList(standaloneModule));
    }
}
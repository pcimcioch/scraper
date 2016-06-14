package scraper.module.common.management.module.runner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import scraper.exception.ResourceNotFoundException;
import scraper.module.common.logger.LoggerService;
import scraper.module.core.ModuleContainer;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.module.core.testclasses.TestWorkerModule;
import scraper.module.core.testclasses.TestWorkerSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scraper.util.FuncUtils.map;

@RunWith(MockitoJUnitRunner.class)
public class ModuleRunnerTest {

    private ModuleRunner runner;

    @Spy
    private ModuleContext moduleContext = new ModuleContext();

    @Mock
    private LoggerService logger;

    @Mock
    private ModuleContainer moduleContainer;

    @Mock
    private SimpMessagingTemplate template;

    @Before
    public void setUp() {
        runner = new ModuleRunner(moduleContext, logger, moduleContainer, template);
    }

    @Test
    public void testStopWorker_noWorkers() {
        // when
        boolean stopped = runner.stopWorker("incorrect");

        // then
        assertFalse(stopped);
    }

    @Test
    public void testStopWorker_incorrectId() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");

        // when
        Thread t = startWorker(moduleDetails, settings);
        runner.stopWorker("incorrect");

        workerModule.stop();
        t.join();

        // then
        assertEquals(moduleDetails, moduleContext.getModuleDetails());
        assertFalse(moduleContext.isStopped());
        verify(workerModule).call(settings);
        verify(template, times(2)).convertAndSend(eq(ModuleRunner.STATUS_TOPIC), any(WorkerDescriptor.class));
    }

    @Test
    public void testStopWorker() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");

        // when
        Thread t = startWorker(moduleDetails, settings);
        List<WorkerDescriptor> workingBefore = runner.getWorkingWorkers();
        assertEquals(1, workingBefore.size());
        runner.stopWorker(workingBefore.get(0).getId());

        workerModule.stop();
        t.join();

        // then
        assertEquals(moduleDetails, moduleContext.getModuleDetails());
        assertTrue(moduleContext.isStopped());
        verify(workerModule).call(settings);
        verify(template, times(3)).convertAndSend(eq(ModuleRunner.STATUS_TOPIC), any(WorkerDescriptor.class));
    }

    @Test
    public void testGetWorkingWorkers_noWorkers() {
        // when
        List<WorkerDescriptor> working = runner.getWorkingWorkers();

        // then
        assertTrue(working.isEmpty());
    }

    @Test
    public void testStartWorker() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");

        // when
        Thread t = startWorker(moduleDetails, settings);

        List<WorkerDescriptor> workingBefore = runner.getWorkingWorkers();
        workerModule.stop();
        t.join();
        List<WorkerDescriptor> workingAfter = runner.getWorkingWorkers();

        // then
        assertEquals(Collections.singletonList(moduleDetails), map(workingBefore, WorkerDescriptor::getModuleDetails));
        assertTrue(workingAfter.isEmpty());
        assertEquals(moduleDetails, moduleContext.getModuleDetails());
        verify(workerModule).call(settings);
        verify(template, times(2)).convertAndSend(eq(ModuleRunner.STATUS_TOPIC), any(WorkerDescriptor.class));
    }

    @Test
    public void testStartWorker_throwsException() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");
        doThrow(new IllegalArgumentException("test")).when(workerModule).call(settings);

        // when
        Thread t = startWorker(moduleDetails, settings);
        t.join();
        List<WorkerDescriptor> workingAfter = runner.getWorkingWorkers();

        // then
        assertTrue(workingAfter.isEmpty());
        assertEquals(moduleDetails, moduleContext.getModuleDetails());
        verify(workerModule).call(settings);
        verify(template, never()).convertAndSend(eq(ModuleRunner.STATUS_TOPIC), any(WorkerDescriptor.class));
    }

    @Test
    public void testStartWorkers_multipleWorkers() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails1 = new ModuleDetails("module.worker", "ins1");
        TestWorkerSettings settings1 = new TestWorkerSettings("test1");
        ModuleDetails moduleDetails2 = new ModuleDetails("module.worker", "ins2");
        TestWorkerSettings settings2 = new TestWorkerSettings("test2");

        // when
        Thread t1 = startWorker(moduleDetails1, settings1);
        Thread t2 = startWorker(moduleDetails2, settings2);

        List<WorkerDescriptor> workingBefore = runner.getWorkingWorkers();
        workerModule.stop();
        t1.join();
        t2.join();
        List<WorkerDescriptor> workingAfter = runner.getWorkingWorkers();

        // then
        assertEquals(Arrays.asList(moduleDetails1, moduleDetails2), map(workingBefore, WorkerDescriptor::getModuleDetails));
        assertTrue(workingAfter.isEmpty());
        verify(workerModule).call(settings1);
        verify(workerModule).call(settings2);
    }

    @Test
    public void testStartWorker_missingModule() throws InterruptedException {
        // given
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(null);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");

        // when
        try {
            runner.runWorkerAsync(moduleDetails, settings);
            fail();
        } catch (ResourceNotFoundException ex) {
            // then
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    public void testStartWorker_workerAlreadyWorking() throws InterruptedException {
        // given
        TestWorkerModule workerModule = spy(new TestWorkerModule(logger, moduleContext, "module.worker", "description"));
        when(moduleContainer.getWorkerModule("module.worker")).thenReturn(workerModule);
        ModuleDetails moduleDetails = new ModuleDetails("module.worker", "ins");
        TestWorkerSettings settings = new TestWorkerSettings("test");

        // when
        Thread t = startWorker(moduleDetails, settings);
        try {
            runner.runWorkerAsync(moduleDetails, settings);
            fail();
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("already in progress"));
        }

        List<WorkerDescriptor> workingBefore = runner.getWorkingWorkers();
        workerModule.stop();
        t.join();
        List<WorkerDescriptor> workingAfter = runner.getWorkingWorkers();

        // then
        assertEquals(Collections.singletonList(moduleDetails), map(workingBefore, WorkerDescriptor::getModuleDetails));
        assertTrue(workingAfter.isEmpty());
        assertEquals(moduleDetails, moduleContext.getModuleDetails());
        verify(workerModule).call(settings);
        verify(template, times(2)).convertAndSend(eq(ModuleRunner.STATUS_TOPIC), any(WorkerDescriptor.class));
    }

    private Thread startWorker(ModuleDetails moduleDetails, Object settings) throws InterruptedException {
        Thread thread = new Thread(() -> runner.runWorkerAsync(moduleDetails, settings));
        thread.start();
        Thread.sleep(100);

        return thread;
    }
}
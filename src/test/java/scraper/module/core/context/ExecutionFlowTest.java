package scraper.module.core.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionFlowTest {

    @Spy
    private TestObserver observer = new TestObserver();

    @Test
    public void testStop_noObserver() {
        // given
        ExecutionFlow flow = new ExecutionFlow();

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.stop();

        // then
        assertFlow(flow, true, false, 0, 0, 0, 0);
    }

    @Test
    public void testSetRunning_noObserver() {
        // given
        ExecutionFlow flow = new ExecutionFlow();

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.setRunning(true);

        // then
        assertFlow(flow, false, true, 0, 0, 0, 0);
    }

    @Test
    public void testStop_observer() {
        // given
        ExecutionFlow flow = new ExecutionFlow();
        flow.setObserver(observer);

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.stop();

        // then
        assertFlow(flow, true, false, 0, 0, 0, 0);
        verify(observer).accept(any());
        List<ExecutionFlow> flows = observer.getExecutionFlows();
        assertEquals(1, flows.size());
        assertFlow(flows.get(0), true, false, 0, 0, 0, 0);
    }

    @Test
    public void testStop_observerThrowsException() {
        // given
        ExecutionFlow flow = new ExecutionFlow();
        flow.setObserver(observer);
        doThrow(new IllegalArgumentException("Test")).when(observer).accept(any());

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.stop();

        // then
        assertFlow(flow, true, false, 0, 0, 0, 0);
        verify(observer).accept(any());
    }

    @Test
    public void testSetRunning_observer() {
        // given
        ExecutionFlow flow = new ExecutionFlow();
        flow.setObserver(observer);

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.setRunning(true);

        // then
        assertFlow(flow, false, true, 0, 0, 0, 0);
        verify(observer).accept(any());
        List<ExecutionFlow> flows = observer.getExecutionFlows();
        assertEquals(1, flows.size());
        assertFlow(flows.get(0), false, true, 0, 0, 0, 0);
    }

    @Test
    public void testChangeStatus_observer() {
        // given
        ExecutionFlow flow = new ExecutionFlow();
        flow.setObserver(observer);

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.getStatus().setSteps(4);

        // then
        assertFlow(flow, false, false, 4, 0, 0, 0);
        verify(observer).accept(any());
        List<ExecutionFlow> flows = observer.getExecutionFlows();
        assertEquals(1, flows.size());
        assertFlow(flows.get(0), false, false, 4, 0, 0, 0);
    }

    @Test
    public void testMultipleUpdates_observer() {
        // given
        ExecutionFlow flow = new ExecutionFlow();
        flow.setObserver(observer);

        // sanity
        assertFlow(flow, false, false, 0, 0, 0, 0);

        // when
        flow.setRunning(true);
        flow.getStatus().setSteps(3);
        flow.getStatus().incrementCurrentStep();
        flow.getStatus().setSubSteps(2);
        flow.getStatus().incrementCurrentSubStep();
        flow.stop();
        flow.setRunning(false);

        // then
        assertFlow(flow, true, false, 3, 1, 2, 1);
        verify(observer, times(7)).accept(any());
        List<ExecutionFlow> flows = observer.getExecutionFlows();
        assertEquals(7, flows.size());
        assertFlow(flows.get(0), false, true, 0, 0, 0, 0);
        assertFlow(flows.get(1), false, true, 3, 0, 0, 0);
        assertFlow(flows.get(2), false, true, 3, 1, 0, 0);
        assertFlow(flows.get(3), false, true, 3, 1, 2, 0);
        assertFlow(flows.get(4), false, true, 3, 1, 2, 1);
        assertFlow(flows.get(5), true, true, 3, 1, 2, 1);
        assertFlow(flows.get(6), true, false, 3, 1, 2, 1);
    }

    private static void assertFlow(ExecutionFlow flow, boolean stopped, boolean running, int steps, int currentStep, int subSteps, int currentSubStep) {
        assertEquals(stopped, flow.isStopped());
        assertEquals(running, flow.isRunning());

        Status status = flow.getStatus();
        assertEquals(steps, status.getSteps());
        assertEquals(currentStep, status.getCurrentStep());
        assertEquals(subSteps, status.getSubSteps());
        assertEquals(currentSubStep, status.getCurrentSubStep());
    }

    private static class TestObserver implements Consumer<ExecutionFlow> {

        private List<ExecutionFlow> executionFlows = new ArrayList<>();

        @Override
        public void accept(ExecutionFlow executionFlow) {
            executionFlows.add(new ExecutionFlow(executionFlow));
        }

        public List<ExecutionFlow> getExecutionFlows() {
            return executionFlows;
        }
    }
}
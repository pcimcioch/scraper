package scraper.module.core.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StatusTest {

    @Spy
    private TestObserver observer = new TestObserver();

    @Test
    public void testStatusConstructor() {
        // given
        Status status1 = new Status(4, 3, 2, 1);

        // when
        Status status2 = new Status(status1);

        // then
        assertEquals(status1, status2);
        assertEquals(4, status2.getSteps());
        assertEquals(3, status2.getCurrentStep());
        assertEquals(2, status2.getSubSteps());
        assertEquals(1, status2.getCurrentSubStep());
    }

    @Test
    public void testSetSteps_noObserver() {
        // given
        Status status = new Status();

        // when
        status.setSteps(5);

        // then
        Status expected = new Status(5, 0, 0, 0);
        assertEquals(expected, status);
    }

    @Test
    public void testSetSubSteps_noObserver() {
        // given
        Status status = new Status();

        // when
        status.setSubSteps(7);

        // then
        Status expected = new Status(0, 0, 7, 0);
        assertEquals(expected, status);
    }

    @Test
    public void testIncrementCurrentStep_noObserver() {
        // given
        Status status = new Status(5, 3, 6, 2);

        // when
        status.incrementCurrentStep();

        // then
        Status expected = new Status(5, 4, 0, 0);
        assertEquals(expected, status);
    }

    @Test
    public void testIncrementCurrentSubStep_noObserver() {
        // given
        Status status = new Status(5, 3, 6, 2);

        // when
        status.incrementCurrentSubStep();

        // then
        Status expected = new Status(5, 3, 6, 3);
        assertEquals(expected, status);
    }

    @Test
    public void testSetSteps_observer() {
        // given
        Status status = new Status();
        status.setObserver(observer);

        // when
        status.setSteps(5);

        // then
        Status expected = new Status(5, 0, 0, 0);
        assertEquals(expected, status);
        verify(observer).accept(any());
        assertEquals(Collections.singletonList(expected), observer.getStatuses());
    }

    @Test
    public void testSetSubSteps_observer() {
        // given
        Status status = new Status();
        status.setObserver(observer);

        // when
        status.setSubSteps(7);

        // then
        Status expected = new Status(0, 0, 7, 0);
        assertEquals(expected, status);
        verify(observer).accept(any());
        assertEquals(Collections.singletonList(expected), observer.getStatuses());
    }

    @Test
    public void testIncrementCurrentStep_observer() {
        // given
        Status status = new Status(5, 3, 6, 2);
        status.setObserver(observer);

        // when
        status.incrementCurrentStep();

        // then
        Status expected = new Status(5, 4, 0, 0);
        assertEquals(expected, status);
        verify(observer).accept(any());
        assertEquals(Collections.singletonList(expected), observer.getStatuses());
    }

    @Test
    public void testIncrementCurrentSubStep_observer() {
        // given
        Status status = new Status(5, 3, 6, 2);
        status.setObserver(observer);

        // when
        status.incrementCurrentSubStep();

        // then
        Status expected = new Status(5, 3, 6, 3);
        assertEquals(expected, status);
        verify(observer).accept(any());
        assertEquals(Collections.singletonList(expected), observer.getStatuses());
    }

    @Test
    public void testObserverThrowsException() {
        // given
        Status status = new Status();
        status.setObserver(observer);
        doThrow(new IllegalArgumentException("Test")).when(observer).accept(any());

        // when
        status.setSteps(5);

        // then
        Status expected = new Status(5, 0, 0, 0);
        assertEquals(expected, status);
        verify(observer).accept(any());
    }

    @Test
    public void testMultipleStatusUpdates() {
        // given
        Status status = new Status();
        status.setObserver(observer);

        // when
        status.setSteps(2);
        status.setSubSteps(3);
        status.incrementCurrentSubStep();
        status.incrementCurrentSubStep();
        status.incrementCurrentSubStep();
        status.incrementCurrentStep();
        status.setSubSteps(2);
        status.incrementCurrentSubStep();
        status.incrementCurrentSubStep();
        status.incrementCurrentStep();

        // then
        Status expected = new Status(2, 2, 0, 0);
        assertEquals(expected, status);
        verify(observer, times(10)).accept(any());
        assertEquals(Arrays.asList(new Status(2, 0, 0, 0), new Status(2, 0, 3, 0), new Status(2, 0, 3, 1), new Status(2, 0, 3, 2), new Status(2, 0, 3, 3), new Status(2, 1, 0, 0),
                new Status(2, 1, 2, 0), new Status(2, 1, 2, 1), new Status(2, 1, 2, 2), new Status(2, 2, 0, 0)), observer.getStatuses());
    }

    private static class TestObserver implements Consumer<Status> {

        private List<Status> statuses = new ArrayList<>();

        @Override
        public void accept(Status st) {
            this.statuses.add(new Status(st));
        }

        public List<Status> getStatuses() {
            return statuses;
        }
    }
}
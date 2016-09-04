package scraper.module.common.management.module.store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {

    @Mock
    private ScheduledFuture future1;

    @Mock
    private ScheduledFuture future2;

    @Mock
    private ScheduledFuture future3;

    @Mock
    private Runnable callback;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private SchedulerRunner runner;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private Scheduler scheduler;

    private Trigger trigger = new CronTrigger("* * * * * *");

    @Before
    public void setUp() {
        scheduler = new Scheduler(taskScheduler, runner);
    }

    @Test
    public void testSchedule_callbackThrowsException() {
        // given
        when(taskScheduler.schedule(any(Runnable.class), eq(trigger))).thenReturn(future1);
        doThrow(new IllegalArgumentException("Test")).when(callback).run();

        // when
        scheduler.schedule(12L, trigger, callback);

        // then
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));
        verify(taskScheduler).schedule(runnableCaptor.capture(), eq(trigger));
        verifyRunnable(runnableCaptor.getValue());
    }

    @Test
    public void testSchedule_override() {
        // given
        when(taskScheduler.schedule(any(Runnable.class), eq(trigger))).thenReturn(future1, future2, future3);

        // when
        scheduler.schedule(12L, trigger, callback);
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));

        scheduler.schedule(13L, trigger, callback);
        assertTrue(scheduler.isScheduled(12L));
        assertTrue(scheduler.isScheduled(13L));

        scheduler.schedule(12L, trigger, callback);
        assertTrue(scheduler.isScheduled(12L));
        assertTrue(scheduler.isScheduled(13L));

        // then
        verify(taskScheduler, times(3)).schedule(any(Runnable.class), eq(trigger));
        verify(future1).cancel(false);
    }

    @Test
    public void testSchedule() {
        // given
        when(taskScheduler.schedule(any(Runnable.class), eq(trigger))).thenReturn(future1);

        // when
        scheduler.schedule(12L, trigger, callback);

        // then
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));
        verify(taskScheduler).schedule(runnableCaptor.capture(), eq(trigger));
        verifyRunnable(runnableCaptor.getValue());
    }

    @Test
    public void testCancel_noTasks() {
        // when
        scheduler.cancel(12L);

        // then
        assertFalse(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));
    }

    @Test
    public void testCancel_incorrectTask() {
        // given
        when(taskScheduler.schedule(any(Runnable.class), eq(trigger))).thenReturn(future1);

        // when
        scheduler.schedule(12L, trigger, callback);
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));

        scheduler.cancel(13L);

        // then
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));
    }

    @Test
    public void testCancel() {
        // given
        when(taskScheduler.schedule(any(Runnable.class), eq(trigger))).thenReturn(future1);

        // when
        scheduler.schedule(12L, trigger, callback);
        assertTrue(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));

        scheduler.cancel(12L);
        assertFalse(scheduler.isScheduled(12L));
        assertFalse(scheduler.isScheduled(13L));

        // then
        verify(future1).cancel(false);
    }

    private void verifyRunnable(Runnable runnable) {
        // sanity
        verify(runner, never()).safeRun(any());

        // when
        runnable.run();

        // then
        verify(runner).safeRun(callback);
    }
}
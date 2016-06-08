package scraper.module.common.management.application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.environment.LifeCycle;
import scraper.module.common.logger.LoggerService;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLifecycleServiceTest {

    private ApplicationLifecycleService service;

    @Mock
    private LoggerService logger;

    @Mock
    private LifeCycle lifecycle;

    @Before
    public void setUp() {
        service = new ApplicationLifecycleService(logger, lifecycle);
    }

    @Test
    public void testStopApplication() {
        // when
        service.stopApplication();

        // then
        verify(lifecycle).finish();
    }
}
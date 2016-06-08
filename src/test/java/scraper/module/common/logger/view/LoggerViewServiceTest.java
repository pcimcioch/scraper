package scraper.module.common.logger.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.common.logger.LogEntryDs;
import scraper.module.common.logger.LogEntryDsRepository;
import scraper.module.common.logger.LoggerLevel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggerViewServiceTest {

    private LoggerViewService service;

    @Mock
    private LogEntryDsRepository repository;

    @Before
    public void setUp() {
        service = new LoggerViewService(repository);
    }

    @Test
    public void testGetAllLogs_noLogs() {
        // given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<LogEntryJsonDto> logs = service.getAllLogs();

        // then
        assertTrue(logs.isEmpty());
    }

    @Test
    public void testGetAllLogs() {
        // given
        Date date1 = new Date(1234L);
        Date date2 = new Date(2345L);
        Date date3 = new Date(3456L);

        LogEntryDs log1 = new LogEntryDs(LoggerLevel.ERROR, "module1", "instance1", date1, "message1");
        LogEntryDs log2 = new LogEntryDs(LoggerLevel.INFO, "module2", "instance2", date2, "message2");
        LogEntryDs log3 = new LogEntryDs(LoggerLevel.WARNING, "module3", "instance3", date3, "message3");
        when(repository.findAll()).thenReturn(Arrays.asList(log1, log2, log3));

        // when
        List<LogEntryJsonDto> logs = service.getAllLogs();

        // then
        assertEquals(Arrays.asList(new LogEntryJsonDto(LoggerLevel.ERROR, "module1", "instance1", date1, "message1"),
                new LogEntryJsonDto(LoggerLevel.INFO, "module2", "instance2", date2, "message2"),
                new LogEntryJsonDto(LoggerLevel.WARNING, "module3", "instance3", date3, "message3")), logs);
    }

    @Test
    public void testDeleteAllLogs() {
        // when
        service.deleteAllLogs();

        // then
        verify(repository).deleteAll();
    }

    @Test
    public void testDeleteModuleLogs() {
        // given
        LogEntryDs log1 = mock(LogEntryDs.class);
        LogEntryDs log2 = mock(LogEntryDs.class);
        LogEntryDs log3 = mock(LogEntryDs.class);
        when(repository.findByModule("module")).thenReturn(Arrays.asList(log1, log2, log3));

        // when
        service.removeModuleLogs("module");

        // verify
        verify(repository, times(3)).delete(any(LogEntryDs.class));
        verify(repository).delete(log1);
        verify(repository).delete(log2);
        verify(repository).delete(log3);
    }

    @Test
    public void testDeleteModuleLogs_nothingToDelete() {
        when(repository.findByModule("module")).thenReturn(Collections.emptyList());

        // when
        service.removeModuleLogs("module");

        // verify
        verify(repository, never()).delete(any(LogEntryDs.class));
    }
}
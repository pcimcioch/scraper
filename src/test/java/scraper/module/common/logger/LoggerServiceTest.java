package scraper.module.common.logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggerServiceTest {

    private LoggerService service;

    @Mock
    private ModuleContext moduleContext;

    @Mock
    private LogEntryDsRepository logRepository;

    @Captor
    private ArgumentCaptor<LogEntryDs> logEntryCaptor;

    @Before
    public void setUp() {
        service = new LoggerService(moduleContext, logRepository, LoggerLevel.TRACE, LoggerLevel.TRACE);
        stub(moduleContext.getModuleDetails()).toReturn(new ModuleDetails("moduleName", "instance"));
    }

    @Test
    public void testLog_message() {
        // when
        service.log(LoggerLevel.INFO, "Info message");

        // then
        verifyLog(LoggerLevel.INFO, "Info message");
    }

    @Test
    public void testLog_formattedMessage() {
        // when
        service.log(LoggerLevel.TRACE, "Info message %s %s", "test", "-bar");

        // then
        verifyLog(LoggerLevel.TRACE, "Info message test -bar");
    }

    @Test
    public void testLog_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.log(LoggerLevel.WARNING, "Info message", cause);

        // then
        verifyLog(LoggerLevel.WARNING, "Info message");
    }

    @Test
    public void testLog_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.log(LoggerLevel.ERROR, "Info message %d", cause, 1078);

        // then
        verifyLog(LoggerLevel.ERROR, "Info message 1078");
    }

    @Test
    public void testTrace_message() {
        // when
        service.trace("Trace message");

        // then
        verifyLog(LoggerLevel.TRACE, "Trace message");
    }

    @Test
    public void testTrace_formattedMessage() {
        // when
        service.trace("Trace message %s %d", "test", 12);

        // then
        verifyLog(LoggerLevel.TRACE, "Trace message test 12");
    }

    @Test
    public void testTrace_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.trace("Trace message", cause);

        // then
        verifyLog(LoggerLevel.TRACE, "Trace message");
    }

    @Test
    public void testDebug_message() {
        // when
        service.debug("Debug message");

        // then
        verifyLog(LoggerLevel.DEBUG, "Debug message");
    }

    @Test
    public void testDebug_formattedMessage() {
        // when
        service.debug("Debug message %s %d%s", "t", 2, "b");

        // then
        verifyLog(LoggerLevel.DEBUG, "Debug message t 2b");
    }

    @Test
    public void testDebug_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.debug("Debug message", cause);

        // then
        verifyLog(LoggerLevel.DEBUG, "Debug message");
    }

    @Test
    public void testDebug_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.debug("Debug message %d", cause, 5);

        // then
        verifyLog(LoggerLevel.DEBUG, "Debug message 5");
    }

    @Test
    public void testTrace_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.trace("Trace message %d %s", cause, 10, "foo");

        // then
        verifyLog(LoggerLevel.TRACE, "Trace message 10 foo");
    }

    @Test
    public void testInfo_message() {
        // when
        service.info("Info message");

        // then
        verifyLog(LoggerLevel.INFO, "Info message");
    }

    @Test
    public void testInfo_formattedMessage() {
        // when
        service.info("Info message %s %s", "test", "-bar");

        // then
        verifyLog(LoggerLevel.INFO, "Info message test -bar");
    }

    @Test
    public void testInfo_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.info("Info message", cause);

        // then
        verifyLog(LoggerLevel.INFO, "Info message");
    }

    @Test
    public void testInfo_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.info("Info message %d", cause, 1078);

        // then
        verifyLog(LoggerLevel.INFO, "Info message 1078");
    }

    @Test
    public void testWarn_message() {
        // when
        service.warn("Warn message");

        // then
        verifyLog(LoggerLevel.WARNING, "Warn message");
    }

    @Test
    public void testWarn_formattedMessage() {
        // when
        service.warn("Warn message %s %d%s", "t", 2, "b");

        // then
        verifyLog(LoggerLevel.WARNING, "Warn message t 2b");
    }

    @Test
    public void testWarn_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.warn("Warn message", cause);

        // then
        verifyLog(LoggerLevel.WARNING, "Warn message");
    }

    @Test
    public void testWarn_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.warn("Warn message %d", cause, 5);

        // then
        verifyLog(LoggerLevel.WARNING, "Warn message 5");
    }

    @Test
    public void testError_message() {
        // when
        service.error("Error message");

        // then
        verifyLog(LoggerLevel.ERROR, "Error message");
    }

    @Test
    public void testError_formattedMessage() {
        // when
        service.error("Error message %d %d", 44, 52L);

        // then
        verifyLog(LoggerLevel.ERROR, "Error message 44 52");
    }

    @Test
    public void testError_messageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.error("Error message", cause);

        // then
        verifyLog(LoggerLevel.ERROR, "Error message");
    }

    @Test
    public void testError_formattedMessageAndCause() {
        // given
        IOException cause = new IOException("Test");

        // when
        service.error("Error message %s", cause, "foo");

        // then
        verifyLog(LoggerLevel.ERROR, "Error message foo");
    }

    private void verifyLog(LoggerLevel level, String message) {
        verify(logRepository).save(logEntryCaptor.capture());
        LogEntryDs actualLogEntry = logEntryCaptor.getValue();

        assertEquals(level, actualLogEntry.getLevel());
        assertEquals("moduleName", actualLogEntry.getModule());
        assertEquals("instance", actualLogEntry.getInstance());
        assertEquals(message, actualLogEntry.getMessage());
        assertNotNull(actualLogEntry.getDate());
    }
}
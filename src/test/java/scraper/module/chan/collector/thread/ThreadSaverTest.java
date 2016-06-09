package scraper.module.chan.collector.thread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.chan.collector.BoardProcessedThreadDs;
import scraper.module.chan.collector.BoardProcessedThreadDsRepository;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ThreadSaverTest {

    @Mock
    private BoardProcessedThreadDsRepository boardProcessedThreadRepository;

    @Mock
    private ThreadDsRepository threadRepository;

    private ThreadSaver saver;

    @Before
    public void setUp() {
        saver = new ThreadSaver(boardProcessedThreadRepository, threadRepository);
    }

    @Test
    public void testSaveThread() {
        // given
        ThreadDs thread = new ThreadDs("1", "board", "subject");

        // when
        saver.saveThread(thread);

        // then
        verify(threadRepository).save(thread);
        verify(boardProcessedThreadRepository).save(new BoardProcessedThreadDs("board", "1"));
    }
}
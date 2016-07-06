package scraper.module.chan.collector.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.chan.collector.BoardProcessedThreadDs;
import scraper.module.chan.collector.BoardProcessedThreadDsRepository;

@Service
// TODO remove this class when BoardProcessedThreadDsRepository will be removed
public class ThreadSaver {

    private final BoardProcessedThreadDsRepository boardProcessedThreadRepository;

    private final ThreadDsRepository threadRepository;

    @Autowired
    public ThreadSaver(BoardProcessedThreadDsRepository boardProcessedThreadRepository, ThreadDsRepository threadRepository) {
        this.boardProcessedThreadRepository = boardProcessedThreadRepository;
        this.threadRepository = threadRepository;
    }

    @Neo4jTransactional
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveThread(ThreadDs thread) {
        threadRepository.save(thread);
        boardProcessedThreadRepository.save(new BoardProcessedThreadDs(thread.getBoard(), thread.getThreadId()));
    }
}

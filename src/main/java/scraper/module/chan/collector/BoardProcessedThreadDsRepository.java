package scraper.module.chan.collector;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardProcessedThreadDsRepository extends CrudRepository<BoardProcessedThreadDs, Long> {

    List<BoardProcessedThreadDs> findByBoardName(String boardName);
}

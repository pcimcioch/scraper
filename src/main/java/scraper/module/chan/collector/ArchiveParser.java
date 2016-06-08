package scraper.module.chan.collector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static scraper.util.FuncUtils.filter;
import static scraper.util.FuncUtils.map;
import static scraper.util.FuncUtils.mapFilterSet;

@Service
public class ArchiveParser {

    private static final Pattern LINK_PATTERN = Pattern.compile(".*/thread/(.+)#.*");

    private static final String BOARD_REGEX = "(?s)\\/(.*?)\\/.*";

    private final BoardProcessedThreadDsRepository boardProcessedThreadRepo;

    @Autowired
    public ArchiveParser(BoardProcessedThreadDsRepository boardProcessedThreadRepo) {
        this.boardProcessedThreadRepo = boardProcessedThreadRepo;
    }

    @Neo4jTransactional
    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> parseArchive(Document document) {
        String board = extractBoardTitle(document);
        Set<String> threadIds = getThreadIds(document);
        filterUnprocessedIds(board, threadIds);

        return buildLinks(board, threadIds);
    }

    private Set<String> getThreadIds(Element element) {
        Elements elements = element.select("#arc-list a");
        return mapFilterSet(elements, el -> idFromLink(el.attr("href")), Objects::nonNull);
    }

    private String idFromLink(String link) {
        Matcher m = LINK_PATTERN.matcher(link);
        if (m.matches()) {
            return m.group(1);
        }

        return null;
    }

    private void filterUnprocessedIds(String board, Set<String> ids) {
        if (ids.isEmpty()) {
            return;
        }

        List<BoardProcessedThreadDs> processed = boardProcessedThreadRepo.findByBoardName(board);
        List<BoardProcessedThreadDs> historicToRemove = filter(processed, bpt -> !ids.contains(bpt.getThreadId()));
        ids.removeAll(map(processed, BoardProcessedThreadDs::getThreadId));
        boardProcessedThreadRepo.delete(historicToRemove);
    }

    private List<String> buildLinks(String board, Set<String> ids) {
        return map(ids, id -> String.format("http://boards.4chan.org/%s/thread/%s", board, id));
    }

    private String extractBoardTitle(Element element) {
        Element elem = element.select("title").first();
        String fullBoard = elem == null ? "" : elem.text();
        return StringUtils.getSingleMatch(fullBoard, BOARD_REGEX, 1, "");
    }
}

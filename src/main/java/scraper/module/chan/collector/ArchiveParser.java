package scraper.module.chan.collector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import scraper.util.StringUtils;
import scraper.util.structure.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static scraper.util.FuncUtils.map;
import static scraper.util.FuncUtils.mapFilterSet;

/**
 * Parser for 4chan archived threads page.
 * <p>
 * Will extract all archived thread links from DOM DOcument. Uses neo4j database to keep information about already extracted threads.
 */
@Service
public class ArchiveParser {

    private static final Pattern LINK_PATTERN = Pattern.compile(".*/thread/(.+)#.*");

    private static final String BOARD_REGEX = "(?s)\\/(.*?)\\/.*";

    /**
     * Parses given DOM {@code document}, and extracts links to archived threads.
     *
     * @param document document
     * @return list of pairs [thread id, url]
     */
    public List<Pair<String, String>> parseArchive(Document document) {
        String board = extractBoardTitle(document);
        Set<String> threadIds = getThreadIds(document);

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

    private List<Pair<String, String>> buildLinks(String board, Set<String> ids) {
        return map(ids, id -> new Pair<>(id, String.format("http://boards.4chan.org/%s/thread/%s", board, id)));
    }

    private String extractBoardTitle(Element element) {
        Element elem = element.select("title").first();
        String fullBoard = elem == null ? "" : elem.text();
        return StringUtils.getSingleMatch(fullBoard, BOARD_REGEX, 1, "");
    }
}

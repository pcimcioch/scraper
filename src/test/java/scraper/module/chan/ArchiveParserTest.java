package scraper.module.chan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.chan.collector.ArchiveParser;
import scraper.util.Utils;
import scraper.util.structure.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ArchiveParserTest {

    private ArchiveParser parser = new ArchiveParser();

    @Test
    public void testParseLinks() throws IOException {
        // given
        Document doc = getDocument("/scraper/module/chan/test1.html");

        // when
        List<Pair<String, String>> links = parser.parseArchive(doc);

        // then
        Set<Pair<String, String>> expectedLinks =
                Utils.set(new Pair<>("914875", "http://boards.4chan.org/wsg/thread/914875"), new Pair<>("914648", "http://boards.4chan.org/wsg/thread/914648"),
                        new Pair<>("933834", "http://boards.4chan.org/wsg/thread/933834"), new Pair<>("918293", "http://boards.4chan.org/wsg/thread/918293"),
                        new Pair<>("915270", "http://boards.4chan.org/wsg/thread/915270"), new Pair<>("937553", "http://boards.4chan.org/wsg/thread/937553"),
                        new Pair<>("937897", "http://boards.4chan.org/wsg/thread/937897"), new Pair<>("910942", "http://boards.4chan.org/wsg/thread/910942"),
                        new Pair<>("936141", "http://boards.4chan.org/wsg/thread/936141"), new Pair<>("833726", "http://boards.4chan.org/wsg/thread/833726"),
                        new Pair<>("904456", "http://boards.4chan.org/wsg/thread/904456"), new Pair<>("937873", "http://boards.4chan.org/wsg/thread/937873"),
                        new Pair<>("936692", "http://boards.4chan.org/wsg/thread/936692"), new Pair<>("934376", "http://boards.4chan.org/wsg/thread/934376"),
                        new Pair<>("931235", "http://boards.4chan.org/wsg/thread/931235"), new Pair<>("866201", "http://boards.4chan.org/wsg/thread/866201"),
                        new Pair<>("934426", "http://boards.4chan.org/wsg/thread/934426"), new Pair<>("933818", "http://boards.4chan.org/wsg/thread/933818"),
                        new Pair<>("907417", "http://boards.4chan.org/wsg/thread/907417"), new Pair<>("903103", "http://boards.4chan.org/wsg/thread/903103"),
                        new Pair<>("931122", "http://boards.4chan.org/wsg/thread/931122"), new Pair<>("933276", "http://boards.4chan.org/wsg/thread/933276"),
                        new Pair<>("923792", "http://boards.4chan.org/wsg/thread/923792"), new Pair<>("907219", "http://boards.4chan.org/wsg/thread/907219"),
                        new Pair<>("897749", "http://boards.4chan.org/wsg/thread/897749"), new Pair<>("934011", "http://boards.4chan.org/wsg/thread/934011"));

        assertEquals(expectedLinks.size(), links.size());
        assertTrue(expectedLinks.containsAll(links));
    }

    private Document getDocument(String path) throws IOException {
        InputStream in = getClass().getResourceAsStream(path);
        return Jsoup.parse(in, StandardCharsets.UTF_8.name(), "http://boards.4chan.org/");
    }
}

package scraper.module.chan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.chan.collector.ArchiveParser;
import scraper.module.chan.collector.BoardProcessedThreadDs;
import scraper.module.chan.collector.BoardProcessedThreadDsRepository;
import scraper.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArchiveParserTest {

    private ArchiveParser parser;

    @Mock
    private BoardProcessedThreadDsRepository boardProcessedThreadRepo;

    @Before
    public void setUp() {
        parser = new ArchiveParser(boardProcessedThreadRepo);
    }

    @Test
    public void testParseLinks() throws IOException {
        // given
        Document doc = getDocument("/scraper/module/chan/test1.html");
        stub(boardProcessedThreadRepo.findByBoardName("wsg")).toReturn(Collections.emptyList());

        // when
        List<String> links = parser.parseArchive(doc);

        // then
        Set<String> expectedLinks = Utils.set("http://boards.4chan.org/wsg/thread/914875", "http://boards.4chan.org/wsg/thread/914648", "http://boards.4chan.org/wsg/thread/933834",
                "http://boards.4chan.org/wsg/thread/918293", "http://boards.4chan.org/wsg/thread/915270", "http://boards.4chan.org/wsg/thread/937553",
                "http://boards.4chan.org/wsg/thread/937897", "http://boards.4chan.org/wsg/thread/910942", "http://boards.4chan.org/wsg/thread/936141",
                "http://boards.4chan.org/wsg/thread/833726", "http://boards.4chan.org/wsg/thread/904456", "http://boards.4chan.org/wsg/thread/937873",
                "http://boards.4chan.org/wsg/thread/936692", "http://boards.4chan.org/wsg/thread/934376", "http://boards.4chan.org/wsg/thread/931235",
                "http://boards.4chan.org/wsg/thread/866201", "http://boards.4chan.org/wsg/thread/934426", "http://boards.4chan.org/wsg/thread/933818",
                "http://boards.4chan.org/wsg/thread/907417", "http://boards.4chan.org/wsg/thread/903103", "http://boards.4chan.org/wsg/thread/931122",
                "http://boards.4chan.org/wsg/thread/933276", "http://boards.4chan.org/wsg/thread/923792", "http://boards.4chan.org/wsg/thread/907219",
                "http://boards.4chan.org/wsg/thread/897749", "http://boards.4chan.org/wsg/thread/934011");
        assertEquals(expectedLinks, new HashSet<>(links));
        verify(boardProcessedThreadRepo).delete(Collections.emptyList());
        verify(boardProcessedThreadRepo).findByBoardName("wsg");
    }

    @Test
    public void testParseLinks_historicLinks() throws IOException {
        // given
        Document doc = getDocument("/scraper/module/chan/test1.html");
        BoardProcessedThreadDs processed1 = new BoardProcessedThreadDs("wsg", "933834");
        BoardProcessedThreadDs processed2 = new BoardProcessedThreadDs("wsg", "915270");
        BoardProcessedThreadDs processed3 = new BoardProcessedThreadDs("wsg", "123456");
        BoardProcessedThreadDs processed4 = new BoardProcessedThreadDs("wsg", "654321");
        stub(boardProcessedThreadRepo.findByBoardName("wsg")).toReturn(Arrays.asList(processed1, processed2, processed3, processed4));

        // when
        List<String> links = parser.parseArchive(doc);

        // then
        Set<String> expectedLinks = Utils.set("http://boards.4chan.org/wsg/thread/914875", "http://boards.4chan.org/wsg/thread/914648", "http://boards.4chan.org/wsg/thread/918293",
                "http://boards.4chan.org/wsg/thread/937553", "http://boards.4chan.org/wsg/thread/937897", "http://boards.4chan.org/wsg/thread/910942",
                "http://boards.4chan.org/wsg/thread/936141", "http://boards.4chan.org/wsg/thread/833726", "http://boards.4chan.org/wsg/thread/904456",
                "http://boards.4chan.org/wsg/thread/937873", "http://boards.4chan.org/wsg/thread/936692", "http://boards.4chan.org/wsg/thread/934376",
                "http://boards.4chan.org/wsg/thread/931235", "http://boards.4chan.org/wsg/thread/866201", "http://boards.4chan.org/wsg/thread/934426",
                "http://boards.4chan.org/wsg/thread/933818", "http://boards.4chan.org/wsg/thread/907417", "http://boards.4chan.org/wsg/thread/903103",
                "http://boards.4chan.org/wsg/thread/931122", "http://boards.4chan.org/wsg/thread/933276", "http://boards.4chan.org/wsg/thread/923792",
                "http://boards.4chan.org/wsg/thread/907219", "http://boards.4chan.org/wsg/thread/897749", "http://boards.4chan.org/wsg/thread/934011");
        assertEquals(expectedLinks, new HashSet<>(links));
        verify(boardProcessedThreadRepo).delete(Arrays.asList(processed3, processed4));
        verify(boardProcessedThreadRepo).findByBoardName("wsg");
    }

    private Document getDocument(String path) throws IOException {
        InputStream in = getClass().getResourceAsStream(path);
        return Jsoup.parse(in, StandardCharsets.UTF_8.name(), "http://boards.4chan.org/");
    }
}

package scraper.module.chan.thread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.chan.collector.thread.FileDownloader;
import scraper.module.chan.collector.thread.PostDs;
import scraper.module.chan.collector.thread.ThreadDs;
import scraper.module.chan.collector.thread.ThreadParser;
import scraper.module.core.context.ModuleContext;
import scraper.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static scraper.util.FuncUtils.mapSet;

@RunWith(MockitoJUnitRunner.class)
public class ThreadParserTest {

    private ThreadParser parser;

    @Mock
    private FileDownloader fileDownloader;

    private ModuleContext moduleContext = new ModuleContext();

    @Before
    public void setUp() {
        parser = new ThreadParser(fileDownloader, moduleContext);
    }

    @Test
    public void testSimpleThread() throws IOException {
        // given
        Document document = getDocument("/scraper/module/chan/thread/test1.html");

        // when
        ThreadDs thread = parser.parseThread(document, true);

        // then
        assertThread(thread, "6474195", "wg", "", Utils.set("6474195", "6474196", "6474197", "6474283", "6474293"));
        List<PostDs> posts = thread.getOrderdPosts();

        assertPost(posts.get(0), "6474195", "Anonymous", new GregorianCalendar(2016, 1, 10, 11, 59, 31).getTime(), thread, "Fc5rw3qHBpHuQ8g3qS1qJQ==", "1444079794632.jpg",
                "Urban environments being reclaimed by nature. Buildings being assimilated by trees, grass breaking through pavement, that sort of thing.", Collections.emptySet(),
                Collections.emptySet());
        assertPost(posts.get(1), "6474196", "Anonymous", new GregorianCalendar(2016, 1, 10, 11, 59, 55).getTime(), thread, "qBkGMlMQpJh4UH6p4uaavQ==", "1444079570311.jpg", "",
                Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(2), "6474197", "Anonymous", new GregorianCalendar(2016, 1, 10, 12, 0, 34).getTime(), thread, "jnZ47piQcCkIhNbSv1PXoQ==", "1440912477020.jpg", "",
                Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(3), "6474283", "Clara", new GregorianCalendar(2016, 1, 10, 13, 54, 22).getTime(), thread, "701VqL+l//Vew01MgkxWag==", "1446413525727.jpg",
                "Ooh. *prepares to stalk thread*", Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(4), "6474293", "Clara", new GregorianCalendar(2016, 1, 10, 14, 0, 19).getTime(), thread, "xY+PcAOKJWrN1pe9QUUVOQ==", "0509 - U3mRDNj.jpg", "",
                Collections.emptySet(), Collections.emptySet());

        verify(fileDownloader, times(5)).tryDownload(anyString(), anyString(), anyString());
        verify(fileDownloader).tryDownload("Fc5rw3qHBpHuQ8g3qS1qJQ==", "http://i.4cdn.org/wg/1455123571756.jpg", "http://i.4cdn.org/wg/1455123571756s.jpg");
        verify(fileDownloader).tryDownload("qBkGMlMQpJh4UH6p4uaavQ==", "http://i.4cdn.org/wg/1455123595514.jpg", "http://i.4cdn.org/wg/1455123595514s.jpg");
        verify(fileDownloader).tryDownload("jnZ47piQcCkIhNbSv1PXoQ==", "http://i.4cdn.org/wg/1455123634967.jpg", "http://i.4cdn.org/wg/1455123634967s.jpg");
        verify(fileDownloader).tryDownload("701VqL+l//Vew01MgkxWag==", "http://i.4cdn.org/wg/1455130462415.jpg", "http://i.4cdn.org/wg/1455130462415s.jpg");
        verify(fileDownloader).tryDownload("xY+PcAOKJWrN1pe9QUUVOQ==", "http://i.4cdn.org/wg/1455130819995.jpg", "http://i.4cdn.org/wg/1455130819995s.jpg");
    }

    @Test
    public void testSimpleThread_dontDownloadFiles() throws IOException {
        // given
        Document document = getDocument("/scraper/module/chan/thread/test1.html");

        // when
        ThreadDs thread = parser.parseThread(document, false);

        // then
        assertThread(thread, "6474195", "wg", "", Utils.set("6474195", "6474196", "6474197", "6474283", "6474293"));
        List<PostDs> posts = thread.getOrderdPosts();

        assertPost(posts.get(0), "6474195", "Anonymous", new GregorianCalendar(2016, 1, 10, 11, 59, 31).getTime(), thread, "Fc5rw3qHBpHuQ8g3qS1qJQ==", "1444079794632.jpg",
                "Urban environments being reclaimed by nature. Buildings being assimilated by trees, grass breaking through pavement, that sort of thing.", Collections.emptySet(),
                Collections.emptySet());
        assertPost(posts.get(1), "6474196", "Anonymous", new GregorianCalendar(2016, 1, 10, 11, 59, 55).getTime(), thread, "qBkGMlMQpJh4UH6p4uaavQ==", "1444079570311.jpg", "",
                Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(2), "6474197", "Anonymous", new GregorianCalendar(2016, 1, 10, 12, 0, 34).getTime(), thread, "jnZ47piQcCkIhNbSv1PXoQ==", "1440912477020.jpg", "",
                Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(3), "6474283", "Clara", new GregorianCalendar(2016, 1, 10, 13, 54, 22).getTime(), thread, "701VqL+l//Vew01MgkxWag==", "1446413525727.jpg",
                "Ooh. *prepares to stalk thread*", Collections.emptySet(), Collections.emptySet());
        assertPost(posts.get(4), "6474293", "Clara", new GregorianCalendar(2016, 1, 10, 14, 0, 19).getTime(), thread, "xY+PcAOKJWrN1pe9QUUVOQ==", "0509 - U3mRDNj.jpg", "",
                Collections.emptySet(), Collections.emptySet());

        verify(fileDownloader, never()).tryDownload(anyString(), anyString(), anyString());
    }

    @Test
    public void testComplexThread() throws IOException {
        // given
        Document document = getDocument("/scraper/module/chan/thread/test2.html");

        // when
        ThreadDs thread = parser.parseThread(document, true);

        // then
        assertThread(thread, "6480486", "wg", "MERICA'N WALLPAPERS!", Utils.set("6480486", "6480524", "6481166", "6481167", "6481219", "6481249", "6481250", "6481251", "6481730"));
        List<PostDs> posts = thread.getOrderdPosts();

        assertPost(posts.get(0), "6480486", "Anonymous", new GregorianCalendar(2016, 1, 15, 13, 44, 37).getTime(), thread, "4ZBB1DyGKb1X9ZqEGpkwAA==", "hugemericans.jpg",
                "Merican' wallpaper thread! I wanna see the most american wallpaper you guys can come up with! Mine is missing the cheesburgers! POST!", Collections.emptySet(),
                Utils.set("6481219"));
        assertPost(posts.get(1), "6480524", "Anonymous", new GregorianCalendar(2016, 1, 15, 14, 39, 59).getTime(), thread, "JkzHoV5mrIQe5Awe9FSPrg==", "1452922935046.jpg", "",
                Collections.emptySet(), Utils.set("6481219"));
        assertPost(posts.get(2), "6481166", "Anonymous", new GregorianCalendar(2016, 1, 15, 22, 52, 12).getTime(), thread, "NfeWVXMqNY1rpx4Ded8Nwg==", "1452038899014.jpg", "",
                Collections.emptySet(), Utils.set("6481219", "6481251"));
        assertPost(posts.get(3), "6481167", "Anonymous", new GregorianCalendar(2016, 1, 15, 22, 52, 45).getTime(), thread, "rzYkVaqgT6GLLzxEBRXL0Q==", "1452364318919.png", "",
                Collections.emptySet(), Utils.set("6481219", "6481250", "6481730"));
        assertPost(posts.get(4), "6481219", "Anonymous", new GregorianCalendar(2016, 1, 15, 23, 42, 18).getTime(), thread, "", "",
                ">>6480486 >>6480524 >>6481166 >>6481167 SO DAMN LAME!", Utils.set("6481167", "6481166", "6480524", "6480486"), Utils.set("6481249"));
        assertPost(posts.get(5), "6481249", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 11, 21).getTime(), thread, "EtijY/vtm5uta+YKL39Cyg==", "America.jpg", ">>6481219",
                Utils.set("6481219"), Collections.emptySet());
        assertPost(posts.get(6), "6481250", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 12, 0).getTime(), thread, "CRUIPDWzS30/HuLMY84BeQ==", "FMF.jpg", ">>6481167",
                Utils.set("6481167"), Collections.emptySet());
        assertPost(posts.get(7), "6481251", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 13, 6).getTime(), thread, "D9C+VWCL40zudT5vr/7KrA==", "Freedom.png", ">>6481166",
                Utils.set("6481166"), Collections.emptySet());
        assertPost(posts.get(8), "6481730", "Anonymous", new GregorianCalendar(2016, 1, 16, 14, 33, 18).getTime(), thread, "r8bDNW81yvzVQoyyIewyTg==", "1455637885341.png",
                ">>6481167", Utils.set("6481167"), Collections.emptySet());

        verify(fileDownloader, times(8)).tryDownload(anyString(), anyString(), anyString());
        verify(fileDownloader).tryDownload("4ZBB1DyGKb1X9ZqEGpkwAA==", "http://i.4cdn.org/wg/1455561877984.jpg", "http://i.4cdn.org/wg/1455561877984s.jpg");
        verify(fileDownloader).tryDownload("JkzHoV5mrIQe5Awe9FSPrg==", "http://i.4cdn.org/wg/1455565199245.jpg", "http://i.4cdn.org/wg/1455565199245s.jpg");
        verify(fileDownloader).tryDownload("NfeWVXMqNY1rpx4Ded8Nwg==", "http://i.4cdn.org/wg/1455594732851.jpg", "http://i.4cdn.org/wg/1455594732851s.jpg");
        verify(fileDownloader).tryDownload("rzYkVaqgT6GLLzxEBRXL0Q==", "http://i.4cdn.org/wg/1455594765334.png", "http://i.4cdn.org/wg/1455594765334s.jpg");
        verify(fileDownloader).tryDownload("EtijY/vtm5uta+YKL39Cyg==", "http://i.4cdn.org/wg/1455599481619.jpg", "http://i.4cdn.org/wg/1455599481619s.jpg");
        verify(fileDownloader).tryDownload("CRUIPDWzS30/HuLMY84BeQ==", "http://i.4cdn.org/wg/1455599520874.jpg", "http://i.4cdn.org/wg/1455599520874s.jpg");
        verify(fileDownloader).tryDownload("D9C+VWCL40zudT5vr/7KrA==", "http://i.4cdn.org/wg/1455599586482.png", "http://i.4cdn.org/wg/1455599586482s.jpg");
        verify(fileDownloader).tryDownload("r8bDNW81yvzVQoyyIewyTg==", "http://i.4cdn.org/wg/1455651198292.png", "http://i.4cdn.org/wg/1455651198292s.jpg");
    }

    @Test
    public void testComplexThread_dontDownloadFiles() throws IOException {
        // given
        Document document = getDocument("/scraper/module/chan/thread/test2.html");

        // when
        ThreadDs thread = parser.parseThread(document, false);

        // then
        assertThread(thread, "6480486", "wg", "MERICA'N WALLPAPERS!", Utils.set("6480486", "6480524", "6481166", "6481167", "6481219", "6481249", "6481250", "6481251", "6481730"));
        List<PostDs> posts = thread.getOrderdPosts();

        assertPost(posts.get(0), "6480486", "Anonymous", new GregorianCalendar(2016, 1, 15, 13, 44, 37).getTime(), thread, "4ZBB1DyGKb1X9ZqEGpkwAA==", "hugemericans.jpg",
                "Merican' wallpaper thread! I wanna see the most american wallpaper you guys can come up with! Mine is missing the cheesburgers! POST!", Collections.emptySet(),
                Utils.set("6481219"));
        assertPost(posts.get(1), "6480524", "Anonymous", new GregorianCalendar(2016, 1, 15, 14, 39, 59).getTime(), thread, "JkzHoV5mrIQe5Awe9FSPrg==", "1452922935046.jpg", "",
                Collections.emptySet(), Utils.set("6481219"));
        assertPost(posts.get(2), "6481166", "Anonymous", new GregorianCalendar(2016, 1, 15, 22, 52, 12).getTime(), thread, "NfeWVXMqNY1rpx4Ded8Nwg==", "1452038899014.jpg", "",
                Collections.emptySet(), Utils.set("6481219", "6481251"));
        assertPost(posts.get(3), "6481167", "Anonymous", new GregorianCalendar(2016, 1, 15, 22, 52, 45).getTime(), thread, "rzYkVaqgT6GLLzxEBRXL0Q==", "1452364318919.png", "",
                Collections.emptySet(), Utils.set("6481219", "6481250", "6481730"));
        assertPost(posts.get(4), "6481219", "Anonymous", new GregorianCalendar(2016, 1, 15, 23, 42, 18).getTime(), thread, "", "",
                ">>6480486 >>6480524 >>6481166 >>6481167 SO DAMN LAME!", Utils.set("6481167", "6481166", "6480524", "6480486"), Utils.set("6481249"));
        assertPost(posts.get(5), "6481249", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 11, 21).getTime(), thread, "EtijY/vtm5uta+YKL39Cyg==", "America.jpg", ">>6481219",
                Utils.set("6481219"), Collections.emptySet());
        assertPost(posts.get(6), "6481250", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 12, 0).getTime(), thread, "CRUIPDWzS30/HuLMY84BeQ==", "FMF.jpg", ">>6481167",
                Utils.set("6481167"), Collections.emptySet());
        assertPost(posts.get(7), "6481251", "Anonymous", new GregorianCalendar(2016, 1, 16, 0, 13, 6).getTime(), thread, "D9C+VWCL40zudT5vr/7KrA==", "Freedom.png", ">>6481166",
                Utils.set("6481166"), Collections.emptySet());
        assertPost(posts.get(8), "6481730", "Anonymous", new GregorianCalendar(2016, 1, 16, 14, 33, 18).getTime(), thread, "r8bDNW81yvzVQoyyIewyTg==", "1455637885341.png",
                ">>6481167", Utils.set("6481167"), Collections.emptySet());

        verify(fileDownloader, never()).tryDownload(anyString(), anyString(), anyString());
    }

    private Document getDocument(String path) throws IOException {
        InputStream in = getClass().getResourceAsStream(path);
        return Jsoup.parse(in, StandardCharsets.UTF_8.name(), "http://boards.4chan.org/");
    }

    private void assertThread(ThreadDs thread, String threadId, String board, String subject, Set<String> postIds) {
        assertEquals(threadId, thread.getThreadId());
        assertEquals(board, thread.getBoard());
        assertEquals(subject, thread.getSubject());
        assertEquals(postIds, mapSet(thread.getPosts(), PostDs::getPostId));
    }

    private void assertPost(PostDs post, String postId, String author, Date date, ThreadDs thread, String md5, String fileName, String comment, Set<String> replyToIds,
            Set<String> repliedByIds) {
        assertEquals(postId, post.getPostId());
        assertEquals(author, post.getAuthor());
        assertEquals(date, post.getDate());
        assertEquals(thread, post.getThread());
        assertEquals(md5, post.getMd5());
        assertEquals(fileName, post.getFileName());
        assertEquals(comment, post.getComment());
        assertEquals(replyToIds, mapSet(post.getReplyTo(), PostDs::getPostId));
        assertEquals(repliedByIds, mapSet(post.getRepliedBy(), PostDs::getPostId));
    }
}

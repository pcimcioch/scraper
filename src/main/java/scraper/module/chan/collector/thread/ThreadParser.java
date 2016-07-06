package scraper.module.chan.collector.thread;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.module.core.context.ModuleContext;
import scraper.util.StringUtils;
import scraper.util.function.ThrowingFunction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static scraper.util.FuncUtils.map;
import static scraper.util.FuncUtils.mapFilter;
import static scraper.util.FuncUtils.toMap;

/**
 * Service responsible for parsing DOM document of 4chan thread to {@link ThreadDs}.
 */
@Service
public class ThreadParser {

    private static final String BOARD_REGEX = "(?s)\\/(.*?)\\/.*";

    private static final String THREAD_ID_REGEX = ".*thread\\/([0-9]+)\\/.*";

    private static final String DATE_FORMAT = "MM/dd/yy(EEE)HH:mm:ss";

    private final FileDownloader fileDownloader;

    private final ModuleContext moduleContext;

    @Autowired
    public ThreadParser(FileDownloader fileDownloader, ModuleContext moduleContext) {
        this.fileDownloader = fileDownloader;
        this.moduleContext = moduleContext;
    }

    /**
     * Parse thread DOM {@code document} to new {@link ThreadDs} instance.
     *
     * @param document      document with thread webpage
     * @param downloadFiles flag indicating, if parses should also trigger downloading files associated with given thread. For file downloading, {@link FileDownloader} will be
     *                      used
     * @return created thread
     * @throws IOException if any IO operation failed, during downloading files associated with thread
     */
    public ThreadDs parseThread(Document document, boolean downloadFiles) throws IOException {
        ThreadDs thread = buildThread(document);
        thread.addPosts(buildPosts(document, downloadFiles));

        return thread;
    }

    private ThreadDs buildThread(Document document) {
        String threadId = extractThreadId(document);
        String board = extractBoardTitle(document);
        String subject = extractSubject(document);

        return new ThreadDs(threadId, board, subject);
    }

    private String extractThreadId(Element element) {
        Element elem = element.select("link[rel=canonical]").first();
        return elem == null ? "" : StringUtils.getSingleMatch(elem.absUrl("href"), THREAD_ID_REGEX, 1, "");
    }

    private String extractBoardTitle(Element element) {
        Element elem = element.select("title").first();
        String fullBoard = elem == null ? "" : elem.text();
        return StringUtils.getSingleMatch(fullBoard, BOARD_REGEX, 1, "");
    }

    private String extractSubject(Element element) {
        Element elem = element.select("span.subject").first();
        return elem == null ? "" : elem.text();
    }

    private List<PostDs> buildPosts(Element element, boolean downloadFiles) throws IOException {
        Elements postElements = element.select("div.post");
        if (downloadFiles) {
            moduleContext.setSubSteps(postElements.size());
        }

        List<PostDs> posts = new ArrayList<>(postElements.size());
        for (Element el : postElements) {
            posts.add(buildPostBase(el, downloadFiles));
            if (downloadFiles) {
                moduleContext.incrementCurrentSubStep();
            }
        }

        Map<String, List<String>> replies = extractReplies(postElements);
        applyReplies(replies, posts);

        return posts;
    }

    private Map<String, List<String>> extractReplies(Collection<Element> elements) {
        Map<String, List<String>> replies = new HashMap<>();
        for (Element element : elements) {
            List<String> replyIds = extractReplies(element);
            if (!replyIds.isEmpty()) {
                String postId = extractPostId(element);
                replies.put(postId, replyIds);
            }
        }

        return replies;
    }

    private void applyReplies(Map<String, List<String>> repliesMap, List<PostDs> posts) {
        Map<String, PostDs> postsMap = toMap(posts, PostDs::getPostId, ThrowingFunction.identity());
        for (Entry<String, List<String>> entry : repliesMap.entrySet()) {
            PostDs post = postsMap.get(entry.getKey());
            if (post != null) {
                List<PostDs> replies = mapFilter(entry.getValue(), postsMap::get, Objects::nonNull);
                post.addReplyTo(replies);
            }
        }
    }

    private PostDs buildPostBase(Element element, boolean downloadFile) throws IOException {
        String author = extractAuthor(element);
        Date date = extractDate(element);
        String postId = extractPostId(element);
        String comment = extractComment(element);
        String fileName = extractFileName(element);
        String md5 = getFileIfNecessery(element, downloadFile);

        return new PostDs(author, date, postId, comment, fileName, md5);
    }

    private String extractAuthor(Element element) {
        Element elem = element.select("div.postInfo span.nameBlock span.name").first();
        return elem == null ? "" : elem.text();
    }

    private Date extractDate(Element element) {
        Element elem = element.select("div.postInfo span.dateTime").first();
        String dateStr = elem == null ? "" : elem.text();

        try {
            return new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dateStr);
        } catch (ParseException e) {
            return new Date(0L);
        }
    }

    private String extractPostId(Element element) {
        Element elem = element.select("div.postInfo input").first();
        return elem == null ? "" : elem.attr("name");
    }

    private String extractComment(Element element) {
        Element elem = element.select("blockquote.postMessage").first();
        return elem == null ? "" : elem.text();
    }

    private String extractFileName(Element element) {
        Element elem = element.select("div.file div.fileText a").first();
        return elem == null ? "" : elem.text();
    }

    private String getFileIfNecessery(Element element, boolean downloadFile) throws IOException {
        String md5 = extractMd5(element);
        String fileUrl = extractFileUrl(element);
        String thumbnailUrl = extractThumbnailUrl(element);

        if (downloadFile && !StringUtils.isAnyBlank(md5, fileUrl, thumbnailUrl)) {
            fileDownloader.tryDownload(md5, fileUrl, thumbnailUrl);
        }
        return md5;
    }

    private String extractMd5(Element element) {
        Element elem = element.select("div.file a.fileThumb img").first();
        return elem == null ? "" : elem.attr("data-md5");
    }

    private String extractFileUrl(Element element) {
        Element elem = element.select("div.file div.fileText a").first();
        return elem == null ? "" : elem.absUrl("href");
    }

    private String extractThumbnailUrl(Element element) {
        Element elem = element.select("div.file a.fileThumb img").first();
        return elem == null ? "" : elem.absUrl("src");
    }

    private List<String> extractReplies(Element element) {
        Elements elems = element.select("blockquote.postMessage a.quotelink");
        return map(elems, el -> el.text().substring(2));
    }
}

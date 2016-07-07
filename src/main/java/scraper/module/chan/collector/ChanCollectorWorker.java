package scraper.module.chan.collector;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.transaction.Neo4jTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scraper.module.chan.collector.thread.ThreadDs;
import scraper.module.chan.collector.thread.ThreadDsRepository;
import scraper.module.chan.collector.thread.ThreadParser;
import scraper.module.common.logger.LoggerService;
import scraper.module.common.service.HtmlService;
import scraper.module.core.context.ModuleContext;
import scraper.util.structure.Pair;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static scraper.util.FuncUtils.mapIf;

@Service
public class ChanCollectorWorker {

    private final LoggerService logger;

    private final HtmlService htmlService;

    private final ModuleContext moduleContext;

    private final ArchiveParser archiveParser;

    private final ThreadParser threadParser;

    private final ThreadDsRepository threadRepository;

    @Autowired
    public ChanCollectorWorker(LoggerService logger, HtmlService htmlService, ModuleContext moduleContext, ArchiveParser archiveParser, ThreadParser threadParser,
            ThreadDsRepository threadRepository) {
        this.logger = logger;
        this.htmlService = htmlService;
        this.moduleContext = moduleContext;
        this.archiveParser = archiveParser;
        this.threadParser = threadParser;
        this.threadRepository = threadRepository;
    }

    @Neo4jTransactional
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doWork(ChanCollectorModuleSettings settings) {
        Document archiveDoc = getArchiveDocument(settings.getBoardName());
        if (archiveDoc == null || moduleContext.isStopped()) {
            return;
        }

        List<String> threadUrls = getThreadUrls(archiveDoc);
        // Reverse, so that oldest threads are scrapped first. They may disapper quickly.
        Collections.reverse(threadUrls);
        moduleContext.setSteps(threadUrls.size());

        for (String threadUrl : threadUrls) {
            moduleContext.incrementCurrentStep();
            if (moduleContext.isStopped()) {
                return;
            }

            scrapThread(threadUrl, settings.getDownloadFiles());
        }
    }

    private Document getArchiveDocument(String board) {
        String boardUrl = String.format("http://boards.4chan.org/%s/archive", board);
        try {
            return htmlService.getDocument(boardUrl);
        } catch (IOException e) {
            logger.warn("Error obtaining archive html: %s", e, e.getMessage());
        }

        return null;
    }

    private List<String> getThreadUrls(Document archiveDoc) {
        List<Pair<String, String>> threads = archiveParser.parseArchive(archiveDoc);
        return mapIf(threads, pair -> threadRepository.findByThreadId(pair.getFirst()) == null, Pair::getSecond);
    }

    private void scrapThread(String threadUrl, boolean downloadFiles) {
        Document threadDoc = getThreadDocument(threadUrl);
        if (threadDoc == null) {
            return;
        }

        ThreadDs thread = getTherad(threadDoc, downloadFiles);
        if (thread != null) {
            threadRepository.save(thread);
        }
    }

    private ThreadDs getTherad(Document threadDoc, boolean downloadFiles) {
        try {
            return threadParser.parseThread(threadDoc, downloadFiles);
        } catch (IOException e) {
            logger.warn("Error building thread: %s", e, e.getMessage());
        }

        return null;
    }

    private Document getThreadDocument(String threadUrl) {
        try {
            return htmlService.getDocument(threadUrl);
        } catch (IOException e) {
            logger.warn("Error obtaining thread html: %s", e, e.getMessage());
        }

        return null;
    }
}

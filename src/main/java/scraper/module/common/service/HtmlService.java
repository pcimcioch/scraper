package scraper.module.common.service;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Service implementing standard http operations.
 */
@Service
public class HtmlService {

    private static final int DOWNLOAD_TIMEOUT = 120000;

    private static final int STANDARD_TIMEOUT = 10000;

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";

    private static final String REFERRER = "http://www.google.com";

    /**
     * Get DOM document from given url.
     *
     * @param url url
     * @return dom document
     * @throws IOException if connection failed. See {@link Connection#execute()} for details
     */
    public Document getDocument(String url) throws IOException {
        return getStandardResponse(url);
    }

    /**
     * Get content from given url and save it to output stream {@code out}.
     *
     * @param url url
     * @param out opened output stream
     * @throws IOException if connection failed or stream operation I/O exception occurred. See {@link Connection#execute()} for details
     */
    public void download(String url, OutputStream out) throws IOException {
        Response response = getDownloadResponse(url);
        out.write(response.bodyAsBytes());
    }

    private Response getDownloadResponse(String url) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).timeout(DOWNLOAD_TIMEOUT).maxBodySize(MAX_FILE_SIZE).ignoreContentType(true).execute();
    }

    private Document getStandardResponse(String url) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).timeout(STANDARD_TIMEOUT).get();
    }
}

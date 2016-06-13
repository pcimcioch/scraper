package scraper.module.common.service;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class HtmlService {

    private static final int DOWNLOAD_TIMEOUT = 120000;

    private static final int STANDARD_TIMEOUT = 10000;

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";

    private static final String REFERRER = "http://www.google.com";

    public Document getDocument(String url) throws IOException {
        System.out.println("Getting " + url);
        return getStandardResponse(url);
    }

    public void download(String url, OutputStream out) throws IOException {
        System.out.println("Download " + url);
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

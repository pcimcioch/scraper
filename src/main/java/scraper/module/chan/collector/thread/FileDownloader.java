package scraper.module.chan.collector.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.module.common.service.HtmlService;
import scraper.module.common.service.WorkspaceService;
import scraper.util.FileUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service responsible for downloading files.
 */
@Service
public class FileDownloader {

    private static final String DEFAULT_EXTENSION = "tmp";

    private final WorkspaceService workspaceService;

    private final HtmlService htmlService;

    @Autowired
    public FileDownloader(WorkspaceService workspaceService, HtmlService htmlService) {
        this.workspaceService = workspaceService;
        this.htmlService = htmlService;
    }

    /**
     * Downloads file and its thumbnail.
     * <p>
     * md5 of the file must be known prior to download. Both, file ulr and thumbnail url are required. File will be saved to this module workspace, as described in {@link
     * WorkspaceService#getModulePath()}.
     * <p>
     * File path will be <tt>[module root]/[first and second char of MD5]/[third and forth char of MD5]/[MD5].[Extension from the url, or "tmp"]</tt> Thumbnail path will be
     * <tt>[module root]/[first and second char of MD5]/[third and forth char of MD5]/[MD5]_s.[Extension from the url, or "tmp"]</tt>
     *
     * @param md5          md5 of the file
     * @param fileUrl      file url
     * @param thumbnailUrl file thumbnail url
     * @throws IOException if IO operation failed
     */
    public void tryDownload(String md5, String fileUrl, String thumbnailUrl) throws IOException {
        System.out.println("Finding file on drive " + md5);

        Path filePath = createFilePath(md5, FileUtils.getExtension(fileUrl, DEFAULT_EXTENSION));
        if (filePath != null) {
            download(filePath, fileUrl);
        }

        Path thumbnailPath = createFilePath(md5 + "_s", FileUtils.getExtension(thumbnailUrl, DEFAULT_EXTENSION));
        if (thumbnailPath != null) {
            download(thumbnailPath, thumbnailUrl);
        }
    }

    private Path createFilePath(String filename, String extension) throws IOException {
        if (filename.length() < 5) {
            return null;
        }

        return workspaceService.createFile(filename.substring(0, 2), filename.substring(2, 4), filename + "." + extension);
    }

    private void download(Path path, String url) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            htmlService.download(url, out);
        }
    }
}

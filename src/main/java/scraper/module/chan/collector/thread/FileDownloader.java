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

@Service
// TODO add tests
public class FileDownloader {

    private static final String DEFAULT_EXTENSION = "tmp";

    private final WorkspaceService workspaceService;

    private final HtmlService htmlService;

    @Autowired
    public FileDownloader(WorkspaceService workspaceService, HtmlService htmlService) {
        this.workspaceService = workspaceService;
        this.htmlService = htmlService;
    }

    public void tryDownload(String md5, String fileUrl, String thumbnailUrl) throws IOException {
        System.out.println("Finding file on drive " + md5);

        Path filePath = createFilePath(md5, FileUtils.getExtension(fileUrl, DEFAULT_EXTENSION));
        if (filePath != null) {
            download(filePath, fileUrl);
        }

        Path thumbnailPath = createFilePath(md5 + "_s", FileUtils.getExtension(thumbnailUrl, DEFAULT_EXTENSION));
        if (thumbnailPath != null) {
            download(thumbnailPath, fileUrl);
        }
    }

    private Path createFilePath(String filename, String extension) throws IOException {
        if (filename.length() < 5) {
            return null;
        }
        String sanitizedFilename = FileUtils.sanitize(filename);

        return workspaceService.createFile(sanitizedFilename.substring(0, 2), sanitizedFilename.substring(2, 4), sanitizedFilename + "." + extension);
    }

    private void download(Path path, String url) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            htmlService.download(url, out);
        }
    }
}

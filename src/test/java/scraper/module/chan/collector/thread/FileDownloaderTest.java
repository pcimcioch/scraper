package scraper.module.chan.collector.thread;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.module.common.service.HtmlService;
import scraper.module.common.service.WorkspaceService;
import scraper.test.FileSystemRule;
import scraper.util.FileUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileDownloaderTest {

    private FileDownloader downloader;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private HtmlService htmlService;

    @Rule
    public final FileSystemRule rule = new FileSystemRule();

    private Path path1;

    private Path path2;

    @Before
    public void setUp() throws IOException {
        downloader = new FileDownloader(workspaceService, htmlService);

        FileSystem fs = rule.getFileSystem();
        path1 = fs.getPath("p1");
        path2 = fs.getPath("p2");
        Files.createFile(path1);
        Files.createFile(path2);

        doAnswer(invocation -> {
            String url = (String) invocation.getArguments()[0];
            OutputStream stream = (OutputStream) invocation.getArguments()[1];
            stream.write(url.getBytes(StandardCharsets.UTF_8));
            return null;
        }).when(htmlService).download(anyString(), any());
    }

    @Test
    public void testTryDownload() throws IOException {
        // given
        when(workspaceService.createFile("12", "34", "123456789.jpg")).thenReturn(path1);
        when(workspaceService.createFile("12", "34", "123456789_s.txt")).thenReturn(path2);

        // when
        downloader.tryDownload("123456789", "foobar.com/file1.jpg", "foobar.com/file2.txt");

        // then
        verifyDownload("foobar.com/file1.jpg", path1);
        verifyDownload("foobar.com/file2.txt", path2);
    }

    @Test
    public void testTryDownload_defaultExtension() throws IOException {
        // given
        when(workspaceService.createFile("12", "34", "123456789.tmp")).thenReturn(path1);
        when(workspaceService.createFile("12", "34", "123456789_s.tmp")).thenReturn(path2);

        // when
        downloader.tryDownload("123456789", "foobar.com/file1", "foobar.com/file2");

        // then
        verifyDownload("foobar.com/file1", path1);
        verifyDownload("foobar.com/file2", path2);
    }

    @Test
    public void testTryDownload_tooShortMd5() throws IOException {
        // when
        downloader.tryDownload("12", "foobar.com/file1.jpg", "foobar.com/file2.txt");

        // then
        verifyNoMoreInteractions(htmlService, workspaceService);
    }

    private void verifyDownload(String url, Path path) throws IOException {
        verify(htmlService).download(eq(url), any());
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);
        assertEquals(url, content);
    }
}
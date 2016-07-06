package scraper.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import scraper.test.FileSystemRule;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileUtilsTest {

    @Rule
    public final FileSystemRule rule = new FileSystemRule();

    private FileSystem fs;

    @Before
    public void setUp() {
        fs = rule.getFileSystem();
    }

    @Test
    public void testSanitize() {
        assertEquals("file123_[]()&^%$#@!.txt", FileUtils.sanitize("file123_[]()&^%$#@!.txt"));
        assertEquals("file", FileUtils.sanitize("file"));
        assertEquals("file.txt", FileUtils.sanitize("file.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file<.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file>.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file:.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file\".txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file/.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file\\.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file|.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file?.txt"));
        assertEquals("file_.txt", FileUtils.sanitize("file*.txt"));
    }

    @Test
    public void testSanitizeArray() {
        assertEquals(0, FileUtils.sanitize().length);
        assertArrayEquals(new String[]{"file1.txt", "file2.tmp", "f3"}, FileUtils.sanitize("file1.txt", "file2.tmp", "f3"));
        assertArrayEquals(new String[]{"file1.txt", "file2_.tmp", "f3_"}, FileUtils.sanitize("file1.txt", "file2?.tmp", "f3*"));
        assertArrayEquals(new String[]{"f_ile1.txt", "_file2.tmp", "f_3"}, FileUtils.sanitize("f<ile1.txt", ">file2.tmp", "f:3"));
    }

    @Test
    public void testGetExtension() {
        assertEquals("txt", FileUtils.getExtension("file.txt"));
        assertEquals("mp3", FileUtils.getExtension("file.txt.mp3"));
        assertEquals("txt", FileUtils.getExtension("file...txt"));
        assertNull(FileUtils.getExtension("file"));
        assertNull(FileUtils.getExtension(""));
    }

    @Test
    public void testGetExtension_defaultValue() {
        assertEquals("txt", FileUtils.getExtension("file.txt", "tmp"));
        assertEquals("mp3", FileUtils.getExtension("file.txt.mp3", "tmp"));
        assertEquals("txt", FileUtils.getExtension("file...txt", "tmp"));
        assertEquals("tmp", FileUtils.getExtension("file", "tmp"));
        assertEquals("tmp", FileUtils.getExtension("", "tmp"));
    }

    @Test
    public void testGetExtension_complexUrl() {
        assertEquals("txt", FileUtils.getExtension("foobar.com/file.txt"));
        assertNull(FileUtils.getExtension("foobar.com/file"));
        assertEquals("txt", FileUtils.getExtension("foobar.com/file", "txt"));
    }

    @Test
    public void testResolve() {
        assertEquals(fs.getPath("one", "two"), FileUtils.resolve(fs.getPath("one"), "two"));
        assertEquals(fs.getPath("one", "two", "three"), FileUtils.resolve(fs.getPath("one", "two"), "three"));
        assertEquals(fs.getPath("one", "two", "three"), FileUtils.resolve(fs.getPath("one"), "two", "three"));
        assertEquals(fs.getPath("one", "two", "three", "four", "five"), FileUtils.resolve(fs.getPath("one", "two"), "three", "four", "five"));
    }

    @Test(expected = NoSuchFileException.class)
    public void testReadFile_noFile() throws IOException {
        // given
        Path path = fs.getPath("file.txt");

        // when
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);
    }

    @Test
    public void testReadFile_emptyFile() throws IOException {
        // given
        Path path = fs.getPath("file.txt");
        Files.createFile(path);

        // when
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);

        // then
        assertEquals("", content);
    }

    @Test
    public void testReadFile() throws IOException {
        // given
        String savedContent = "Some Content\nAnd other stuff";
        Path path = fs.getPath("file.txt");
        Files.createFile(path);
        try (OutputStream out = Files.newOutputStream(path)) {
            out.write(savedContent.getBytes(StandardCharsets.UTF_8));
        }

        // when
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);

        // then
        assertEquals(savedContent, content);
    }
}
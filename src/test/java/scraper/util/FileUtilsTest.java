package scraper.util;

import org.junit.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileUtilsTest {

    private FileSystem fs = FileSystems.getDefault();

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
    public void testResolve() {
        assertEquals(fs.getPath("one", "two"), FileUtils.resolve(fs.getPath("one"), "two"));
        assertEquals(fs.getPath("one", "two", "three"), FileUtils.resolve(fs.getPath("one", "two"), "three"));
        assertEquals(fs.getPath("one", "two", "three"), FileUtils.resolve(fs.getPath("one"), "two", "three"));
        assertEquals(fs.getPath("one", "two", "three", "four", "five"), FileUtils.resolve(fs.getPath("one", "two"), "three", "four", "five"));
    }
}
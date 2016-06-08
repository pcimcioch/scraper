package scraper.environment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import scraper.test.FileSystemRule;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WorkspaceTest {

    private static final String WORKSPACE = "workspace";

    @Rule
    public final FileSystemRule rule = new FileSystemRule();

    private FileSystem fs;

    private Path workspacePath;

    private Workspace workspace;

    @Before
    public void setUp() {
        fs = rule.getFileSystem();
        workspacePath = fs.getPath(FileSystemRule.ROOT, WORKSPACE);
        workspace = new Workspace(workspacePath);
    }

    @Test
    public void testGetRootPath() {
        // when
        Path rootPath = workspace.getRootPath();

        // then
        assertEquals(workspacePath, rootPath);
    }

    @Test
    public void testInitialize() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE);

        // when
        workspace.initWorkspace();

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE);
    }

    @Test
    public void testGetDirectory_createSingle() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir");

        // when
        workspace.getDirectory(fs.getPath("dir"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
    }

    @Test
    public void testGetDirectory_createMultiple() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.getDirectory(fs.getPath("dir", "sub"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");
    }

    @Test
    public void testGetDirectory_createSome() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.getDirectory(fs.getPath("dir", "sub"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");
    }

    @Test
    public void testGetDirectory_createNone() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "sub"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.getDirectory(fs.getPath("dir", "sub"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testGetDirectory_fileExists() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));
        Files.createFile(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "sub"));

        // sanity
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.getDirectory(fs.getPath("dir", "sub"));
    }

    @Test
    public void testCreateDirectory_createSingle() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir");

        // when
        workspace.createDirectory(fs.getPath("dir"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
    }

    @Test
    public void testCreateDirectory_createMultiple() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.createDirectory(fs.getPath("dir", "sub"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");
    }

    @Test
    public void testCreateDirectory_createSome() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.createDirectory(fs.getPath("dir", "sub"));

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateDirectory_createNone() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "sub"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.createDirectory(fs.getPath("dir", "sub"));
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateDirectory_fileExists() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));
        Files.createFile(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "sub"));

        // sanity
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "dir", "sub");

        // when
        workspace.createDirectory(fs.getPath("dir", "sub"));
    }

    @Test
    public void testCreateFile_createSingle() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "file.txt");

        // when
        workspace.createFile(fs.getPath("file.txt"));

        // then
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "file.txt");
    }

    @Test
    public void testCreateFile_createMultiple() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");

        // when
        workspace.createFile(fs.getPath("dir", "file.txt"));

        // then
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");
    }

    @Test
    public void testCreateFile_createSome() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");

        // when
        workspace.createFile(fs.getPath("dir", "file.txt"));

        // then
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateFile_createNone() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));
        Files.createFile(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt"));

        // sanity
        assertExistsFile(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");

        // when
        workspace.createFile(fs.getPath("dir", "file.txt"));
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateFile_directoryExists() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir", "file.txt");

        // when
        workspace.createFile(fs.getPath("dir", "file.txt"));
    }

    @Test
    public void testCreateRandomFile_createSubDirectory() throws IOException {
        // sanity
        assertNotExists(FileSystemRule.ROOT, WORKSPACE, "dir");

        // when
        createRandomFiles();

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
    }

    @Test
    public void testCreateRandomFile_existingSubDirectory() throws IOException {
        // given
        Files.createDirectories(fs.getPath(FileSystemRule.ROOT, WORKSPACE, "dir"));

        // sanity
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");

        // when
        createRandomFiles();

        // then
        assertExistsDir(FileSystemRule.ROOT, WORKSPACE, "dir");
    }

    private void createRandomFiles() throws IOException {
        // given
        Set<String> randomFiles = new HashSet<>();

        for (int i = 0; i < 100; ++i) {
            // when
            Path randomFile = workspace.createRandomFile(fs.getPath("dir"), "suffix");

            // then
            String randomFilename = randomFile.getFileName().toString();

            assertTrue(Files.exists(randomFile));
            assertTrue(Files.isRegularFile(randomFile));
            assertTrue(randomFilename.endsWith("suffix"));
            assertTrue(randomFiles.add(randomFilename));
        }
    }

    private void assertNotExists(String first, String... more) {
        assertFalse(Files.exists(fs.getPath(first, more)));
    }

    private void assertExistsDir(String first, String... more) {
        Path path = fs.getPath(first, more);
        assertTrue(Files.exists(path));
        assertTrue(Files.isDirectory(path));
    }

    private void assertExistsFile(String first, String... more) {
        Path path = fs.getPath(first, more);
        assertTrue(Files.exists(path));
        assertTrue(Files.isRegularFile(path));
    }
}
package scraper.module.common.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scraper.environment.Workspace;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceServiceTest {

    private WorkspaceService service;

    @Mock
    private Workspace workspace;

    @Mock
    private ModuleContext moduleContext;

    private FileSystem fileSystem = FileSystems.getDefault();

    @Before
    public void setUp() {
        service = new WorkspaceService(workspace, fileSystem, moduleContext);
        stub(moduleContext.getModuleDetails()).toReturn(new ModuleDetails("moduleName", "instance"));
    }

    @Test
    public void testCreateFile() throws IOException {
        // given
        Path expectedPath = fileSystem.getPath("C", "workspace", "modules", "moduleName", "instance", "one", "two", "three");
        Path path = fileSystem.getPath("modules", "moduleName", "instance", "one", "two", "three.txt");
        when(workspace.createFile(path)).thenReturn(expectedPath);

        // when
        Path actualPath = service.createFile("one", "two", "three.txt");

        // then
        assertEquals(expectedPath, actualPath);
        verify(workspace).createFile(path);
    }

    @Test
    public void testCreateFile_rethrowIOException() throws IOException {
        // given
        IOException exception = new IOException("Test");
        Path path = fileSystem.getPath("modules", "moduleName", "instance", "one", "two", "three.txt");
        when(workspace.createFile(path)).thenThrow(exception);

        // when
        try {
            service.createFile("one", "two", "three.txt");
            fail();
        } catch (IOException ex) {
            assertEquals(exception, ex);
        }

        // then
        verify(workspace).createFile(path);
    }

    @Test
    public void testCreateFile_ignoreFileAlreadyExistsException() throws IOException {
        // given
        FileAlreadyExistsException exception = new FileAlreadyExistsException("Test");
        Path path = fileSystem.getPath("modules", "moduleName", "instance", "one", "two", "three.txt");
        when(workspace.createFile(path)).thenThrow(exception);

        // when
        Path actualPath = service.createFile("one", "two", "three.txt");

        // then
        assertNull(actualPath);
        verify(workspace).createFile(path);
    }
}
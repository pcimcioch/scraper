package scraper.module.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.environment.Workspace;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.util.FileUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Service implementing file I/O operations.
 */
@Service
public class WorkspaceService {

    private static final String MODULES_DIR = "modules";

    private final Workspace workspace;

    private final FileSystem fileSystem;

    private final ModuleContext moduleContext;

    @Autowired
    public WorkspaceService(Workspace workspace, FileSystem fileSystem, ModuleContext moduleContext) {
        this.workspace = workspace;
        this.fileSystem = fileSystem;
        this.moduleContext = moduleContext;
    }

    /**
     * Creates file under given path.
     * <p>
     * Each module instance has it's own, separated workspace.
     *
     * @param first  first node of the path
     * @param others next nodes of the path. May be empty
     * @return path to created file
     */
    // TODO add test for sanitizing
    public Path createFile(String first, String... others) throws IOException {
        try {
            return workspace.createFile(FileUtils.resolve(getModulePath(), FileUtils.sanitize(first), FileUtils.sanitize(others)));
        } catch (FileAlreadyExistsException e) {
            return null;
        }
    }

    private Path getModulePath() {
        ModuleDetails moduleDetails = moduleContext.getModuleDetails();
        // TODO add support for empty instances
        // TODO add tests for sanitizing. It should never be necessary, but just in case
        return fileSystem.getPath(MODULES_DIR, FileUtils.sanitize(moduleDetails.getModule()), FileUtils.sanitize(moduleDetails.getInstance()));
    }
}

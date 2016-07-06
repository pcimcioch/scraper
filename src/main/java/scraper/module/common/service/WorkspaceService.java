package scraper.module.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.environment.Workspace;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.util.FileUtils;
import scraper.util.StringUtils;

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

    private static final String EMPTY_INSTANCE_REPLACEMENT = "_";

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
    public Path createFile(String first, String... others) throws IOException {
        try {
            return workspace.createFile(FileUtils.resolve(getModulePath(), FileUtils.sanitize(first), FileUtils.sanitize(others)));
        } catch (FileAlreadyExistsException e) {
            return null;
        }
    }

    private Path getModulePath() {
        ModuleDetails moduleDetails = moduleContext.getModuleDetails();
        String instance = StringUtils.isNotBlank(moduleDetails.getInstance()) ? moduleDetails.getInstance() : EMPTY_INSTANCE_REPLACEMENT;

        // There is no need to sanitize module or instance. Their values must be correct
        return fileSystem.getPath(MODULES_DIR, moduleDetails.getModule(), instance);
    }
}

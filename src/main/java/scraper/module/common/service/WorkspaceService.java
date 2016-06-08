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

    public Path createFile(String first, String... others) throws IOException {
        try {
            return workspace.createFile(FileUtils.resolve(getModulePath(), first, others));
        } catch (FileAlreadyExistsException e) {
            return null;
        }
    }

    private Path getModulePath() {
        ModuleDetails moduleDetails = moduleContext.getModuleDetails();
        return fileSystem.getPath(MODULES_DIR, moduleDetails.getModule(), moduleDetails.getInstance());
    }
}

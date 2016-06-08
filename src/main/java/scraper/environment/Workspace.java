package scraper.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Workspace {

    private final Path wokspacePath;

    @Autowired
    public Workspace(Path workspacePath) {
        this.wokspacePath = workspacePath;
    }

    @PostConstruct
    protected void initWorkspace() throws IOException {
        Files.createDirectories(wokspacePath);
    }

    public Path getRootPath() {
        return wokspacePath;
    }

    public Path createRandomFile(Path path, String suffix) throws IOException {
        return Files.createTempFile(getDirectory(path), "", suffix);
    }

    public Path createFile(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        Files.createDirectories(fullPath.getParent());
        return Files.createFile(fullPath);
    }

    public Path createDirectory(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        Files.createDirectories(fullPath.getParent());
        return Files.createDirectory(fullPath);
    }

    public Path getDirectory(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        return Files.createDirectories(fullPath);
    }

    private Path resolvePath(Path path) {
        return wokspacePath.resolve(path);
    }
}

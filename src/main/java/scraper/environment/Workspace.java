package scraper.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service used to manage all workspace I/O operations.
 */
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

    /**
     * Return workspace root path.
     *
     * @return workspace root path.
     */
    public Path getRootPath() {
        return wokspacePath;
    }

    /**
     * Create file with random filename in given {@code path} with given {@code suffix}.
     *
     * @param path   path where file should be created. Must be directory, doesn't have to exist. This can't be absolute path. It will be resolved against root directory ({@link
     *               #getRootPath()}).
     * @param suffix filename sufix. Optional, may be null.
     * @return path to newly created file.
     * @throws IOException if IO operation failed.
     */
    public Path createRandomFile(Path path, String suffix) throws IOException {
        return Files.createTempFile(getDirectory(path), "", suffix);
    }

    /**
     * Create file under given {@code path}.
     *
     * @param path path to file. This path's directory structure doesn't have to exist. This can't be absolute path. It will be resolved against root directory ({@link
     *             #getRootPath()}).
     * @return path to newly created file.
     * @throws IOException if IO operation failed.
     */
    public Path createFile(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        Files.createDirectories(fullPath.getParent());
        return Files.createFile(fullPath);
    }

    /**
     * Create directory under given {@code path}. In contrary to {@link #getDirectory(Path)}, this method will throw {@link java.nio.file.FileAlreadyExistsException} if directory
     * alredy exists.
     *
     * @param path path to file. This path's whole diresctory structure doesn't have to exist. This can't be absolute path. It will be resolved against root directory ({@link
     *             #getRootPath()}).
     * @return path to newly created directory.
     * @throws IOException if IO operation failed.
     */
    public Path createDirectory(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        Files.createDirectories(fullPath.getParent());
        return Files.createDirectory(fullPath);
    }

    /**
     * Create or get directory under given {@code path}. In contrary to {@link #createDirectory(Path)}, this method will <b>not</b> throw {@link
     * java.nio.file.FileAlreadyExistsException} if directory alredy exists.
     *
     * @param path path to file. This path's whole diresctory structure doesn't have to exist. This can't be absolute path. It will be resolved against root directory ({@link
     *             #getRootPath()}).
     * @return path to newly created directory or existing one.
     * @throws IOException if IO operation failed.
     */
    public Path getDirectory(Path path) throws IOException {
        Path fullPath = resolvePath(path);
        return Files.createDirectories(fullPath);
    }

    private Path resolvePath(Path path) {
        return wokspacePath.resolve(path);
    }
}

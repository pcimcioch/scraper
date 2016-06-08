package scraper.test;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.nio.file.FileSystem;

public class FileSystemRule implements TestRule {

    public static final String ROOT = "root";

    private FileSystem fileSystem;

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try (FileSystem fs = MemoryFileSystemBuilder.newEmpty().build(ROOT)) {
                    fileSystem = fs;
                    base.evaluate();
                }
            }
        };
    }

}

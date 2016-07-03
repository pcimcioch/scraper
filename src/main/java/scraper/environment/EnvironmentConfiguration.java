package scraper.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Configuration of the application environment.
 */
@Configuration
public class EnvironmentConfiguration {

    @Bean(destroyMethod = "")
    FileSystem filesystem() {
        return FileSystems.getDefault();
    }

    @Bean
    Path workspacePath(FileSystem filesystem, @Value("${workpspace.path}") String workspacePath) throws IOException {
        return filesystem.getPath(workspacePath);
    }
}

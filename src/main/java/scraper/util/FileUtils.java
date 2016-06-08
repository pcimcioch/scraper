package scraper.util;

import java.nio.file.Path;

public final class FileUtils {

    private FileUtils() {

    }

    public static Path resolve(Path path, String first, String... others) {
        Path resolvedPath = path.resolve(first);
        for (String p : others) {
            resolvedPath = resolvedPath.resolve(p);
        }

        return resolvedPath;
    }

    public static String getExtension(String filename) {
        return getExtension(filename, null);
    }

    public static String getExtension(String filename, String defaultValue) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? defaultValue : filename.substring(lastDot + 1);
    }

    public static String sanitize(String filename) {
        return filename.replaceAll("[<>:\"/\\\\|\\?\\*]", "_");
    }
}

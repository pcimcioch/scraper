package scraper.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {

    public static final String FORBIDDEN_CHARS_REGEX = "[<>:\"/\\\\|\\?\\*]";

    private FileUtils() {

    }

    public static Path resolve(Path path, String first, String... others) {
        Path resolvedPath = path.resolve(first);
        for (String p : others) {
            resolvedPath = resolvedPath.resolve(p);
        }

        return resolvedPath;
    }

    public static String getExtension(String url) {
        return getExtension(url, null);
    }

    public static String getExtension(String url, String defaultValue) {
        String[] filenameParts = url.split(FORBIDDEN_CHARS_REGEX);
        String filename = filenameParts[filenameParts.length - 1];

        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? defaultValue : filename.substring(lastDot + 1);
    }

    public static String sanitize(String filename) {
        return filename.replaceAll(FORBIDDEN_CHARS_REGEX, "_");
    }

    public static String readFile(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }
}

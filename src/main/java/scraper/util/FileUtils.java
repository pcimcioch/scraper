package scraper.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for file operations.
 */
public final class FileUtils {

    private static final String FORBIDDEN_CHARS_REGEX = "[<>:\"/\\\\|\\?\\*]";

    private FileUtils() {
    }

    /**
     * Resolves given {@code path} against given nodes.
     * <p>
     * Uses {@link Path#resolve(String)}.
     *
     * @param path   path to resolve
     * @param first  first node to resolve
     * @param others other nodes to resolve. May be empty
     * @return resolved path
     */
    public static Path resolve(Path path, String first, String... others) {
        Path resolvedPath = path.resolve(first);
        for (String p : others) {
            resolvedPath = resolvedPath.resolve(p);
        }

        return resolvedPath;
    }

    /**
     * Gets file extension from {@code url}.
     *
     * @param url the url
     * @return file extension or <tt>null</tt> if extension can not be determined
     */
    public static String getExtension(String url) {
        return getExtension(url, null);
    }

    /**
     * Gets file extension from {@code url}.
     *
     * @param url          the url
     * @param defaultValue value to return if extension can not be determined
     * @return file extension or {@code defaultValue} if extension can not be determined
     */
    public static String getExtension(String url, String defaultValue) {
        String[] filenameParts = url.split(FORBIDDEN_CHARS_REGEX);
        String filename = filenameParts[filenameParts.length - 1];

        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? defaultValue : filename.substring(lastDot + 1);
    }

    /**
     * Sanitizes {@code filename}.
     * <p>
     * Replaces all filename forbidden characters with underscore (<tt>"_"</tt>) character.
     *
     * @param filename filename to sanitize
     * @return sanitized filename
     */
    public static String sanitize(String filename) {
        return filename.replaceAll(FORBIDDEN_CHARS_REGEX, "_");
    }

    /**
     * Sanitize all given {@code filenames}.
     * <p>
     * Uses {@link #sanitize(String)} method.
     *
     * @param filenames filenames to sanitize. May be empty
     * @return array of sanitized filenames
     */
    // TODO add test
    public static String[] sanitize(String... filenames) {
        String[] sanitizedFilenames = new String[filenames.length];
        for (int i = 0; i < filenames.length; ++i) {
            sanitizedFilenames[i] = sanitize(filenames[i]);
        }

        return sanitizedFilenames;
    }

    /**
     * Reads whole file content as string.
     *
     * @param path     path of the file to read
     * @param encoding encoding used to read file
     * @return file content as a string
     * @throws IOException if I/O problems occurred
     */
    public static String readFile(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }
}

package scraper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for string operations.
 */
public final class StringUtils {

    private StringUtils() {

    }

    /**
     * Return single match from given {@code text}.
     *
     * @param text  text to match.
     * @param regex regular expression to use.
     * @param group index of the group in regular expression which value should be returned.
     * @return value of the given group or <tt>null</tt>.
     */
    public static String getSingleMatch(String text, String regex, int group) {
        return getSingleMatch(text, regex, group, null);
    }

    /**
     * Return single match from given {@code text}.
     *
     * @param text         text to match.
     * @param regex        regular expression to use.
     * @param group        index of the group in regular expression which value should be returned.
     * @param defaultValue default value to return in case regular expression do not match text. Can be <tt>null</tt>.
     * @return value of the given group or {@code defaultValue}.
     */
    public static String getSingleMatch(String text, String regex, int group, String defaultValue) {
        Matcher m = Pattern.compile(regex).matcher(text);
        if (m.matches()) {
            return m.group(group);
        }

        return defaultValue;
    }

    /**
     * Check if any of the given strings is blank (<tt>null</tt> or <tt>""</tt\>).
     *
     * @param values values to check. Can be empty. Can contain <tt>null</tt> values.
     * @return <tt>true</tt> if any o the given strings is blank. <tt>false</tt> otherwise.
     */
    public static boolean isAnyBlank(String... values) {
        for (String val : values) {
            if (isBlank(val)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if given string is blank (<tt>null</tt> or <tt>""</tt\>).
     *
     * @param val string to check. Can be null.
     * @return <tt>true</tt> if given string is <tt>null</tt> or <tt>""</tt>. <tt>false</tt> otherwise.
     */
    public static boolean isBlank(String val) {
        return val == null || "".equals(val);
    }

    /**
     * Check if given string is not blank (not <tt>null</tt> nor <tt>""</tt\>).
     *
     * @param val string to check. Can be null.
     * @return <tt>true</tt> if given string is not <tt>null</tt> nor <tt>""</tt>. <tt>false</tt> otherwise.
     */
    public static boolean isNotBlank(String val) {
        return val != null && !"".equals(val);
    }
}

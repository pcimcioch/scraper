package scraper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {

    }

    public static String getSingleMatch(String text, String regex, int group) {
        return getSingleMatch(text, regex, group, null);
    }

    public static String getSingleMatch(String text, String regex, int group, String defaultValue) {
        Matcher m = Pattern.compile(regex).matcher(text);
        if (m.matches()) {
            return m.group(group);
        }

        return defaultValue;
    }

    public static boolean isAnyBlank(String... values) {
        for (String val : values) {
            if (isBlank(val)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBlank(String val) {
        return val == null || "".equals(val);
    }

    public static boolean isNotBlank(String val) {
        return val != null && !"".equals(val);
    }
}

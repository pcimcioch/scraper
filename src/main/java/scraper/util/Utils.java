package scraper.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class Utils {

    private static final int HASH_BASE = 31;

    private Utils() {

    }

    @SafeVarargs
    public static <T> Set<T> set(T... elements) {
        HashSet<T> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);

        return set;
    }

    public static <K, V> Map<K, V> map(Object... elements) {
        if ((elements.length % 2) != 0) {
            throw new IllegalArgumentException("Expected even number of arguments");
        }

        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < elements.length; i += 2) {
            map.put((K) elements[i], (V) elements[i + 1]);
        }

        return map;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static int hash(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    public static boolean computeEq(Object... elements) {
        if ((elements.length % 2) != 0) {
            throw new IllegalArgumentException("Expected even number of arguments");
        }

        for (int i = 0; i < elements.length; i += 2) {
            if (!eq(elements[i], elements[i + 1])) {
                return false;
            }
        }

        return true;
    }

    public static int computeHash(Object... elements) {
        int result = 1;
        for (Object element : elements) {
            result = HASH_BASE * result + hash(element);
        }

        return result;
    }
}

package scraper.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class responsible for all common functionality.
 */
public final class Utils {

    private static final int HASH_BASE = 31;

    private Utils() {

    }

    /**
     * Returns (modifiable) set of given {@code elements}.
     *
     * @param elements elements that should be added to the set. May be empty
     * @param <T>      type of the elements in the set
     * @return set containing given {@code elements}
     */
    @SafeVarargs
    public static <T> Set<T> set(T... elements) {
        HashSet<T> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);

        return set;
    }

    /**
     * Returns (modifiable) map of given {@code elements}.
     *
     * @param elements elements to add. Should be array of even length, containing desired keys an values alternately. May be empty
     * @param <K>      type of the keys
     * @param <V>      type of the values
     * @return created map containing {@code elements
     * @throws IllegalArgumentException if given {@code elements} array has odd length
     */
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

    /**
     * Generates UUID in form of a string.
     *
     * @return newly generated UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Compares two elements using {@code o1} {@link Object#equals(Object)} method.
     * <p>
     * If both elements are <tt>null</tt> or equal, it will return <tt>true</tt>. <tt>false</tt> otherwise.
     *
     * @param o1 first element. May be <tt>null</tt>
     * @param o2 second element. May be <tt>null</tt>
     * @return <tt>true</tt> if elements are equal, or both <tt>null</tt>. <tt>false</tt> otherwise
     */
    public static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    /**
     * Returns result of the {@link Object#hashCode()} of the passed {@code element} or <tt>0</tt> if <tt>null</tt> was passed.
     *
     * @param element element to hash. May be <tt>null</tt>
     * @return hash code of the {@code element}, or <tt>0</tt> if {@code element} is <tt>null</tt>
     */
    public static int hash(Object element) {
        return (element == null) ? 0 : element.hashCode();
    }

    /**
     * Checks equality of all the given {@code elements} paired.
     * <p>
     * That means, it will compare first element with second one, third with forth, etc. For comparison, uses {@link #eq(Object, Object)} method.
     *
     * @param elements array of the elements to check. Must has even length. May be empty. May contain <tt>null</tt> values
     * @return result of the comparison
     * @throws IllegalArgumentException if given {@code elements} array has odd length
     */
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

    /**
     * Computes hash code of all given {@code elements}.
     *
     * @param elements array of objects rom which hash code should be computed. May be empty. May contain <tt>null</tt> values
     * @return computed hash code
     */
    public static int computeHash(Object... elements) {
        int result = 1;
        for (Object element : elements) {
            result = HASH_BASE * result + hash(element);
        }

        return result;
    }
}

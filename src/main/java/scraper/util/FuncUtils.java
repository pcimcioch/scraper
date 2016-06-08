package scraper.util;

import scraper.util.function.ThrowingFunction;
import scraper.util.function.ThrowingPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FuncUtils {

    private FuncUtils() {

    }

    public static <T, K, X extends Throwable> List<K> map(Iterable<T> elements, ThrowingFunction<T, K, X> transform) throws X {
        List<K> result = new ArrayList<>();
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    public static <T, K, X extends Throwable> List<K> map(T[] elements, ThrowingFunction<T, K, X> transform) throws X {
        List<K> result = new ArrayList<>(elements.length);
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    public static <T, K, X extends Throwable> Set<K> mapSet(Iterable<T> elements, ThrowingFunction<T, K, X> transform) throws X {
        Set<K> result = new HashSet<>();
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    public static <T, K, X extends Throwable> Set<K> mapSet(T[] elements, ThrowingFunction<T, K, X> transform) throws X {
        Set<K> result = new HashSet<>(elements.length);
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    public static <T, X extends Throwable> List<T> filter(Iterable<T> elements, ThrowingPredicate<T, X> predicate) throws X {
        List<T> result = new ArrayList<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    public static <T, X extends Throwable> List<T> filter(T[] elements, ThrowingPredicate<T, X> predicate) throws X {
        List<T> result = new ArrayList<>(elements.length);
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    public static <T, X extends Throwable> Set<T> filterSet(Iterable<T> elements, ThrowingPredicate<T, X> predicate) throws X {
        Set<T> result = new HashSet<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    public static <T, X extends Throwable> Set<T> filterSet(T[] elements, ThrowingPredicate<T, X> predicate) throws X {
        Set<T> result = new HashSet<>(elements.length);
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    public static <T, K, X extends Throwable, Y extends Throwable> List<K> mapFilter(Iterable<T> elements, ThrowingFunction<T, K, X> transform, ThrowingPredicate<K, Y> predicate)
            throws X, Y {
        List<K> result = new ArrayList<>();
        for (T el : elements) {
            K newEl = transform.apply(el);
            if (predicate.test(newEl)) {
                result.add(newEl);
            }
        }

        return result;
    }

    public static <T, K, X extends Throwable, Y extends Throwable> List<K> mapFilter(T[] elements, ThrowingFunction<T, K, X> transform, ThrowingPredicate<K, Y> predicate)
            throws X, Y {
        List<K> result = new ArrayList<>(elements.length);
        for (T el : elements) {
            K newEl = transform.apply(el);
            if (predicate.test(newEl)) {
                result.add(newEl);
            }
        }

        return result;
    }

    public static <T, K, X extends Throwable, Y extends Throwable> Set<K> mapFilterSet(Iterable<T> elements, ThrowingFunction<T, K, X> transform, ThrowingPredicate<K, Y> predicate)
            throws X, Y {
        Set<K> result = new HashSet<>();
        for (T el : elements) {
            K newEl = transform.apply(el);
            if (predicate.test(newEl)) {
                result.add(newEl);
            }
        }

        return result;
    }

    public static <T, K, X extends Throwable, Y extends Throwable> Set<K> mapFilterSet(T[] elements, ThrowingFunction<T, K, X> transform, ThrowingPredicate<K, Y> predicate)
            throws X, Y {
        Set<K> result = new HashSet<>(elements.length);
        for (T el : elements) {
            K newEl = transform.apply(el);
            if (predicate.test(newEl)) {
                result.add(newEl);
            }
        }

        return result;
    }

    public static <T, K, V, X extends Throwable, Y extends Throwable> Map<K, V> toMap(Iterable<T> elements, ThrowingFunction<T, K, X> keyMapper,
            ThrowingFunction<T, V, Y> valueMapper) throws X, Y {
        Map<K, V> result = new HashMap<>();
        for (T element : elements) {
            result.put(keyMapper.apply(element), valueMapper.apply(element));
        }

        return result;
    }

    public static <T, K, V, X extends Throwable, Y extends Throwable> Map<K, V> toMap(T[] elements, ThrowingFunction<T, K, X> keyMapper, ThrowingFunction<T, V, Y> valueMapper)
            throws X, Y {
        Map<K, V> result = new HashMap<>(elements.length);
        for (T element : elements) {
            result.put(keyMapper.apply(element), valueMapper.apply(element));
        }

        return result;
    }
}

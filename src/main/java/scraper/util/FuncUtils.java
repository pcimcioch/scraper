package scraper.util;

import scraper.util.function.ThrowingFunction;
import scraper.util.function.ThrowingPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for functional operations.
 */
public final class FuncUtils {

    private FuncUtils() {

    }

    /**
     * Maps {@code elements} using {@code transform} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @return list of mapped {@code elements}
     * @throws X if {@code transform} function failed
     */
    public static <T, K, X extends Throwable> List<K> map(Iterable<T> elements, ThrowingFunction<T, K, X> transform) throws X {
        List<K> result = new ArrayList<>();
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    /**
     * Maps {@code elements} using {@code transform} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @return list of mapped {@code elements}
     * @throws X if {@code transform} function failed
     */
    public static <T, K, X extends Throwable> List<K> map(T[] elements, ThrowingFunction<T, K, X> transform) throws X {
        List<K> result = new ArrayList<>(elements.length);
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    /**
     * Maps {@code elements} using {@code transform} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @return set of mapped {@code elements}
     * @throws X if {@code transform} function failed
     */
    public static <T, K, X extends Throwable> Set<K> mapSet(Iterable<T> elements, ThrowingFunction<T, K, X> transform) throws X {
        Set<K> result = new HashSet<>();
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    /**
     * Maps {@code elements} using {@code transform} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @return set of mapped {@code elements}
     * @throws X if {@code transform} function failed
     */
    public static <T, K, X extends Throwable> Set<K> mapSet(T[] elements, ThrowingFunction<T, K, X> transform) throws X {
        Set<K> result = new HashSet<>(elements.length);
        for (T el : elements) {
            result.add(transform.apply(el));
        }

        return result;
    }

    /**
     * Filters {@code elements} using {@code predicate} function.
     *
     * @param elements  elements to filter
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @return list of {@code elements} for which {@code predicate} function returns <tt>true</tt>
     * @throws X if {@code predicate} function failed
     */
    public static <T, X extends Throwable> List<T> filter(Iterable<T> elements, ThrowingPredicate<T, X> predicate) throws X {
        List<T> result = new ArrayList<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    /**
     * Filters {@code elements} using {@code predicate} function.
     *
     * @param elements  elements to filter
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @return list of {@code elements} for which {@code predicate} function returns <tt>true</tt>
     * @throws X if {@code predicate} function failed
     */
    public static <T, X extends Throwable> List<T> filter(T[] elements, ThrowingPredicate<T, X> predicate) throws X {
        List<T> result = new ArrayList<>(elements.length);
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    /**
     * Filters {@code elements} using {@code predicate} function.
     *
     * @param elements  elements to filter
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @return set of {@code elements} for which {@code predicate} function returns <tt>true</tt>
     * @throws X if {@code predicate} function failed
     */
    public static <T, X extends Throwable> Set<T> filterSet(Iterable<T> elements, ThrowingPredicate<T, X> predicate) throws X {
        Set<T> result = new HashSet<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    /**
     * Filters {@code elements} using {@code predicate} function.
     *
     * @param elements  elements to filter
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @return set of {@code elements} for which {@code predicate} function returns <tt>true</tt>
     * @throws X if {@code predicate} function failed
     */
    public static <T, X extends Throwable> Set<T> filterSet(T[] elements, ThrowingPredicate<T, X> predicate) throws X {
        Set<T> result = new HashSet<>(elements.length);
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(el);
            }
        }

        return result;
    }

    /**
     * Maps and the filters {@code elements} using {@code transform} and {@code predicate} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @param <Y>       type of the exception that may be thrown by {@code predicate} function
     * @return list of mapped and filtered {@code elements}
     * @throws X if {@code transform} function failed
     * @throws Y if {@code predicate} function failed
     */
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

    /**
     * Maps and the filters {@code elements} using {@code transform} and {@code predicate} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @param <Y>       type of the exception that may be thrown by {@code predicate} function
     * @return list of mapped and filtered {@code elements}
     * @throws X if {@code transform} function failed
     * @throws Y if {@code predicate} function failed
     */
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

    /**
     * Maps and the filters {@code elements} using {@code transform} and {@code predicate} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @param <Y>       type of the exception that may be thrown by {@code predicate} function
     * @return set of mapped and filtered {@code elements}
     * @throws X if {@code transform} function failed
     * @throws Y if {@code predicate} function failed
     */
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

    /**
     * Maps and the filters {@code elements} using {@code transform} and {@code predicate} function.
     *
     * @param elements  elements to map
     * @param transform transformation function
     * @param predicate filter predicate function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code transform} function
     * @param <Y>       type of the exception that may be thrown by {@code predicate} function
     * @return set of mapped and filtered {@code elements}
     * @throws X if {@code transform} function failed
     * @throws Y if {@code predicate} function failed
     */
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

    /**
     * Filters and maps {@code elements} using {@code predicate} and {@code transform} function.
     *
     * @param elements  elements to map
     * @param predicate filter predicate function
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @param <Y>       type of the exception that may be thrown by {@code transform} function
     * @return list of filtered and mapped {@code elements}
     * @throws X if {@code predicate} function failed
     * @throws Y if {@code transform} function failed
     */
    public static <T, K, X extends Throwable, Y extends Throwable> List<K> mapIf(Iterable<T> elements, ThrowingPredicate<T, X> predicate, ThrowingFunction<T, K, Y> transform)
            throws X, Y {
        List<K> result = new ArrayList<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(transform.apply(el));
            }
        }

        return result;
    }

    /**
     * Filters and maps {@code elements} using {@code predicate} and {@code transform} function.
     *
     * @param elements  elements to map
     * @param predicate filter predicate function
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @param <Y>       type of the exception that may be thrown by {@code transform} function
     * @return list of filtered and mapped {@code elements}
     * @throws X if {@code predicate} function failed
     * @throws Y if {@code transform} function failed
     */
    public static <T, K, X extends Throwable, Y extends Throwable> List<K> mapIf(T[] elements, ThrowingPredicate<T, X> predicate, ThrowingFunction<T, K, Y> transform) throws X, Y {
        List<K> result = new ArrayList<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(transform.apply(el));
            }
        }

        return result;
    }

    /**
     * Filters and maps {@code elements} using {@code predicate} and {@code transform} function.
     *
     * @param elements  elements to map
     * @param predicate filter predicate function
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @param <Y>       type of the exception that may be thrown by {@code transform} function
     * @return set of filtered and mapped {@code elements}
     * @throws X if {@code predicate} function failed
     * @throws Y if {@code transform} function failed
     */
    public static <T, K, X extends Throwable, Y extends Throwable> Set<K> mapIfSet(Iterable<T> elements, ThrowingPredicate<T, X> predicate, ThrowingFunction<T, K, Y> transform)
            throws X, Y {
        Set<K> result = new HashSet<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(transform.apply(el));
            }
        }

        return result;
    }

    /**
     * Filters and maps {@code elements} using {@code predicate} and {@code transform} function.
     *
     * @param elements  elements to map
     * @param predicate filter predicate function
     * @param transform transformation function
     * @param <T>       type of the {@code elements}
     * @param <K>       type of the result
     * @param <X>       type of the exception that may be thrown by {@code predicate} function
     * @param <Y>       type of the exception that may be thrown by {@code transform} function
     * @return set of filtered and mapped {@code elements}
     * @throws X if {@code predicate} function failed
     * @throws Y if {@code transform} function failed
     */
    public static <T, K, X extends Throwable, Y extends Throwable> Set<K> mapIfSet(T[] elements, ThrowingPredicate<T, X> predicate, ThrowingFunction<T, K, Y> transform)
            throws X, Y {
        Set<K> result = new HashSet<>();
        for (T el : elements) {
            if (predicate.test(el)) {
                result.add(transform.apply(el));
            }
        }

        return result;
    }

    /**
     * Builds map based on {@code elements}.
     *
     * @param elements    elements that should be turned into map
     * @param keyMapper   transformation function used to generate key out of element
     * @param valueMapper transformation function used to generate value out of element
     * @param <T>         type of the {@code elements}
     * @param <K>         type of the key
     * @param <V>         type of the value
     * @param <X>         type of the exception that may be thrown by {@code keyMapper} function
     * @param <Y>         type of the exception that may be thrown by {@code valueMapper} function
     * @return map of the elements. Will return {@link HashMap}
     * @throws X if key mapping function failed
     * @throws Y if value mapping function failed
     */
    public static <T, K, V, X extends Throwable, Y extends Throwable> Map<K, V> toMap(Iterable<T> elements, ThrowingFunction<T, K, X> keyMapper,
            ThrowingFunction<T, V, Y> valueMapper) throws X, Y {
        Map<K, V> result = new HashMap<>();
        for (T element : elements) {
            result.put(keyMapper.apply(element), valueMapper.apply(element));
        }

        return result;
    }

    /**
     * Builds map based on {@code elements}.
     *
     * @param elements    elements that should be turned into map
     * @param keyMapper   transformation function used to generate key out of element
     * @param valueMapper transformation function used to generate value out of element
     * @param <T>         type of the {@code elements}
     * @param <K>         type of the key
     * @param <V>         type of the value
     * @param <X>         type of the exception that may be thrown by {@code keyMapper} function
     * @param <Y>         type of the exception that may be thrown by {@code valueMapper} function
     * @return map of the elements. Will return {@link HashMap}
     * @throws X if key mapping function failed
     * @throws Y if value mapping function failed
     */
    public static <T, K, V, X extends Throwable, Y extends Throwable> Map<K, V> toMap(T[] elements, ThrowingFunction<T, K, X> keyMapper, ThrowingFunction<T, V, Y> valueMapper)
            throws X, Y {
        Map<K, V> result = new HashMap<>(elements.length);
        for (T element : elements) {
            result.put(keyMapper.apply(element), valueMapper.apply(element));
        }

        return result;
    }
}

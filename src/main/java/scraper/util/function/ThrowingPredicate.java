package scraper.util.function;

/**
 * Functional interface to represent predicate function that may throw checked exception.
 *
 * @param <T> type of the function argument
 * @param <X> type of the exception that may be thrown by this predicate
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T, X extends Throwable> {

    /**
     * Test given argument.
     *
     * @param arg argument to test
     * @return the result of check
     * @throws X if check failed
     * @see java.util.function.Predicate#test(Object)
     */
    boolean test(T arg) throws X;
}

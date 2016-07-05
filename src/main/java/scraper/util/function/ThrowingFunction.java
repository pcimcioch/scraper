package scraper.util.function;

/**
 * Functional interface to represent one-argument function returning value that may throw checked exception.
 *
 * @param <T> type of the argument
 * @param <R> return type
 * @param <X> exception type
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, X extends Throwable> {

    /**
     * Executes function
     *
     * @param arg the argument
     * @return the result
     * @throws X if execution failed
     * @see java.util.function.Function#apply(Object)
     */
    R apply(T arg) throws X;

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     * @see java.util.function.Function#identity()
     */
    static <T> ThrowingFunction<T, T, RuntimeException> identity() {
        return t -> t;
    }
}

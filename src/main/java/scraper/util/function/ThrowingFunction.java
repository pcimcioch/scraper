package scraper.util.function;

@FunctionalInterface
public interface ThrowingFunction<T, R, X extends Throwable> {

    R apply(T t) throws X;

    static <T> ThrowingFunction<T, T, RuntimeException> identity() {
        return t -> t;
    }
}

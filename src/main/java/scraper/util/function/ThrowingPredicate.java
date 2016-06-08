package scraper.util.function;

@FunctionalInterface
public interface ThrowingPredicate<T, X extends Throwable> {

    boolean test(T t) throws X;
}

package scraper.util.structure;

import scraper.util.Utils;

/**
 * Class representing pair of two objects.
 *
 * @param <T> first object type
 * @param <K> second object type
 */
public class Pair<T, K> {

    private T first;

    private K second;

    public Pair() {
    }

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public K getSecond() {
        return second;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> other = (Pair<?, ?>) o;

        return Utils.computeEq(this.first, other.first, this.second, other.second);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(first, second);
    }
}

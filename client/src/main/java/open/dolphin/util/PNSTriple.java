package open.dolphin.util;

import java.util.Objects;

/**
 * PNSTriple.
 * PNSPair の３要素版
 * @author pns
 * @param <F>
 * @param <S>
 * @param <T>
 */
public class PNSTriple<F,S,T> {
    private F first;
    private S second;
    private T third;

    public PNSTriple() {
    }

    public PNSTriple(F f, S s, T t) {
        this();
        first = f;
        second = s;
        third = t;
    }

    /**
     * @return the first
     */
    public F getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public S getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * @return the third
     */
    public T getThird() {
        return third;
    }

    /**
     * @param third the third to set
     */
    public void setThird(T third) {
        this.third = third;
    }

    @Override
    public int hashCode() {
        return first.hashCode() + 15;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PNSTriple<?, ?, ?> other = (PNSTriple<?, ?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        return true;
    }
}

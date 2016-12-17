package open.dolphin.ui;

/**
 * PNSNameValuePair.
 * Generics に対応した open.dolphin.client.NameValuePair
 * @author pns
 * @param <N>
 * @param <V>
 */
public class PNSNameValuePair<N,V extends Comparable<V>> implements Comparable<PNSNameValuePair<N,V>> {

    private N name;
    private V value;

    public static int getIndex(PNSNameValuePair<?,?> target, PNSNameValuePair<?,?>[] pairArray) {
        int index = 0;
        for (int i = 0; i < pairArray.length; i++) {
            if (target.equals(pairArray[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getIndex( targetVal, PNSNameValuePair<?,?>[] pairArray) {
        int index = 0;
        for (int i = 0; i < pairArray.length; i++) {
            if (targetVal.equals(pairArray[i].getValue())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public PNSNameValuePair() {
    }

    public PNSNameValuePair(N n, V v) {
        this();
        name = n;
        value = v;
    }

    public void setValue(V code) {
        this.value = code;
    }

    public V getValue() {
        return value;
    }

    public void setName(N name) {
        this.name = name;
    }

    public N getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode() + 15;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == PNSNameValuePair.class) {
            Object otherValue = ((PNSNameValuePair)other).getValue();
            return value.equals(otherValue);
        }
        return false;
    }

    @Override
    public int compareTo(PNSNameValuePair<N,V> other) {
        if (other != null) {
            V otherValue = other.getValue();
            return value.compareTo(otherValue);
        }
        return -1;
    }
}

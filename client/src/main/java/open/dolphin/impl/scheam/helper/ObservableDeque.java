package open.dolphin.impl.scheam.helper;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * Deque (double ended queue) interface を備えた ObservableList.
 * offer/poll/peek 系：取れなければ null または false を返す.
 * add/remove/get 系：取れなければ IndexOutOfBoundsException (本物の Deque は NoSuchElementException).
 * @author pns
 * @param <T>
 */
public class ObservableDeque<T> implements Deque<T> {
    private final ObservableList<T> deque;
    private IntegerProperty sizeProperty;

    public ObservableDeque() {
        deque = FXCollections.observableArrayList();
        sizeProperty = new SimpleIntegerProperty();

        deque.addListener((Observable o) -> {
            sizeProperty.set(deque.size());
        });
    }

    public void addListener(ListChangeListener<T> listener) {
        deque.addListener(listener);
    }

    public IntegerProperty sizeProperty() { return sizeProperty; }

    @Override
    public void addFirst(T e) { deque.add(0, e); }

    @Override
    public void addLast(T e) { deque.add(e); }

    @Override
    public boolean offerFirst(T e) { deque.add(0, e); return true; }

    @Override
    public boolean offerLast(T e) { deque.add(e); return true; }

    @Override
    public T removeFirst() { return deque.remove(0); }

    @Override
    public T removeLast() { return deque.remove(deque.size() - 1); }

    @Override
    public T pollFirst() {
        if (deque.isEmpty()) { return null;  }
        else { return removeFirst(); }
    }

    @Override
    public T pollLast() {
        if (deque.isEmpty()) { return null; }
        else { return removeLast(); }
    }

    @Override
    public T getFirst() { return deque.get(0); }

    @Override
    public T getLast() { return deque.get(deque.size()-1); }

    @Override
    public T peekFirst() {
        if (deque.isEmpty()) { return null; }
        else { return getFirst(); }
    }

    @Override
    public T peekLast() {
        if (deque.isEmpty()) { return null; }
        else { return getLast(); }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) { return deque.remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i=deque.size()-1; i>=0; i--) {
            if (o.equals(deque.get(i))) {
                deque.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(T e) { return deque.add(e); }

    @Override
    public boolean offer(T e) { return offerLast(e); }

    @Override
    public T remove() { return removeFirst(); }

    @Override
    public T poll() { return pollFirst(); }

    @Override
    public T element() { return getFirst(); }

    @Override
    public T peek() { return peekFirst(); }

    @Override
    public void push(T e) { addFirst(e); }

    @Override
    public T pop() { return removeFirst(); }

    @Override
    public boolean remove(Object o) { return removeFirstOccurrence(o); }

    @Override
    public boolean contains(Object o) { return deque.contains(o); }

    @Override
    public int size() { return deque.size(); }

    @Override
    public Iterator<T> iterator() { return deque.iterator(); }

    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingIterator();
    }

    @Override
    public boolean isEmpty() { return deque.isEmpty(); }

    @Override
    public Object[] toArray() { return deque.toArray(); }

    @Override
    public <T> T[] toArray(T[] a) { return deque.toArray(a); }

    @Override
    public boolean containsAll(Collection<?> c) { return deque.containsAll(c); }

    @Override
    public boolean addAll(Collection<? extends T> c) { return deque.addAll(c); }

    @Override
    public boolean removeAll(Collection<?> c) { return deque.removeAll(c); }

    @Override
    public boolean retainAll(Collection<?> c) { return deque.retainAll(c); }

    @Override
    public void clear() { deque.clear(); }

    private class DescendingIterator implements Iterator<T> {

        private int cursor = deque.size() - 1;

        @Override
        public boolean hasNext() {
            return cursor > -1;
        }

        @Override
        public T next() {
            return deque.get(cursor --);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

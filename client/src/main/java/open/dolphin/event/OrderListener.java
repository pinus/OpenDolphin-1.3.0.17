package open.dolphin.event;

import java.util.EventListener;

/**
 * @param <T>
 * @author pns
 */
public interface OrderListener<T> extends EventListener {
    void order(T order);
}

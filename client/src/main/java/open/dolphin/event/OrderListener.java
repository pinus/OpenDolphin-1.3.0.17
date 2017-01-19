package open.dolphin.event;

import java.util.EventListener;

/**
 *
 * @author pns
 * @param <T>
 */
public interface OrderListener<T> extends EventListener {

    public void order(T order);
}

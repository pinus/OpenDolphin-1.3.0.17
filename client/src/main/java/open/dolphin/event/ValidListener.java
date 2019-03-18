package open.dolphin.event;

import java.util.EventListener;

/**
 * @author pns
 */
public interface ValidListener extends EventListener {

    public void validity(boolean valid);
}

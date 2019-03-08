package open.dolphin.event;

import java.util.EventListener;

/**
 * CompletionListener.
 * 
 * @author pns
 */
public interface CompletionListener extends EventListener {

    public void completed();
}

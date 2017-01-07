package open.dolphin.event;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface DocumentHistoryUpdateListener extends EventListener {

    public void updated(String editDate);
}

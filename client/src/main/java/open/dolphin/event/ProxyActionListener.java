package open.dolphin.event;

import java.util.EventListener;

/**
 * ProxyActionListener.
 * ActionListener restricted for use within ProxyAction.<br>
 * <b>Not compatible with java.awt.event.ActionListener.</b>
 * @author pns
 */
public interface ProxyActionListener extends EventListener {
    public void actionPerformed();
}

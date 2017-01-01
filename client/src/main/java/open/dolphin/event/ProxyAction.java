package open.dolphin.event;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 *
 * @author pns
 */
public class ProxyAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private ProxyActionListener listener;

    public ProxyAction(ProxyActionListener listener) {
        this(null, listener);
    }

    public ProxyAction(String name, ProxyActionListener listener) {
        this(name, null, listener);
    }

    public ProxyAction(String name, Icon icon, ProxyActionListener listener) {
        this.listener = listener;
        init(name, icon);
    }

    private void init(String name, Icon icon) {
        putValue(Action.NAME, name);
        putValue(Action.SMALL_ICON, icon);
    }

    public void setActionName(String name) {
        putValue(Action.NAME, name);
    }

    public void setIcon(Icon icon) {
        putValue(Action.SMALL_ICON, icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed();
    }
}

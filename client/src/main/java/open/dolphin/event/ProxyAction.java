package open.dolphin.event;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * ProxyAction.
 * Action を {@code new ProxyAction("ActionName", () -> action)} と書ける.
 *
 * @author pns
 */
public class ProxyAction extends AbstractAction {

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

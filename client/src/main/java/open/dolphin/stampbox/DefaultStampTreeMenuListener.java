package open.dolphin.stampbox;

import javax.swing.*;

/**
 * StampTreeMenuBuilder に登録する Defalut Listener.
 *
 * @author pns
 */
public class DefaultStampTreeMenuListener implements StampTreeMenuListener {
    private final JComponent component;
    private final TransferHandler handler;

    public DefaultStampTreeMenuListener(JComponent c) {
        component = c;
        handler = c.getTransferHandler();
    }

    @Override
    public void actionPerformed(StampTreeMenuEvent e) {
        handler.importData(component, e.getTransferable());
    }
}

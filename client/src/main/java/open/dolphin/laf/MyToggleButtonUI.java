package open.dolphin.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * @author pns
 */
public class MyToggleButtonUI extends MyButtonUI {

    public static ComponentUI createUI(JComponent c) {
        return new MyToggleButtonUI();
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);

    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        c.putClientProperty("JButton.disabledForeground", Color.GRAY);
        c.putClientProperty("JButton.activeForeground", c.getForeground());
    }
}

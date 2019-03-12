package open.dolphin.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author pns
 */
public class MyComboBoxUI extends com.apple.laf.AquaComboBoxUI {

    public static ComponentUI createUI(JComponent c) {
        return new MyComboBoxUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JComboBox combo = (JComboBox)c;
        combo.setMaximumRowCount(20);
    }
}

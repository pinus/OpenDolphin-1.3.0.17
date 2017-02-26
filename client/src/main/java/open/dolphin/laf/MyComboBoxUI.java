package open.dolphin.laf;

import javax.swing.JComboBox;
import javax.swing.JComponent;
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

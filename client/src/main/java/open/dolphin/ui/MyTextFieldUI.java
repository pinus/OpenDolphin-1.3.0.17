package open.dolphin.ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 *
 * @author pns
 */
public class MyTextFieldUI extends BasicTextFieldUI {
    private Border selectedBorder;
    private Border border;

    public static ComponentUI createUI(JComponent c) {
        return new MyTextFieldUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        selectedBorder = new CompoundBorder( PNSBorderFactory.createSelectedBorder(), new EmptyBorder(0,3,0,3));
        border = new CompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), new EmptyBorder(0,5,0,5));

        c.setBorder(border);
        c.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                c.setBorder(selectedBorder);
                c.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                c.setBorder(border);
                c.repaint();
            }
        });
    }
}

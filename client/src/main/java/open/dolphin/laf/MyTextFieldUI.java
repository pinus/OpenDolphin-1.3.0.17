package open.dolphin.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import open.dolphin.ui.PNSBorderFactory;

/**
 *
 * @author pns
 */
public class MyTextFieldUI extends BasicTextFieldUI {

    private static final Color LIGHTER_GRAY = new Color(228, 228, 228);
    private JTextField tf;
    private Border selectedBorder;
    private Border border;

    public static ComponentUI createUI(JComponent c) {
        return new MyTextFieldUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        tf = (JTextField) c;

        selectedBorder = new CompoundBorder(PNSBorderFactory.createSelectedBorder(), new EmptyBorder(0, 5, 0, 5));
        //border = new CompoundBorder( PNSBorderFactory.createSelectedGrayBorder(), new EmptyBorder(0,3,0,3));
        border = new EmptyBorder(0, 8, 0, 8);

        c.setBackground(Color.WHITE);
        c.setOpaque(false);
        c.setBorder(border);
        c.addFocusListener(new FocusListener() {
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

    @Override
    public void paintSafely(Graphics g) {
        g.setColor(tf.getBackground());
        // 背景を塗る
        g.fillRoundRect(1, 1, tf.getWidth() - 2, tf.getHeight() - 2, 5, 5); // retina
        if (!tf.isFocusOwner()) {
            // focus されていないときにグレーの枠線を描く
            g.setColor(LIGHTER_GRAY);
            g.drawRoundRect(1, 1, tf.getWidth() - 2, tf.getHeight() - 2, 5, 5);
        }
        super.paintSafely(g);
    }
}

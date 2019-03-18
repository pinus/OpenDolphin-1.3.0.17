package open.dolphin.laf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author pns
 */
public class MyTextFieldUI extends BasicTextFieldUI {

    public static final Color LIGHT_BLUE = new Color(152, 194, 241);
    public static final Color LIGHTER_GRAY = new Color(228, 228, 228);
    private static final int DEFAULT_TEXT_HEIGHT = 28;

    private JTextField tf;

    public static ComponentUI createUI(JComponent c) {
        return new MyTextFieldUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        tf = (JTextField) c;

        Dimension d = c.getPreferredSize();
        c.setPreferredSize(new Dimension(d.width, DEFAULT_TEXT_HEIGHT));
        c.setMinimumSize(new Dimension(0, DEFAULT_TEXT_HEIGHT));

        Border border = new EmptyBorder(0, 8, 0, 8);
        c.setBorder(border);

        c.setBackground(Color.WHITE);
        c.setOpaque(false);

        c.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                c.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                c.repaint();
            }
        });
    }

    @Override
    public void paintSafely(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (tf.isFocusOwner()) {
            g.setColor(LIGHT_BLUE);
            g.fillRoundRect(0, 0, tf.getWidth(), tf.getHeight(), 10, 10);

        } else {
            // focus されていないときにグレーの枠線を描く
            g.setColor(LIGHTER_GRAY);
            g.drawRoundRect(2, 2, tf.getWidth() - 5, tf.getHeight() - 5, 5, 5);
        }
        // 背景を塗る
        g.setColor(tf.getBackground());
        g.fillRoundRect(3, 3, tf.getWidth() - 6, tf.getHeight() - 6, 5, 5);

        g.dispose();
        super.paintSafely(graphics);
    }
}

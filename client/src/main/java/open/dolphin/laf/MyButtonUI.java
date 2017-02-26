package open.dolphin.laf;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author pns
 */
public class MyButtonUI extends com.apple.laf.AquaButtonUI {

    public static ComponentUI createUI(JComponent c) {
        return new MyButtonUI();
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);

        b.putClientProperty("JButton.disabledForeground", Color.GRAY);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        ButtonModel model = b.getModel();
        FontMetrics fm = g.getFontMetrics();

        if (model.isEnabled()) {
            /* draw text of pressed & armed, selected, and default button with white in active window */
            if (javax.swing.SwingUtilities.getWindowAncestor(b).isActive() && (
                    (model.isPressed() && model.isArmed())
                    || b.isSelected()
                    || (b instanceof JButton && ((JButton)b).isDefaultButton()))) {
                // text color in active window can be changed by property
                Object o = b.getClientProperty("JButton.activeForeground");
                g.setColor((o instanceof Color)? (Color) o : Color.WHITE);

            } else {
                g.setColor(b.getForeground());
            }

        } else {
            Color c = (Color) b.getClientProperty("JButton.disabledForeground");
            g.setColor((c != null) ? c : b.getForeground());
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(text, textRect.x, textRect.y + fm.getAscent());
    }
}

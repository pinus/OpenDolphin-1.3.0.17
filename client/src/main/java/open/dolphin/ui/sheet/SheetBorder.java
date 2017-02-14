package open.dolphin.ui.sheet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author pns
 */
public class SheetBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;
    private static final Insets INSETS = new Insets(0,10,10,10);
    private static final Color LINE_COLOR = new Color(195,195,195);

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(LINE_COLOR);

        g.drawLine(0, 0, 0, height-1);
        g.drawLine(width-1, 0, width-1, height-1);
        g.drawLine(1, height-1, width-2, height-1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return INSETS;
    }
}

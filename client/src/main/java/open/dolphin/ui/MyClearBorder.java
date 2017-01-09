package open.dolphin.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author pinus
 */
public class MyClearBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        PNSBorder.clearRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c)       {
        return PNSBorder.DEFAULT_MARGIN;
    }
}

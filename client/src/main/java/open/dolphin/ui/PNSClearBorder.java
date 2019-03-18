package open.dolphin.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * @author pns
 */
public class PNSClearBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        PNSBorder.clearRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return PNSBorder.DEFAULT_MARGIN;
    }
}

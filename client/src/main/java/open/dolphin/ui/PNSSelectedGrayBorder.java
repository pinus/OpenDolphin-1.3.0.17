package open.dolphin.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * グレーの枠.
 *
 * @author pns
 */
public class PNSSelectedGrayBorder extends AbstractBorder {

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        PNSBorder.drawSelectedGrayRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return PNSBorder.DEFAULT_MARGIN;
    }
}

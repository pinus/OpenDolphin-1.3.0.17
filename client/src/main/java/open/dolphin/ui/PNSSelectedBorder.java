package open.dolphin.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * 選択されたときの青っぽい枠.
 * @author pns
 */
public class PNSSelectedBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        PNSBorder.drawSelectedRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c)       {
        return PNSBorder.DEFAULT_MARGIN;
    }
}

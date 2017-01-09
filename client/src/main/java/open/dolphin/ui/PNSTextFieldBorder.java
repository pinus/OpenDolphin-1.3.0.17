package open.dolphin.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 * 
 * @author pns
 */
public class PNSTextFieldBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        PNSBorder.drawSelectedRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c)       {
        return PNSBorder.TEXT_FIELD_MARGIN;
    }
}

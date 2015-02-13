package open.dolphin.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author pinus
 */
public class MySelectedGrayBorder extends AbstractBorder{
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        MyBorder.drawSelectedGrayRect(c, g, x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c)       {
        return MyBorder.DEFAULT_MARGIN;
    }
}

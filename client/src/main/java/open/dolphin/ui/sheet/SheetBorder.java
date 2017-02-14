package open.dolphin.ui.sheet;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
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
    public void paintBorder(Component c, Graphics gr, int x, int y, int width, int height) {
        Graphics2D g = (Graphics2D) gr.create();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

        g.setColor(Color.WHITE);
        g.fillRoundRect(x+INSETS.left, y, width-INSETS.left-INSETS.right, height, 10, 10);


        g.dispose();
//        g.setColor(LINE_COLOR);
//
//        g.drawLine(0, 0, 0, height-1);
//        g.drawLine(width-1, 0, width-1, height-1);
//        g.drawLine(1, height-1, width-2, height-1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return INSETS;
    }
}

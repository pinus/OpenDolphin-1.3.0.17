package open.dolphin.ui.sheet;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * @author pns
 */
public class SheetBorder extends AbstractBorder {
    private static final int SHADOW_WIDTH = 10;
    private static final Insets BORDER_INSETS = new Insets(20, 20, 20, 20);
    private static final Color BACKGROUND = new Color(0.95f, 0.95f, 0.95f, 0.95f);
    private static final Color SHADOW_COLOR_ML = new Color(0f, 0f, 0f, 0.02f);
    private static final Color SHADOW_COLOR_L = new Color(0f, 0f, 0f, 0.01f);
    private static final int ARC = 24;

    @Override
    public void paintBorder(Component c, Graphics gr, int x, int y, int width, int height) {
        Graphics2D g = (Graphics2D) gr.create();

        // Shadow
        g.setColor(SHADOW_COLOR_L);
        g.fillRoundRect(1,1, width-2, height-2, ARC, ARC);
        g.fillRoundRect(3,3, width-6, height-6, ARC, ARC);
        g.fillRoundRect(4,4, width-8, height-8, ARC, ARC);
        g.fillRoundRect(5,5, width-10, height-10, ARC, ARC);
        g.fillRoundRect(6,6, width-12, height-12, ARC, ARC);
        g.setColor(SHADOW_COLOR_ML);
        g.fillRoundRect(7,7, width-14, height-14, ARC, ARC);
        g.fillRoundRect(8,8, width-16, height-16, ARC, ARC);
        g.fillRoundRect(9,9, width-18, height-18, ARC, ARC);

        // Background
        g.setColor(BACKGROUND);
        g.fillRoundRect(SHADOW_WIDTH, SHADOW_WIDTH, width - SHADOW_WIDTH * 2, height - SHADOW_WIDTH * 2, ARC, ARC);

        g.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return BORDER_INSETS;
    }
}

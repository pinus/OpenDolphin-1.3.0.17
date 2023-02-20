package open.dolphin.ui.sheet;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

/**
 * @author pns
 */
public class SheetBorder2 extends AbstractBorder {
    private static final int SHADOW_WIDTH = 10;
    private static final Insets BORDER_INSETS = new Insets(10, 20, 20, 20);
    private static final Color BACKGROUND = new Color(0.95f, 0.95f, 0.95f, 0.97f);

    private static final Color SHADOW_COLOR_D = new Color(0f, 0f, 0f, 0.2f);
    private static final Color SHADOW_COLOR_MD = new Color(0f, 0f, 0f, 0.1f);
    private static final Color SHADOW_COLOR_ML = new Color(0f, 0f, 0f, 0.05f);
    private static final Color SHADOW_COLOR_L = new Color(0f, 0f, 0f, 0f);
    private static final Color[] SHADOW_COLORS = {SHADOW_COLOR_D, SHADOW_COLOR_MD, SHADOW_COLOR_ML, SHADOW_COLOR_L};
    private static final float[] SHADOW_FRACTIONS = {0f, 0.3f, 0.5f, 1.0f};

    @Override
    public void paintBorder(Component c, Graphics gr, int x, int y, int width, int height) {
        Graphics2D g = (Graphics2D) gr.create();

        // Background
        g.setColor(BACKGROUND);
        g.fillRect(x + SHADOW_WIDTH, y, width - SHADOW_WIDTH * 2, height - SHADOW_WIDTH);
        //g.setColor(Color.LIGHT_GRAY);
        //g.drawRect(x + SHADOW_WIDTH, y, width - SHADOW_WIDTH * 2, height - SHADOW_WIDTH);

        // Shadow
        Rectangle2D left = new Rectangle2D.Float(x, y, SHADOW_WIDTH, height - SHADOW_WIDTH);
        Rectangle2D right = new Rectangle2D.Float(width - SHADOW_WIDTH, y, SHADOW_WIDTH, height - SHADOW_WIDTH);
        Rectangle2D bottom = new Rectangle2D.Float(x + SHADOW_WIDTH, height - SHADOW_WIDTH, width - SHADOW_WIDTH * 2, SHADOW_WIDTH);
        Arc2D lCorner = new Arc2D.Float(x, height - SHADOW_WIDTH * 2, SHADOW_WIDTH * 2, SHADOW_WIDTH * 2, -90, -90, Arc2D.PIE);
        Arc2D rCorner = new Arc2D.Float(width - SHADOW_WIDTH * 2, height - SHADOW_WIDTH * 2, SHADOW_WIDTH * 2, SHADOW_WIDTH * 2, 0, -90, Arc2D.PIE);

        Paint lPaint = new LinearGradientPaint(SHADOW_WIDTH, y, x, y, SHADOW_FRACTIONS, SHADOW_COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint rPaint = new LinearGradientPaint(width - SHADOW_WIDTH, y, width - 1, y, SHADOW_FRACTIONS, SHADOW_COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint bPaint = new LinearGradientPaint(SHADOW_WIDTH, height - SHADOW_WIDTH, SHADOW_WIDTH, height, SHADOW_FRACTIONS, SHADOW_COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint rcPaint = new RadialGradientPaint(SHADOW_WIDTH, height - SHADOW_WIDTH, SHADOW_WIDTH, SHADOW_FRACTIONS, SHADOW_COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        Paint lcPaint = new RadialGradientPaint(width - SHADOW_WIDTH, height - SHADOW_WIDTH, SHADOW_WIDTH, SHADOW_FRACTIONS, SHADOW_COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);

        g.setPaint(lPaint);
        g.fill(left);
        g.setPaint(rPaint);
        g.fill(right);
        g.setPaint(bPaint);
        g.fill(bottom);

        g.setPaint(rcPaint);
        g.fill(lCorner);
        g.setPaint(lcPaint);
        g.fill(rCorner);

        g.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return BORDER_INSETS;
    }
}

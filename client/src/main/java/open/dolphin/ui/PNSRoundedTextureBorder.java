package open.dolphin.ui;

import open.dolphin.helper.ImageHelper;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * イメージを texture で，角丸 fill するボーダ.
 *
 * @author pns
 */
public class PNSRoundedTextureBorder extends AbstractBorder {
    
    private static final Color EDGE_COLOR = new Color(200, 200, 200);
    private final ImageIcon image;
    private final Insets insets;

    public PNSRoundedTextureBorder(ImageIcon image, Insets insets) {
        this.image = image;
        this.insets = insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();

        BufferedImage buf = ImageHelper.imageToBufferedImage(image);
        TexturePaint paint = new TexturePaint(buf, new Rectangle2D.Double(0, 0, buf.getWidth(), buf.getHeight()));
        g2d.setPaint(paint);
        g2d.fillRoundRect(x, y, width - 1, height - 1, 10, 10);

        g2d.setColor(EDGE_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}

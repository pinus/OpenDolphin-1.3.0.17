package open.dolphin.ui;

import open.dolphin.helper.ImageHelper;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * イメージを texture で fill するボーダ.
 * @author pns
 */
public class PNSTexturedBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    private final BufferedImage image;
    private final Insets insets;

    public PNSTexturedBorder(ImageIcon image, Insets insets) {
        this.image = ImageHelper.imageToBufferedImage(image);
        this.insets = insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();

        TexturePaint paint = new TexturePaint(image, new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight()));
        g2d.setPaint(paint);
        g2d.fillRect(x, y, width, height);

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c){
        return insets;
    }

    @Override
    public boolean isBorderOpaque(){
        return false;
    }
}

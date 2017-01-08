package open.dolphin.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.border.AbstractBorder;

/**
 * タイトルボーダー.
 * @author pns
 */
public class MyTitleBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    private final ImageIcon image;
    private final Insets insets;

    public MyTitleBorder(ImageIcon image, Insets insets) {
        this.image = image;
        this.insets = insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();

        BufferedImage buf = MyBorderFactory.imageToBufferedImage(image);
        TexturePaint paint = new TexturePaint(buf, new Rectangle2D.Double(0, 0, buf.getWidth(), buf.getHeight()));
        g2d.setPaint(paint);
        g2d.fillRect(x, y, width, height);
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

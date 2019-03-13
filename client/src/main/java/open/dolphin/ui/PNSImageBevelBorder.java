package open.dolphin.ui;

import open.dolphin.helper.ImageHelper;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Subset of ch.randelshofer.quaqua.border.PNSImageBevelBorder.
 * @author pns
 */
public class PNSImageBevelBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    private final BufferedImage image;
    private final Insets borderInsets;
    private final Insets imageInsets;

    public PNSImageBevelBorder(ImageIcon img, Insets imageInsets, Insets borderInsets) {
        this.image = ImageHelper.imageToBufferedImage(img);
        this.imageInsets = imageInsets;
        this.borderInsets = borderInsets;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }

    @Override
    public void paintBorder(Component c, Graphics gr, int x, int y, int width, int height) {
        if (image == null) { return; }

        Graphics2D g = (Graphics2D) gr.create();

        int top = imageInsets.top;
        int left = imageInsets.left;
        int bottom = imageInsets.bottom;
        int right = imageInsets.right;
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        // top left corner
        //                |<-------  dist ------->|<---- source --->|
        g.drawImage(image, x, y, x + left, y + top, 0, 0,  left, top,  c);
        // top right corner
        g.drawImage(image, x + width - right, y, x + width, y + top, imgWidth - right, 0, imgWidth, top, c);
        // bottom left
        g.drawImage(image, x, y + height - bottom, x + left, y + height, 0, imgHeight - bottom, left, imgHeight, c);
        // bottom right
        g.drawImage(image, x + width - right, y + height - bottom, x + width, y + height, imgWidth - right, imgHeight - bottom, imgWidth, imgHeight, c);

        BufferedImage subImg;
        TexturePaint paint;

        // north edge
        subImg = image.getSubimage(left, 0, imgWidth - right - left, top);
        paint = new TexturePaint(subImg, new Rectangle(x + left, y, imgWidth - left - right, top));
        g.setPaint(paint);
        g.fillRect(x + left, y, width - left - right, top);
        // South
        subImg = image.getSubimage(left, imgHeight - bottom, imgWidth - right - left, bottom);
        paint = new TexturePaint(subImg, new Rectangle(x + left, y + height - bottom, imgWidth - left - right, bottom));
        g.setPaint(paint);
        g.fillRect(x + left, y + height - bottom, width - left - right, bottom);
        // West
        subImg = image.getSubimage(0, top, left, imgHeight - top - bottom);
        paint = new TexturePaint(subImg, new Rectangle(x, y + top, left, imgHeight - top - bottom));
        g.setPaint(paint);
        g.fillRect(x, y + top, left, height - top - bottom);
        // East
        subImg = image.getSubimage(imgWidth - right, top, right, imgHeight - top - bottom);
        paint = new TexturePaint(subImg, new Rectangle(x + width - right, y + top, right, imgHeight - top - bottom));
        g.setPaint(paint);
        g.fillRect(x + width - right, y + top, right, height - top - bottom);

        g.dispose();
    }
}

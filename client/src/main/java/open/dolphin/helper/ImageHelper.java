package open.dolphin.helper;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author kazm
 * @author pns
 */
public class ImageHelper {

    /**
     * ImageIcon から BufferedImage に変換.
     * alpha 対応.
     * @param src
     * @return
     */
    public static BufferedImage imageToBufferedImage(ImageIcon src) {
        if (src == null) { return null; }

        int width = src.getImage().getWidth(null);
        int height = src.getImage().getHeight(null);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        src.paintIcon(null, g, 0, 0);

        g.dispose();

        return image;
    }

    public static BufferedImage getFirstScaledInstance(BufferedImage inImage, int maxDim) {

        if (inImage.getWidth() <= maxDim && inImage.getHeight() <= maxDim) {
            return inImage;
        }

        double scale = maxDim / (double) inImage.getHeight(null);
        if (inImage.getWidth(null) > inImage.getHeight(null)) {
            scale = maxDim / (double) inImage.getWidth(null);
        }

        // Determine size of new image.
        // One of them should equal maxDim.
        int scaledW = (int) (scale * inImage.getWidth(null));
        int scaledH = (int) (scale * inImage.getHeight(null));

        // Create an image buffer in which to paint on.
        BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_BGR);

        // Set the scale.
        AffineTransform tx = new AffineTransform();

        // If the image is smaller than the desired image size,
        // don't bother scaling.
        if (scale < 1.0d) {
            tx.scale(scale, scale);
        }

        // Paint image.
        Graphics2D g2d = outImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(inImage, tx, null);
        g2d.dispose();

        return outImage;
    }
}

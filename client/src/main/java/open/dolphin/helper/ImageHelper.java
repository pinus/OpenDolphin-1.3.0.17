package open.dolphin.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * ImageHelper
 *
 * @author kazm
 * @author pns
 */
public class ImageHelper {

    /**
     * ImageIcon から BufferedImage に変換.
     * alpha 対応.
     *
     * @param src
     * @return
     */
    public static BufferedImage imageToBufferedImage(ImageIcon src) {
        if (src == null) {
            return null;
        }

        int width = src.getImage().getWidth(null);
        int height = src.getImage().getHeight(null);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        src.paintIcon(null, g, 0, 0);

        g.dispose();

        return image;
    }

    /**
     * inImage の幅と高さの長い方が maxDim になるように縮小する.
     *
     * @param inImage
     * @param maxDim
     * @return
     */
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

    /**
     * Convert Image to ByteArray.
     *
     * @param image
     * @return
     */
    public static byte[] imageToByteArray(Image image) {

        byte[] ret = null;

        try (ByteArrayOutputStream bo = new ByteArrayOutputStream()) {
            Dimension d = new Dimension(image.getWidth(null), image.getHeight(null));

            BufferedImage bf = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_BGR);
            Graphics g = bf.getGraphics();
            g.setColor(Color.white);
            g.drawImage(image, 0, 0, d.width, d.height, null);

            ImageIO.write(bf, "png", bo);

            ret = bo.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        return ret;
    }

    /**
     * ImageIcon のサイズを dim サイズ以内になるように調節する.
     *
     * @param icon
     * @param dim
     * @return
     */
    public static ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {

        if ((icon.getIconHeight() > dim.height) || (icon.getIconWidth() > dim.width)) {

            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / dim.height;
            float wRatio = (float) icon.getIconWidth() / dim.width;
            int h, w;

            if (hRatio > wRatio) {
                h = dim.height;
                w = (int) (icon.getIconWidth() / hRatio);

            } else {
                w = dim.width;
                h = (int) (icon.getIconHeight() / wRatio);
            }

            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);

        } else {
            return icon;
        }
    }
}

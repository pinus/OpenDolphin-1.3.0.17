package open.dolphin.helper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    /**
     * javax_imageio_1.0 - Chroma, Compression, Data, Dimension, Transparency
     *
     *
     * @param bytes
     * @param key
     * @return
     */
    public static String extractMetadata(byte[] bytes, String key) {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("png").next();
            ImageInputStream iis = ImageIO.createImageInputStream(bis);
            reader.setInput(iis, true);

            IIOMetadata metadata = reader.getImageMetadata(0);

            IIOMetadataNode pnsData = new IIOMetadataNode("PnsData");
            pnsData.setAttribute("key", key);
            pnsData.setAttribute("value", "==val==");

            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
            root.appendChild(pnsData);
            ???

            metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);

            System.out.println("r length " + root.getLength());

            for (int i=0; i < root.getLength(); i++) {
                System.out.println("node name = " + root.getChildNodes().item(i).getNodeName());
            }

            IIOMetadataNode r = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
            for (int i=0; i < r.getLength(); i++) {
                System.out.println("new node name = " + r.getChildNodes().item(i).getNodeName());
            }


        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }

        return null;
    }
}

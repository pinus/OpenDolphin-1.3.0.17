package open.dolphin.helper;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
     * Extract Metadata by means of ImageIO.
     * Format Name: javax_imageio_1.0
     * PNG: Chroma, Compression, Data, Dimension, Transparency
     * JPEG: Chroma, ColoSpaceType, NumChannels, Compression, CompressionTypeName, NumProgressiveScans, PixelAspectRatio, ImageOrientation
     * NodeType : ELEMENT_NODE = 1, ATTRIBUTE_NODE = 2, TEXT_NODE = 3
     *
     * @param bytes image bytes
     * @param key key
     * @return value for key
     */
    public static String extractMetadata(byte[] bytes, String key) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            ImageInputStream iis = ImageIO.createImageInputStream(bis);
            ImageReader reader = (ImageReader) ImageIO.getImageReaders(iis).next();
            reader.setInput(iis, true);

            IIOMetadata metadata = reader.getImageMetadata(0);
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);

            NodeList nlist = root.getElementsByTagName(key);
            if (nlist.getLength() > 0) {
                IIOMetadataNode target = (IIOMetadataNode) nlist.item(0);
                if (target.getAttributes().getLength() > 0) {
                    return target.getAttributes().item(0).getNodeValue();
                }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] addMetadata(byte[] bytes, String key, String value) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            ImageInputStream iis = ImageIO.createImageInputStream(bis);
            ImageReader reader = (ImageReader) ImageIO.getImageReaders(iis).next();
            reader.setInput(iis, true);
            IIOImage image = reader.readAll(0, null);

            IIOMetadataNode node = new IIOMetadataNode(key);
            node.setAttribute("value", value);
            IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
            root.appendChild(node);

            ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, param);

            System.out.println("metadata = " + metadata);

            // com.sun.imageio.plugins.png.PNGMetadata
            // node name = "Text", child node name = "TextEntry", attribute "keyword", "language", "compression"
            metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
            image.setMetadata(metadata);

            //showNode(root);
            //showNode((IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName));

            ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
            writer.setOutput(ios);
            writer.write(metadata, image, param);

            return bos.toByteArray();

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Show all Nodes.
     * @param node
     */
    private static void showNode(IIOMetadataNode node) {
        System.out.println("node name = " + node.getNodeName());
        System.out.println("node value = " + node.getNodeValue());
        System.out.println("node type = " + node.getNodeType());
        System.out.println("node attribute size = " + node.getAttributes().getLength());

        for (int i=0; i<node.getAttributes().getLength(); i++) {
            System.out.println("attribute node name = " + node.getAttributes().item(i).getNodeName());
            System.out.println("attribute node value = " + node.getAttributes().item(i).getNodeValue());
            System.out.println("attribute node type = " + node.getAttributes().item(i).getNodeType());
        }

        int len = node.getChildNodes().getLength();
        if (len > 0) {
            for (int i=0; i<len; i++) {
                showNode((IIOMetadataNode) node.getChildNodes().item(i));
            }
        }
    }

    public static void main (String[] arg) {
        String sample1 = "/schemaeditor/Sample-square.JPG";

        InputStream in = ImageHelper.class.getResourceAsStream(sample1);

        byte[] buf = null;
        try {
            int n = in.available();
            buf = new byte[n];
            for (int i = 0; i < n; i++) buf[i] = (byte) in.read();
        } catch (IOException ex) {
        }

        String key = "Text";
        //String key = "CompressionTypeName";

        byte[] buf2 = addMetadata(buf, key, "testValue");

        String val = extractMetadata(buf2, key);

        System.out.println("buf length = " + buf.length);
        System.out.println("buf2 length = " + buf2.length);

        System.out.println("key = " + key);
        System.out.println("value = " + val);
    }
}

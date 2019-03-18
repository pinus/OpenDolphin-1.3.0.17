package open.dolphin.ui;

import open.dolphin.helper.ImageHelper;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 難しい！！　未完成
 *
 * @author pns
 */
public class PNSGroupBoxBorder extends AbstractBorder {

    private ImageIcon image;
    private Insets insets;

    public PNSGroupBoxBorder(ImageIcon image, Insets insets) {
        this.image = image;
        this.insets = insets;
    }

    public static void main(String[] argv) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
//        ImageIcon icon = new ImageIcon(PNSGroupBoxBorder.class.getResource("/open/dolphin/resources/images/borders/16/Frame.titlePane.small.png"));
        ImageIcon icon = new ImageIcon(PNSGroupBoxBorder.class.getResource("/open/dolphin/resources/images/borders/18/GroupBox.png"));
        p.setBorder(new PNSGroupBoxBorder(icon, new Insets(7, 7, 7, 7)));
        f.add(p);
        f.setVisible(true);
        f.setBounds(700, 100, 500, 500);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();


        BufferedImage buf = ImageHelper.imageToBufferedImage(image);

        RectangleGenerator src = new RectangleGenerator(0, 0, buf.getWidth(), buf.getHeight(), insets);
        RectangleGenerator dist = new RectangleGenerator(x, y, width, height, insets);

        Rectangle s = src.getUpperLeftCorner();
        Rectangle d = dist.getUpperLeftCorner();
        g2d.drawImage(buf.getSubimage(s.x, s.y, s.width, s.height), null, d.x, d.y);

        s = src.getUpperRightCorner();
        d = dist.getUpperRightCorner();
        g2d.drawImage(buf.getSubimage(s.x, s.y, s.width, s.height), null, d.x, d.y);

        s = src.getLowerLeftCorner();
        d = dist.getLowerLeftCorner();
        g2d.drawImage(buf.getSubimage(s.x, s.y, s.width, s.height), null, d.x, d.y);

        s = src.getLeft();
        d = dist.getLeft();
        TexturePaint t = new TexturePaint(buf.getSubimage(s.x, s.y, s.width, s.height), s);
        g2d.setPaint(t);
        fillRect(g2d, d);

        s = src.getRight();
        d = dist.getRight();
        t = new TexturePaint(buf.getSubimage(s.x, s.y, s.width, s.height), s);
        g2d.setPaint(t);
        fillRect(g2d, d);

        s = src.getTop();
        d = dist.getTop();
        t = new TexturePaint(buf.getSubimage(s.x, s.y, s.width, s.height), s);
        g2d.setPaint(t);
        fillRect(g2d, d);

        s = src.getBottom();
        d = dist.getBottom();
        t = new TexturePaint(buf.getSubimage(s.x, s.y, s.width, s.height), s);
        g2d.setPaint(t);
        fillRect(g2d, d);

        s = src.getCenter();
        d = dist.getCenter();
        t = new TexturePaint(buf.getSubimage(s.x, s.y, s.width, s.height), s);
        g2d.setPaint(t);
        fillRect(g2d, d);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    private void fillRect(Graphics2D g2d, Rectangle r) {
        g2d.fillRect(r.x, r.y, r.width, r.height);
    }

    private class RectangleGenerator {
        private int x;
        private int y;
        private int w;
        private int h;
        private Insets i;
        private Rectangle c;

        public RectangleGenerator(int x, int y, int width, int height, Insets insets) {
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
            this.i = insets;
            this.c = new Rectangle(x + i.left + 1, y + i.top + 1, w - i.right - i.left - 2, h - i.top - i.bottom - 2);
        }

        public Rectangle getCenter() {
            return c;
        }

        public Rectangle getUpperLeftCorner() {
            return new Rectangle(x, y, i.left, i.top);
        }

        public Rectangle getUpperRightCorner() {
            return new Rectangle(x + i.left + c.width + 1, y, i.right, i.top);
        }

        public Rectangle getLowerLeftCorner() {
            return new Rectangle(x, y + i.top + c.height + 1, i.left, i.bottom);
        }

        public Rectangle getLowerRightCorner() {
            return new Rectangle(x + i.left + c.width + 1, y + i.top + c.height + 1, i.right, i.bottom);
        }

        public Rectangle getLeft() {
            return new Rectangle(x, c.y, i.left, c.height);
        }

        public Rectangle getRight() {
            return new Rectangle(x + i.left + c.width + 1, c.y, i.right, c.height);
        }

        public Rectangle getTop() {
            return new Rectangle(x + i.left + 1, y, c.width, i.top);
        }

        public Rectangle getBottom() {
            return new Rectangle(x + i.left + 1, y + i.top + c.height + 1, c.width, i.bottom);
        }
    }

}

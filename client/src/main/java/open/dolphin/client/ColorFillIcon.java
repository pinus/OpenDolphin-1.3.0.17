package open.dolphin.client;

import javax.swing.*;
import java.awt.*;


/**
 * Core Java Foundation Class by Kim topley.
 */
public class ColorFillIcon implements Icon {

    public static final int BORDER_SIZE = 2;
    public static final int DEFAULT_SIZE = 32;

    private int width;
    private int height;
    private Color fillColor;
    private Color shadow;
    private int borderSize;
    private int fillHeight;
    private int fillWidth;

    /**
     * Creates new ColorFillIcon.
     * @param fill
     * @param width
     * @param height
     * @param borderSize
     */
    public ColorFillIcon(Color fill, int width, int height, int borderSize) {
        super();

        this.fillColor = fill;
        this.width = width;
        this.height = height;
        this.borderSize = borderSize;
        this.shadow = Color.black;
        this.fillWidth = width - 2 * borderSize;
        this.fillHeight = height - 2 * borderSize;
    }

    /**
     * Creates new ColorFillIcon.
     * @param fill
     * @param size
     */
    public ColorFillIcon(Color fill, int size) {
        this(fill, size, size, BORDER_SIZE);
    }

    /**
     * Creates new ColorFillIcon.
     * @param fill
     */
    public ColorFillIcon(Color fill) {
        this(fill, DEFAULT_SIZE, DEFAULT_SIZE, BORDER_SIZE);
    }

    public void setShadow(Color c) {
        shadow = c;
    }

    public void setFillColor(Color c) {
        fillColor = c;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public void paintIcon(Component comp, Graphics g, int x, int y) {
        Color c = g.getColor();

        if(borderSize > 0) {
            g.setColor(shadow);
            for (int i = 0; i < borderSize; i++) {
                g.drawRect(x + i, y + i,
                           width - 2 * i - 1, height - 2 * i -1);
            }
        }

        g.setColor(fillColor);
        g.fillRect(x + borderSize, y + borderSize, fillWidth, fillHeight);
        g.setColor(c);
    }
}

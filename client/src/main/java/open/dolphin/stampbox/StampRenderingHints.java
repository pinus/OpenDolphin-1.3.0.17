package open.dolphin.stampbox;

import java.awt.Color;

/**
 * StampRenderingHints.

* @author Minagawa, Kazushi
 * @autho pns
 */
public class StampRenderingHints {

    private int fontSize = 12;
    private Color foreground;
    private Color background = Color.WHITE;
    private Color labelColor;
    private Color commentColor;
    private int border = 0;
    private int cellSpacing = 0;
    private int cellPadding = System.getProperty("os.name").toLowerCase().startsWith("mac")? 3 : 0;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getCellPadding() {
        return cellPadding;
    }

    public void setCellPadding(int cellPadding) {
        this.cellPadding = cellPadding;
    }

    public int getCellSpacing() {
        return cellSpacing;
    }

    public void setCellSpacing(int cellSpacing) {
        this.cellSpacing = cellSpacing;
    }

    public String getForegroundAs16String() {
        if (getForeground() == null) {
            return "#000C9C";
        } else {
            return Integer.toHexString(getForeground().getRGB()).substring(2);
        }
    }

    public String getBackgroundAs16String() {
        if (getBackground() == null) {
            return "#FFFFFF";
        } else {
            return Integer.toHexString(getBackground().getRGB()).substring(2);
        }
    }

    public String getLabelColorAs16String() {
        if (getLabelColor() == null) {
            return "#FFCED9";
        } else {
            return Integer.toHexString(getLabelColor().getRGB()).substring(2);
        }
    }

    private int width ;

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setCommentColor(Color color) {
        this.commentColor = color;
    }

    public String getCommentColorAs16String() {
        if (commentColor == null) { return getForegroundAs16String(); }
        else { return Integer.toHexString(commentColor.getRGB()).substring(2); }
    }
}

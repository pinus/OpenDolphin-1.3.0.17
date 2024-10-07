package open.dolphin.client;

import java.awt.*;
import java.util.Objects;

/**
 * StampRenderingHints.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class StampRenderingHints {

    private int fontSize = 12;
    private Color foreground;
    private Color background = Color.WHITE;
    private Color labelColor;
    private Color commentColor;
    private Color genericColor;
    private int border = 0;
    private int cellSpacing = 0;
    private int cellPadding = Dolphin.forMac ? 3 : 0;
    private int width;

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getForeground() {
        return foreground;
    }

    public String getForegroundAs16String() {
        return Objects.isNull(foreground)? "000C9C" : colorTo16String(foreground);
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBackground() {
        return background;
    }

    public String getBackgroundAs16String() {
        return Objects.isNull(background)? "FFFFFF" : colorTo16String(background);
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public String getLabelColorAs16String() {
        return Objects.isNull(labelColor) ? "FFCED9" : colorTo16String(labelColor);
    }

    public void setBorder(int border) { this.border = border; }

    public int getBorder() { return border; }

    public void setCellPadding(int cellPadding) { this.cellPadding = cellPadding; }

    public int getCellPadding() { return cellPadding; }

    public void setCellSpacing(int cellSpacing) { this.cellSpacing = cellSpacing; }

    public int getCellSpacing() { return cellSpacing; }

    public void setWidth(int width) { this.width = width; }

    public int getWidth() { return width; }

    public void setCommentColor(Color color) { this.commentColor = color; }

    public String getCommentColorAs16String() {
        return Objects.isNull(commentColor)? getForegroundAs16String() : colorTo16String(commentColor);
    }

    public void setGenericColor(Color color) { this.genericColor = color; }

    public String getGenericColorAs16String() {
        return Objects.isNull(genericColor)? getForegroundAs16String() : colorTo16String(genericColor);
    }

    private String colorTo16String(Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }
}

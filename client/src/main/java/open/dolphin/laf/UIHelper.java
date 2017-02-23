package open.dolphin.laf;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author pns
 */
public class UIHelper {
    public static final int DEFAULT_ROW_HEIGHT = 18;

    public static final Color DEFAULT_ODD_COLOR = Color.WHITE;
    public static final Color DEFAULT_EVEN_COLOR = new Color(237,243,254);
    public static final Color[] ROW_COLORS = { DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR };

    public static final Color DEFAULT_BACKGROUND_SELECTION_FOCUSED = new Color(55,106,210);
    public static final Color DEFAULT_BACKGROUND_SELECTION_OFF_FOCUS = new Color(220,220,220);

    private boolean isDragging = false;

    // foreground
    private Color textSelectionOffFocusColor = Color.BLACK;
    private Color textSelectionFocusedColor = Color.WHITE;
    private Color textNonSelectionColor = Color.BLACK;

    // background
    private Color backgroundSelectionOffFocusColor = DEFAULT_BACKGROUND_SELECTION_OFF_FOCUS;
    private Color backgroundSelectionFocusedColor = DEFAULT_BACKGROUND_SELECTION_FOCUSED;
    private Color backgroundNonSelectionColor = Color.WHITE;

    public UIHelper(JComponent c) {
        init(c);
    }

    private void init(JComponent c) {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                isDragging = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        };
        c.addMouseListener(ma);
        c.addMouseMotionListener(ma);
    }

    public static boolean isWin() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public boolean isDragging() { return isDragging; }

    public Color getBackground(boolean selected, boolean focused) {
        if (selected) {
            return focused? getBackgroundSelectionFocusedColor() : getBackgroundSelectionOffFocusColor();
        } else {
            return getBackgroundNonSelectionColor();
        }
    }

    public Color getForeground(boolean selected, boolean focused) {
        if (selected) {
            return focused? getTextSelectionFocusedColor() : getTextSelectionOffFocusColor();
        } else {
            return getTextNonSelectionColor();
        }
    }

    public void setRendererColors(DefaultTreeCellRenderer renderer) {
        setBackgroundSelectionFocusedColor(renderer.getBackgroundSelectionColor());
        setBackgroundNonSelectionColor(renderer.getBackgroundNonSelectionColor());
        setTextSelectionFocusedColor(renderer.getTextSelectionColor());
        setTextNonSelectionColor(renderer.getTextNonSelectionColor());
        setTextSelectionOffFousColor(renderer.getTextNonSelectionColor());
    }

    /**
     * @return the selectedOffFocusForeground
     */
    public Color getTextSelectionOffFocusColor() {
        return textSelectionOffFocusColor;
    }

    /**
     * @param color the selectedOffFocusForeground to set
     */
    public void setTextSelectionOffFousColor(Color color) {
        textSelectionOffFocusColor = color;
    }

    /**
     * @return the selectedFocusedForeground
     */
    public Color getTextSelectionFocusedColor() {
        return textSelectionFocusedColor;
    }

    /**
     * @param color the selectedFocusedForeground to set
     */
    public void setTextSelectionFocusedColor(Color color) {
        textSelectionFocusedColor = color;
    }

    /**
     * @return the nonSelectedForeground
     */
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

    /**
     * @param color the nonSelectedForeground to set
     */
    public void setTextNonSelectionColor(Color color) {
        textNonSelectionColor = color;
    }

    /**
     * @return the selectedOffFocusBackground
     */
    public Color getBackgroundSelectionOffFocusColor() {
        return backgroundSelectionOffFocusColor;
    }

    /**
     * @param color the selectedOffFocusBackground to set
     */
    public void setBackgroundSelectionOffFocusColor(Color color) {
        backgroundSelectionOffFocusColor = color;
    }

    /**
     * @return the selectedFocusedBackground
     */
    public Color getBackgroundSelectionFocusedColor() {
        return backgroundSelectionFocusedColor;
    }

    /**
     * @param color the selectedFocusedBackground to set
     */
    public void setBackgroundSelectionFocusedColor(Color color) {
        backgroundSelectionFocusedColor = color;
    }

    /**
     * @return the nonSelectedBackground
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * @param color the nonSelectedBackground to set
     */
    public void setBackgroundNonSelectionColor(Color color) {
        backgroundNonSelectionColor = color;
    }
}

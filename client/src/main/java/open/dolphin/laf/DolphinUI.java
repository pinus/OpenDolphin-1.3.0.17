package open.dolphin.laf;

import java.awt.Color;

/**
 *
 * @author pns
 */
public class DolphinUI {
    public static final int DEFAULT_ROW_HEIGHT = 18;

    public static final Color DEFAULT_ODD_COLOR = Color.WHITE;
    public static final Color DEFAULT_EVEN_COLOR = new Color(237,243,254);
    public static final Color[] ROW_COLORS = { DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR };

    public static final Color SELECTED_FOCUSED_BACKGROUND = new Color(55,106,210);
    public static final Color SELECTED_OFF_FOCUS_BACKGROUND = new Color(220,220,220);

    public static final boolean IS_WIN = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static Color getBackground(boolean selected, boolean focused) {
        if (selected) {
            return focused? SELECTED_FOCUSED_BACKGROUND : SELECTED_OFF_FOCUS_BACKGROUND;
        } else {
            return Color.WHITE;
        }
    }

    public static Color getForeground(boolean selected, boolean focused) {
        if (selected) {
            return focused? Color.WHITE : Color.BLACK;
        } else {
            return Color.BLACK;
        }
    }
}

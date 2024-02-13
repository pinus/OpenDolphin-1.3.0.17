package open.dolphin.inspector;

import open.dolphin.client.Dolphin;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Inspector 用の Border.
 *
 * @author pns
 */
public class InspectorBorder extends AbstractBorder {

    private static final Font DEFAULT_TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private static final Color DEFAULT_TITLE_COLOR = Color.BLACK;

    private static final Insets BORDER_INSETS = new Insets(3, 10, 3, 10);
    private static final Insets BORDER_INSETS_W_TITLE = new Insets(15, 10, 3, 10);

    private String title;
    private final Font font;
    private final Color color;
    private final int indent;
    private Insets insets;

    public InspectorBorder(String titleText) {
        title = titleText;
        font = DEFAULT_TITLE_FONT;
        color = DEFAULT_TITLE_COLOR;
        indent = 5; // タイトルの左インデント
    }

    @Override
    public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
        insets = title == null ? BORDER_INSETS : BORDER_INSETS_W_TITLE;

        Graphics2D g = (Graphics2D) graphics;

        // Background
        g.setColor(IInspector.BACKGROUND);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());

        // Border
        g.setColor(IInspector.BORDER_COLOR);
        int bx = insets.left;
        int by = insets.top;
        int bwidth = c.getWidth() - bx - insets.right;
        int bheight = c.getHeight() - by - insets.bottom;
        g.drawRect(bx - 1, by - 1, bwidth + 1, bheight + 1);

        // Title
        if (title != null) {
            g.setFont(font);
            g.setColor(color);
            if (Dolphin.forMac) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.drawString(title, insets.left + indent, g.getFontMetrics().getAscent());
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return title == null ? BORDER_INSETS : BORDER_INSETS_W_TITLE;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }
}

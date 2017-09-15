package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;
import open.dolphin.client.ClientContext;

/**
 * Inspector 用の Border.
 * @author pns
 */
public class InspectorBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    private static final Font DEFAULT_TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private static final Color DEFAULT_TITLE_COLOR = Color.BLACK;

    private static final Insets BORDER_INSETS = new Insets(3,10,3,10);
    private static final Insets BORDER_INSETS_W_TITLE = new Insets(15,10,3,10);

    private final boolean isWin = ClientContext.isWin();
    private String title;
    private Font font;
    private Color color;
    private int indent;
    private Insets insets;

    public InspectorBorder(String titleText) {
        title = titleText;
        font = DEFAULT_TITLE_FONT;
        color = DEFAULT_TITLE_COLOR;
        indent = 5; // タイトルの左インデント
    }

    @Override
    public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
        insets = title == null? BORDER_INSETS : BORDER_INSETS_W_TITLE;

        Graphics2D g = (Graphics2D) graphics.create();

        // Background
        g.setColor(IInspector.BACKGROUND);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());

        // Border
        g.setColor(IInspector.BORDER_COLOR);
        int bx = insets.left;
        int by = insets.top;
        int bwidth = c.getWidth() - bx - insets.right;
        int bheight = c.getHeight() - by - insets.bottom;
        g.drawRect(bx-1, by-1, bwidth+1, bheight+1);

        // Title
        if (title != null) {
            g.setFont(font);
            g.setColor(color);
            if (!isWin) { g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); }
            g.drawString(title, insets.left + indent, g.getFontMetrics().getAscent());
        }

        g.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return title == null? BORDER_INSETS : BORDER_INSETS_W_TITLE;
    }

    public void setTitle(String title) {
        this.title = title == null? "" : title;
    }
}

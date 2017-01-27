package open.dolphin.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;

/**
 * PNSTitledBorder.
 * justification, position, titleBorder 未対応.
 * @author pns
 */
public class PNSTitledBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    // content の回りのスペース
    private static final int DEFAULT_INSET = 5;
    // タイトルと content の間のスペース
    private static final int SPACING = 4;
    private static final Font DEFAULT_TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private static final int DEFAULT_FONT_HEIGHT = 13;

    private final Border contentBorder;
    private final Insets borderInsets = new Insets(DEFAULT_INSET + DEFAULT_FONT_HEIGHT + SPACING, DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET);

    private Border titleBorder; // 未対応
    private String title;
    private int justification, position; // 未対応
    private Font font;
    private Color fontColor;

    private final int fontHeight;

    /**
     * PNSTitledBorder.
     * @param border
     * @param text
     * @param titleJustification
     * @param titlePosition
     * @param titleFont
     * @param titleColor
     */
    PNSTitledBorder(Border border, String text, int titleJustification, int titlePosition, Font titleFont, Color titleColor) {
        titleBorder = border;
        title = text == null? "" : text;
        justification = titleJustification;
        position = titlePosition;
        font = titleFont == null? DEFAULT_TITLE_FONT : titleFont;
        fontColor = titleColor == null? Color.BLACK : titleColor;

        fontHeight = DEFAULT_FONT_HEIGHT;

        contentBorder = new ImageBevelBorder(GUIConst.ICON_BORDER_GROUPBOX_18, new Insets(5,5,5,5), borderInsets);
    }

    @Override
    public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
        Graphics2D g = (Graphics2D) graphics.create();

        // Title
        g.setFont(font);
        g.setColor(fontColor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(title, borderInsets.left + SPACING, fontHeight);

        // Content
        contentBorder.paintBorder(c, g, x, y + borderInsets.top - DEFAULT_INSET, width, height - fontHeight - SPACING);

        g.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }

    public void setTitle(Border b, String text, int j, int p, Font f, Color c) {
        titleBorder = b;
        title = text == null? "":text;
        justification = j;
        position = p;
        font = f == null? DEFAULT_TITLE_FONT : f;
        fontColor = c == null? Color.BLACK : c;
    }

    public void setTitle(String text) {
        setTitle(null, text, justification, position, font, fontColor);
    }
}

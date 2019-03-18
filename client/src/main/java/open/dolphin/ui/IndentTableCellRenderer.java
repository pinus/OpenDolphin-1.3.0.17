package open.dolphin.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * インデントを付けるレンダラ.
 * 頭にスペースをつける偽インデント.
 *
 * @author pns
 */
public class IndentTableCellRenderer extends DefaultTableCellRenderer {
    // pixels to indent
    public static final int NARROW = 5;
    public static final int WIDE = 10;
    public static final Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 9);
    private static final long serialVersionUID = 1L;
    private int indent;
    private Font font;

    public IndentTableCellRenderer() {
        this(WIDE);
    }

    public IndentTableCellRenderer(int indent) {
        this(indent, NORMAL_FONT);
    }

    public IndentTableCellRenderer(int indent, Font font) {
        super();
        this.indent = indent;
        this.font = font;
    }

    /**
     * にせインデント.
     *
     * @param text   インデントを付けるテキスト
     * @param indent インデント量
     * @param color  色
     * @return インデントを付けたテキスト
     */
    public static String addIndent(String text, int indent, Color color) {
        if (indent >= 10) {
            return "　" + text;
        } else {
            return " " + text;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean isFocused,
                                                   int row, int col) {

        if (isSelected) {
            Color fore;
            Color back;
            if (table.isFocusOwner()) {
                fore = table.getSelectionForeground();
                back = table.getSelectionBackground();
            } else {
                fore = table.getForeground();
                back = (Color) table.getClientProperty("JTable.backgroundOffFocus");
            }
            setForeground(fore);
            setBackground(back);

        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        if (value == null) {
            this.setText("");
        } else {
            this.setText(addIndent(value.toString(), indent, this.getForeground()));
        }
        this.setFont(font);

        return this;
    }

    /**
     * Show holizontal grid (Retina 対応)
     *
     * @param graphics Graphics
     */
    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        super.paint(g);

        g.setColor(Color.WHITE);
        g.drawLine(0, getHeight(), getWidth(), getHeight());
        g.dispose();
    }
}

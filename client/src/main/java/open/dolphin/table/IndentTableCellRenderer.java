package open.dolphin.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pns
 */
public class IndentTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    // pixels to indent
    public static final int NARROW = 5;
    public static final int WIDE = 10;
    public static final Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 9);
    private static final String HTML_FORMAT = "<html>"
                + "<font color=#%s>"
                + "<p style=\"white-space:nowrap; text-indent:%dpx\">%s"
                + "</p></font></html>";

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

    @Override
    public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean isFocused,
                int row, int col) {

        Color textColor;

        if (isSelected) {
            textColor = table.getSelectionForeground();
            this.setForeground(textColor);
            this.setBackground(table.getSelectionBackground());
        } else {
            textColor = table.getForeground();
            this.setForeground(textColor);
            this.setBackground(table.getBackground());
        }

        if (value == null) {
            this.setText("");
        } else {
            this.setText(addIndent((String)value, indent, this.getForeground()));
        }
        this.setFont(font);

        return this;
    }

/*  This is too much time consuming for inertia scrolling

    public static String addIndent(String text, int indent, Color color) {
        return String.format(HTML_FORMAT,
                    Integer.toHexString(color.getRGB()).substring(2), // remove alpha
                    indent,
                    text);
    }*/

    public static String addIndent(String text, int indent, Color color) {
        if (indent >= 10) return "　" + text;
        else return " " + text;
    }
}

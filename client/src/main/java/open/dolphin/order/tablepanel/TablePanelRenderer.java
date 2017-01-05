package open.dolphin.order.tablepanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import open.dolphin.client.GUIConst;
import open.dolphin.util.StringTool;

/**
 * ドロップ位置を paintComponent で表示するレンダラ.
 * @author pns
 */
public class TablePanelRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    private boolean isTargetRow;
    private boolean isUnderline;
    private final Color LINE_COLOR = new Color(0x0A,0x53,0xB6);

    public TablePanelRenderer() {
        initComponents();
    }

    private void initComponents() {
        setBorder(GUIConst.RENDERER_BORDER_NARROW);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column ) {

       JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       comp.setBorder(null);

       String text = "";

        if (value != null) {
            text = (value instanceof String)? (String) value : value.toString();
        }

        text = StringTool.toHankakuNumber(text);
        text = StringTool.toHankakuUpperLower(text);
        text = text.replaceAll("　", " ");

        // 偽インデント
        this.setText(" " + text);

        if (isSelected) {
            comp.setBackground(table.getSelectionBackground());
            comp.setForeground(table.getSelectionForeground());
        } else {
            comp.setBackground(table.getBackground());
            comp.setForeground(table.getForeground());
        }

        JTable.DropLocation loc = table.getDropLocation();
        int targetRow = (loc==null)? -1: table.getDropLocation().getRow();

        if (row == 0 && targetRow == 0) {
            isTargetRow = true;
            isUnderline = false;
        } else if (row == targetRow - 1) {
            isTargetRow = true;
            isUnderline = true;
        } else {
            isTargetRow = false;
        }

        return comp;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics g = graphics.create();

        if (isTargetRow) {
            g.setColor(LINE_COLOR);
            if (isUnderline) {
                g.fillRect(0, getSize().height-2, getSize().width, getSize().height);
            } else {
                g.fillRect(0, 0, getSize().width, 2);
            }
        }
        g.dispose();
    }
}

package open.dolphin.order.tablepanel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import open.dolphin.client.GUIConst;
import open.dolphin.helper.StringTool;

/**
 *
 * @author pns
 */
public class TablePanelRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

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
            if (table.isFocusOwner()) {
                comp.setBackground(table.getSelectionBackground());
                comp.setForeground(table.getSelectionForeground());
            } else {
                comp.setBackground((Color)table.getClientProperty("JTable.backgroundOffFocus"));
                comp.setForeground(table.getForeground());
            }
        } else {
            comp.setBackground(table.getBackground());
            comp.setForeground(table.getForeground());
        }

        return comp;
    }
}

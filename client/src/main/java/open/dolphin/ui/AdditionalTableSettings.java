package open.dolphin.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 1) テーブルの行以外の範囲をクリックしたとき，フォーカスを失う
 * 2) quaqua の stripe レンダリングをセットする
 * 3) autoStartsEdit しないようにする
 * 4) table の行の高さを設定する
 * @author pns
 */
public class AdditionalTableSettings {

    private static final int ROW_HEIGHT = 18;

    public static void setTable(final JTable table) {
        // テーブルの行以外の範囲をクリックしたとき，フォーカスを失う
        setTable(table, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.clearSelection();
                table.getParent().requestFocusInWindow();
            }
        });
    }

    public static void setTable(JTable table, MouseListener ma) {

        table.getParent().addMouseListener(ma);
        table.putClientProperty("Quaqua.Table.style", "striped");
        table.putClientProperty("JTable.autoStartsEdit", false); //キー入力によるセル編集開始を禁止する

        if (table.getDefaultRenderer(Object.class).getClass().toString().contains("DefaultTableCellRenderer")) {
            overwriteRenderer(table);
        }
        table.setIntercellSpacing(new Dimension(0,1));
        table.setRowHeight(ROW_HEIGHT);

        //table.setUI(new MyTableUI());
    }

    /**
     * Order のテーブルは intercellSpacing 入れると renderer の line が残ってしまう
     */
    public static void setOrderTable(JTable table) {
        setTable(table);
        table.setIntercellSpacing(new Dimension(0,0));
    }

    /**
     * DefaultTableCellRenderer を使っている場合，選択解除したとき，セルの枠が残ってしまうのを回避
     * @param table
     */
    private static void overwriteRenderer(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int col) {
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                if (value != null) {
                    if (value instanceof String) {
                        this.setText((String) value);
                    } else {
                        this.setText(value.toString());
                    }
                } else {
                    this.setText("");
                }
                return this;
            }
        });
    }
}

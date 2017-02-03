package open.dolphin.impl.care;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.project.Project;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.PNSCellEditor;
import open.dolphin.util.MMLDate;

/**
 * AppointTablePanel.
 * CareMapDocument の一番下に配置されるパネル.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AppointTablePanel extends JPanel {
    private static final long serialVersionUID = 1013931150179503017L;

    private final String[] COLUMN_NAMES   = { "予約日","内  容","メ   モ" };
    private final int[] COLUMN_WIDTH      = {90, 90,300};
    private final int MEMO_COLUMN         = 2;

    private CareTableModel tableModel;
    private JTable appointTable;
    private CareMapDocument parent;
    private boolean dirty;

    public AppointTablePanel(JButton updateBtn) {
        super(new BorderLayout(0, 5));
        initComponents(updateBtn);
    }

    private void initComponents(JButton updateBtn) {
        tableModel = new CareTableModel(COLUMN_NAMES);

        appointTable = new JTable(tableModel);
        appointTable.setDefaultRenderer(Object.class, new TodayRowRenderer());
        appointTable.setSurrendersFocusOnKeystroke(true);
        appointTable.setRowSelectionAllowed(true);

        // CellEditor を設定する
        DefaultCellEditor ce = new PNSCellEditor(new JTextField());
        ce.setClickCountToStart(Project.getPreferences().getInt("diagnosis.table.clickCountToStart", 1));

        // Set the column width
        TableColumn column;
        for (int i=0; i < COLUMN_WIDTH.length; i++) {
            column = appointTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(COLUMN_WIDTH[i]);
        }

        MyJScrollPane scroller = new MyJScrollPane(appointTable,
                MyJScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                MyJScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel cmd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,0));
        cmd.add(updateBtn);
        updateBtn.setMargin(new Insets(2,2,2,2));
        this.add(cmd, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);

        AdditionalTableSettings.setTable(appointTable);
    }

    /**
     * 親の ChartDocument を登録する.
     * @param doc
     */
    public void setParent(CareMapDocument doc) {
        parent = doc;
    }

    /**
     * AppointmentModel のリストを登録する.
     * @param list
     */
    public void setAppointmentList(List<AppointmentModel> list) {
        tableModel.setObjectList(list);
    }

    /**
     * AppointmentModel を更新して，テーブルの行選択する.
     * @param appoint
     */
    public void updateAppoint(AppointmentModel appoint) {
        tableModel.updateAppoint(appoint);

        String mmlDate = MMLDate.getDate(appoint.getDate());
        findAppoint(mmlDate);
    }

    /**
     * MmlDate に一致する行を選択する.
     * @param mmlDate
     */
    private void findAppoint(String mmlDate) {
        for (int i=0; i<tableModel.getObjectCount(); i++) {
            String val = (String)tableModel.getValueAt(i, 0); // date column
            if (val.equals(mmlDate)) {
                appointTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    /**
     * TableModel of AppointmentTable.
     */
    private class CareTableModel extends ObjectReflectTableModel<AppointmentModel> {
        private static final long serialVersionUID = -5342312972368806563L;

        public CareTableModel(String[] columnNames) {
            super(columnNames);
        }

        /**
         * メモ列だけ編集できる.
         * @param row
         * @param col
         * @return
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            return isValidRow(row) && col == MEMO_COLUMN;
        }

        @Override
        public Object getValueAt(int row, int col) {

            AppointmentModel entry = getObject(row);
            if (entry == null) { return null; }

            String ret = null;
            switch (col) {
                case 0: // 日付
                    ret = ModelUtils.getDateAsString(entry.getDate());
                    break;
                case 1: // 内容
                    ret = entry.getName();
                    break;
                case 2: // メモ
                    ret = entry.getMemo();
                    break;
            }

            return ret;
        }

        /**
         * メモ列に文字列を入れる.
         * @param val
         * @param row
         * @param col
         */
        @Override
        public void setValueAt(Object val, int row, int col) {
            String str = (String) val;
            if (col != MEMO_COLUMN || str == null || str.trim().equals("")) { return; }

            AppointmentModel entry = getObject(row);

            if (entry != null) {
                entry.setMemo(str);

                if (entry.getState() == AppointmentModel.TT_HAS) {
                    entry.setState(AppointmentModel.TT_REPLACE);
                }

                fireTableCellUpdated(row, col);

                if (! dirty) {
                    dirty = true;
                    parent.setDirty(dirty);
                }
            }
        }

        /**
         * update AppointmentModel.
         * @param appoint
         */
        public void updateAppoint(AppointmentModel appoint) {
            
            int row = findAppointEntry(appoint);

            if (row == -1 && appoint.getState() == AppointmentModel.TT_NEW) {
                addAppointEntry(appoint);

            } else if (row >= 0) {
                if (appoint.getName() != null) {
                    // その行を update
                    fireTableRowsUpdated(row, row);
                } else {
                    // 名前がない appoint が見つかったら削除する
                    deleteRow(row);
                }
            }
        }

        /**
         * ObjectList に entry を加えてソートする.
         * @param entry
         */
        public void addAppointEntry(AppointmentModel entry) {
            addRow(entry);
            // AppointmentModel は日付でソートされる.
            Collections.sort(getObjectList());
            int index = getObjectCount() - 1;
            fireTableRowsUpdated(0, index);
        }

        /**
         * appoint と一致する行を返す. 内容ではなくオブジェクトとして一致するかどうか.
         * @param appoint
         * @return 一致行があれば行数，なければ -1
         */
        private int findAppointEntry(AppointmentModel appoint) {

            List<AppointmentModel> appList = getObjectList();
            if (appList == null) { return -1; }

            int foundRow = -1;
            for (int i=0; i<appList.size(); i++) {
                if (appoint == appList.get(i)) {
                    foundRow = i;
                    break;
                }
            }
            return foundRow;
        }
    }

    /**
     * 今日の予約のバックグランドに色を付けるレンダラ.
     */
    private class TodayRowRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 4422900791807822090L;

        public TodayRowRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                   boolean isSelected, boolean hasFocus,
                                   int row, int column) {
            //super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

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

            AppointmentModel entry = tableModel.getObject(row);

            if (entry != null) {
                Date appoDate = entry.getDate(); // Date 型式
                GregorianCalendar appoDateGc = new GregorianCalendar();
                appoDateGc.setTime(appoDate); // GregorianCalendar 型式

                String appo = MMLDate.getDate(appoDateGc); // yyyy-mm-dd 型式
                String today = MMLDate.getDate(); // yyyy-mm-dd 型式

                if (appo.equals(today)) {
                    Color c = parent.getAppointColor(entry.getName());
                    setBackground(c);
                }
            }
            return this;
        }
    }
}

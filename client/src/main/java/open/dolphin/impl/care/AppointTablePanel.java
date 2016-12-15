package open.dolphin.impl.care;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.MyDefaultCellEditor;
import open.dolphin.infomodel.AppointmentModel;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.util.MMLDate;

/**
 * AppointTablePanel
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class AppointTablePanel extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = 1013931150179503017L;

    private final String[] COLUMN_NAMES   = new String[] { "　予約日", "　内　　容", "　メ　　モ"};
    private final int[] COLUMN_WIDTH      = {90, 90,300};
    private final int MEMO_COLUMN         = 2;

    private CareTableModel tableModel;
    private JTable careTable;
    private CareMapDocument parent;
    private boolean dirty;

    public AppointTablePanel(JButton updateBtn) {
        super(new BorderLayout(0, 5));
        initComponents(updateBtn);
    }

    private void initComponents(JButton updateBtn) {
        IndentTableCellRenderer todayRenderer = new IndentTableCellRenderer();
        tableModel = new CareTableModel(COLUMN_NAMES);
        String today = MMLDate.getDate();

        careTable = new JTable(tableModel) {
            private static final long serialVersionUID = -3446348785385967929L;

            @Override
            public TableCellRenderer getCellRenderer(int row, int col) {
                AppointmentModel model = tableModel.getObject(row);
                if (model != null && today.equals(ModelUtils.getDateAsString(model.getDate()))) {
                    Color c = parent.getAppointColor(model.getName());
                    todayRenderer.setBackground(c);
                    return todayRenderer;

                } else {
                    return super.getCellRenderer(row, col);
                }
            }
        };
        careTable.setDefaultRenderer(Object.class, todayRenderer);
        careTable.setSurrendersFocusOnKeystroke(true);
        careTable.setRowSelectionAllowed(true);

        // CellEditor を設定する
        DefaultCellEditor ce = new MyDefaultCellEditor(new JTextField());
        ce.setClickCountToStart(1);

        // Set the column width
        if (COLUMN_WIDTH != null) {
            int len = COLUMN_WIDTH.length;
            for (int i = 0; i < len; i++) {
                careTable.getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTH[i]);
            }
        }

        MyJScrollPane scroller = new MyJScrollPane(careTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel cmd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,0));
        cmd.add(updateBtn);
        updateBtn.setMargin(new Insets(2,2,2,2));
        this.add(cmd, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
        AdditionalTableSettings.setTable(careTable);
    }

    public void setParent(CareMapDocument doc) {
        parent = doc;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

        switch (e.getPropertyName()) {
            case CareMapDocument.CALENDAR_PROP:
                SimpleCalendarPanel[] calendars = (SimpleCalendarPanel[])e.getNewValue();
                int len = calendars.length;
                List list = new ArrayList();
                for (int i = 0; i < len; i++) {

                    List results = calendars[i].getAppointDays();
                    int size = results.size();
                    //System.out.println("Appoint size = " + size);
                    for (int k = 0; k < size; k++) {
                        list.add(results.get(k));
                    }
                }
                tableModel.setObjectList(list);
                break;
            case CareMapDocument.APPOINT_PROP:
                AppointmentModel appoint = (AppointmentModel)e.getNewValue();
                tableModel.updateAppoint(appoint);
                break;
            case CareMapDocument.SELECTED_APPOINT_DATE_PROP:
                findAppoint((String)e.getNewValue());
                break;
            default:
                break;
        }
    }

    private void findAppoint(String date) {
        System.out.println(date);
        int size = tableModel.getObjectCount();
        String val = null;
        for (int i = 0; i < size; i++) {
            val = (String)tableModel.getValueAt(i, 0);
            if (val.equals(date)) {
                careTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private class CareTableModel extends ObjectReflectTableModel<AppointmentModel> {
        private static final long serialVersionUID = -5342312972368806563L;

        public CareTableModel(String[] columnNames) {
            super(columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (isValidRow(row) && col == MEMO_COLUMN);
        }

        @Override
        public Object getValueAt(int row, int col) {
            AppointmentModel model = getObject(row);
            if (model == null) { return null; }

            String ret = null;
            switch (col) {
                case 0: // 予約日
                    ret = ModelUtils.getDateAsString(model.getDate());
                    break;
                case 1: // 内容
                    ret = model.getName();
                    break;
                case 2: // メモ
                    ret = model.getMemo();
                    break;
            }
            return ret;
        }

        @Override
        public void setValueAt(Object val, int row, int col) {
            String memo = (String)val;
            if (col != MEMO_COLUMN || memo == null || memo.trim().equals("")) { return; }

            AppointmentModel entry = getObject(row);
            if (entry != null) {
                entry.setMemo(memo);
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

        public void updateAppoint(AppointmentModel appoint) {

            int row = findAppointEntry(appoint);
            int state = appoint.getState();

            if (row == -1 && state == AppointmentModel.TT_NEW) {
                addAppointEntry(appoint);

            } else if (row >= 0) {
                if (appoint.getName() != null) {
                    fireTableRowsUpdated(row, row);
                } else {
                    deleteRow(row);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public void addAppointEntry(AppointmentModel entry) {
            addRow(entry);
            Collections.sort(getObjectList());
            int index = getObjectCount() - 1;
            fireTableRowsUpdated(0, index);
        }

        private int findAppointEntry(AppointmentModel appoint) {
            List<AppointmentModel> models = getObjectList();
            if (models == null) { return -1; }

            int len = models.size();
            int row = -1;
            for (int i = 0; i < len; i++) {
                if (appoint == models.get(i)) {
                    row = i;
                    break;
                }
            }
            return row;
        }
    }

    protected class TodayRowRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 4422900791807822090L;

        public TodayRowRenderer() {
        }
    }
}

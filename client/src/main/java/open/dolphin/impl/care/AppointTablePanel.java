package open.dolphin.impl.care;

import open.dolphin.infomodel.ModelUtils;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.MyDefaultCellEditor;
import javax.swing.*;
import javax.swing.table.*;
import open.dolphin.client.*;

import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.util.*;

import java.awt.*;
import java.beans.*;
import java.util.*;
import java.util.List;
import open.dolphin.project.Project;
import open.dolphin.table.ObjectReflectTableModel;

/**
 * AppointTablePanel
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class AppointTablePanel extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1013931150179503017L;

    private final String[] COLUMN_NAMES   = ClientContext.getStringArray("appoint.table.columnNames");
    private final int[] COLUMN_WIDTH      = {90, 90,300};
    private final int MEMO_COLUMN         = 2;

    private CareTableModel tableModel;
    private JTable careTable;
    private TodayRowRenderer todayRenderer;
    private String today;   // = "2003-02-21";
    private CareMapDocument parent;
    private boolean dirty;

    /** Creates new AppointTablePanel */
    public AppointTablePanel(JButton updateBtn) {

        super(new BorderLayout(0, 5));

        todayRenderer = new TodayRowRenderer();
        tableModel = new CareTableModel(COLUMN_NAMES);
        careTable = new JTable(tableModel) {

            private static final long serialVersionUID = -3446348785385967929L;

            public TableCellRenderer getCellRenderer(int row, int col) {

                AppointmentModel e = (AppointmentModel)tableModel.getObject(row);

                if (e != null && e.getDate().equals(today)) {
                    Color c = parent.getAppointColor(e.getName());
                    todayRenderer.setBackground(c);
                    return todayRenderer;

                } else {
                    return super.getCellRenderer(row, col);
                }
            }
        };
        careTable.setSurrendersFocusOnKeystroke(true);
        careTable.setRowSelectionAllowed(true);
//pns   careTable.setDefaultRenderer(Object.class, new OddEvenRowRenderer());

        // CellEditor を設定する
        // NAME_COL clickCountToStart=1, IME=ON
        TableColumn column = careTable.getColumnModel().getColumn(MEMO_COLUMN);
//pns^  column.setCellEditor(new IMECellEditor(new JTextField(), 1, true));
        DefaultCellEditor ce = new MyDefaultCellEditor(new JTextField());
        ce.setClickCountToStart(Project.getPreferences().getInt("diagnosis.table.clickCountToStart", 1));
//pns$
        // Set the column width
        if (COLUMN_WIDTH != null) {
            int len = COLUMN_WIDTH.length;
            for (int i = 0; i < len; i++) {
                column = careTable.getColumnModel().getColumn(i);
                column.setPreferredWidth(COLUMN_WIDTH[i]);
            }
        }
        //careTable.setPreferredSize(new Dimension(500, 200));

        JScrollPane scroller = new JScrollPane(careTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel cmd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,0));
        cmd.add(updateBtn);
        updateBtn.setMargin(new Insets(2,2,2,2));
        this.add(cmd, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);

        today = MMLDate.getDate();
//pns   動的 startNumRows 調節のための設定
        //tableModel.setTable(careTable);
//pns^  table の関係ないところをクリックしたら，selection をクリア
        AdditionalTableSettings.setTable(careTable);
    }

    public void setParent(CareMapDocument doc) {
        parent = doc;
    }

    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();

        if (prop.equals(CareMapDocument.CALENDAR_PROP)) {

            SimpleCalendarPanel[] calendars = (SimpleCalendarPanel[])e.getNewValue();

            int len = calendars.length;
            ArrayList list = new ArrayList();

            for (int i = 0; i < len; i++) {

                ArrayList results = calendars[i].getAppointDays();
                int size = results.size();
                //System.out.println("Appoint size = " + size);
                for (int k = 0; k < size; k++) {
                    list.add(results.get(k));
                }
            }

            tableModel.setObjectList(list);

        } else if (prop.equals(CareMapDocument.APPOINT_PROP)) {

            AppointmentModel appoint = (AppointmentModel)e.getNewValue();
            tableModel.updateAppoint(appoint);

        } else if (prop.equals(CareMapDocument.SELECTED_APPOINT_DATE_PROP)) {

            findAppoint((String)e.getNewValue());

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

    protected class CareTableModel extends ObjectReflectTableModel {

        private static final long serialVersionUID = -5342312972368806563L;

        public CareTableModel(String[] columnNames) {
            super(columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (isValidRow(row) && col == MEMO_COLUMN) ? true : false;
        }

        @Override
        public Object getValueAt(int row, int col) {

            AppointmentModel e = (AppointmentModel)getObject(row);

            if (e == null) {
                return null;
            }

            String ret = null;

            switch (col) {

                case 0:
                    ret = ModelUtils.getDateAsString(e.getDate());
                    break;

                case 1:
                    ret = e.getName();
                    break;

                case 2:
                    ret = e.getMemo();
                    break;
            }

            return (Object)ret;
        }

        @Override
        public void setValueAt(Object val, int row, int col) {

            String str = (String)val;
            if (col != MEMO_COLUMN || str == null || str.trim().equals("")) {
                return;
            }

            AppointmentModel entry = (AppointmentModel)getObject(row);

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
            addRow((Object)entry);
            Collections.sort(getObjectList());
            int index = getObjectCount() -1;
            fireTableRowsUpdated(0, index);
        }

        private int findAppointEntry(AppointmentModel appoint) {

            List objects = getObjectList();

            if (objects == null) {
                return -1;
            }
            int len = objects.size();
            int row = -1;
            for (int i = 0; i < len; i++) {
                if (appoint == (AppointmentModel)objects.get(i)) {
                    row = i;
                    break;
                }
            }
            return row;
        }

        public Object[] getAppointEntries() {
            List list = getObjectList();
            return list != null ? list.toArray() : null;
        }

    }

    protected class TodayRowRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 4422900791807822090L;

        public TodayRowRenderer() {
        }
    }
}

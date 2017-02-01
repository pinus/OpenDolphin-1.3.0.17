package open.dolphin.impl.care;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.beans.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import open.dolphin.calendar.CalendarEvent;
import open.dolphin.calendar.CalendarTable;
import open.dolphin.calendar.CalendarTableModel;
import open.dolphin.client.*;
import open.dolphin.event.ProxyAction;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.util.*;

/**
 * SimpleCalendarPanel.
 * @author Kazushi Minagawa
 * @author pns
 */
public final class SimpleCalendarPanel extends JPanel implements DragGestureListener, DropTargetListener, DragSourceListener {
    private static final long serialVersionUID = 3030024622746649784L;

    private SimpleDate today;
    private int relativeMonth;

    private CalendarTable table;
    private CalendarTableModel tableModel;
    // MmlDate 型式の日付をキー，AppointmentModel を value とする HashMap
    private final HashMap<String, AppointmentModel> map = new HashMap<>();

    // DnD
    private DragSource dragSource;
    private int dragRow;
    private int dragCol;

    private Chart context;
    private CareMapDocument parent;
    private boolean dirty;

    private JPopupMenu appointMenu;
    private int popedRow;
    private int popedCol;

    private PropertyChangeSupport boundSupport;

    public SimpleCalendarPanel() {
        this(0);
    }

    public SimpleCalendarPanel(int n) {
        // 今月を基点とした相対月数
        relativeMonth = n;
        init();
    }

    private void init() {
        // Get right now
        today = new SimpleDate(new GregorianCalendar());
        GregorianCalendar gc = new GregorianCalendar(today.getYear(), today.getMonth(), today.getDay());

        // Create requested month calendar
        // Add relative number to create
        gc.add(Calendar.MONTH, relativeMonth);
        //table = createCalendarTable(gc);
        table = new CalendarTable(gc);
        tableModel = (CalendarTableModel) table.getModel();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                if (row != -1 && col != -1) {

                    SimpleDate date = (SimpleDate) table.getValueAt(row, col);
                    String mmlDate = SimpleDate.simpleDateToMmldate(date);

                    if (date.getEventCode() != null) {
                        boundSupport.firePropertyChange(CareMapDocument.SELECTED_DATE_PROP, null, mmlDate);

                    } else if (map.get(mmlDate) != null) {
                        boundSupport.firePropertyChange(CareMapDocument.SELECTED_APPOINT_DATE_PROP, null, mmlDate);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    doPopup(e);
                }
            }
        });

        setLayout(new BorderLayout());
        add(table.getTitledPanel(), BorderLayout.CENTER);

        // Embed popup menu
        appointMenu = new JPopupMenu();
        JMenuItem item = new JMenuItem(new ProxyAction("取り消し", this::appointCancel));
        appointMenu.add(item);

        // Table を DragTarget, 自身をリスナに設定する
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_COPY_OR_MOVE, this);

        // Table を DropTarget, 自身をリスナに設定する
        new DropTarget(table, this);
    }

    /**
     * ChartImpl を保存，誕生日を登録.
     * @param context
     */
    public void setChartContext(Chart context) {
        this.context = context;
        String mmlBirthday = context.getPatient().getBirthday();
        tableModel.setBirthday(mmlBirthday);
    }

    /**
     * 親の ChartDocument を登録する.
     * @param doc
     */
    public void setParent(CareMapDocument doc) {
        parent = doc;
    }

    /**
     * 今月からの相対月数を返す.
     * @return
     */
    public int getRelativeMonth() {
        return relativeMonth;
    }

    /**
     * 今月かどうかを返す.
     * @return
     */
    public boolean isThisMonth() {
        return relativeMonth == 0;
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    @Override
    public void removePropertyChangeListener(String propName, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.removePropertyChangeListener(propName, l);
    }

    /**
     * このカレンダーの月の１日を MML 型式で返す.
     * @return
     */
    public String getFirstDate() {
        int year = tableModel.getYear();
        int month = tableModel.getMonth();
        GregorianCalendar firstDay = new GregorianCalendar(year, month, 1);

        return MMLDate.getDate(firstDay);
    }

    /**
     * このカレンダーの月の末日を MML 型式で返す.
     * @return
     */
    public String getLastDate() {
        int year = tableModel.getYear();
        int month = tableModel.getMonth();
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int days = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        gc.add(Calendar.DAY_OF_MONTH, days-1);

        return MMLDate.getDate(gc);
    }

    /**
     * 予約のある日をリストで返す.
     * @return 予約日リスト
     */
    public List<AppointmentModel> getAppointDays() {

        List<AppointmentModel> results = new ArrayList<>();

        for (int row=0; row<tableModel.getRowCount(); row++) {
            for (int col=0; col<tableModel.getColumnCount(); col++) {
                SimpleDate date = (SimpleDate) table.getValueAt(row, col);

                if (date.getYear() == tableModel.getYear() && date.getMonth() == tableModel.getMonth()) {
                    String mmlDate = SimpleDate.simpleDateToMmldate(date);

                    AppointmentModel appoint = map.get(mmlDate);

                    if (appoint != null && appoint.getName() != null) {
                        results.add(appoint);
                    }
                }
            }
        }

        return results;
    }

    /**
     * 更新された予約のリストを返す.
     * @return 更新された予約のリスト
     */
    public List<AppointmentModel> getUpdatedAppoints() {

        List<AppointmentModel> results = new ArrayList<>();

        for (int row=0; row<tableModel.getRowCount(); row++) {
            for (int col=0; col<tableModel.getColumnCount(); col++) {
                SimpleDate date = (SimpleDate) table.getValueAt(row, col);

                if (date.getYear() == tableModel.getYear() && date.getMonth() == tableModel.getMonth()) {
                    String mmlDate = SimpleDate.simpleDateToMmldate(date);

                    AppointmentModel appoint = map.get(mmlDate);

                    if (appoint != null && appoint.getState() != AppointmentModel.TT_NONE) {
                        results.add(appoint);
                    }
                }
            }
        }

        return results;
    }

    /**
     * ModuleModel のリストを登録する.
     * @param event
     * @param list
     */
    public void setModuleList(String event, List<ModuleModel> list) {
        if (list == null || list.isEmpty()) { return; }

        clearMark(event);

        list.forEach(module -> {
            String mmlDate = ModelUtils.getDateAsString(module.getConfirmed());
            SimpleDate date = SimpleDate.mmlDateToSimpleDate(mmlDate);

            date.setEventCode(event);

            // CalendarTableModel で row, col は使ってない
            table.setValueAt(date, 0, 0);
        });

        tableModel.fireTableDataChanged();
    }

    /**
     * ImageEntry のリストを登録する.
     * @param event
     * @param list
     */
    public void setImageList(String event, List<ImageEntry> list) {
        if (list == null || list.isEmpty()) { return; }

        clearMark(event);

        list.forEach(entry -> {
            String mmlDate = entry.getConfirmDate();
            SimpleDate date = SimpleDate.mmlDateToSimpleDate(mmlDate);

            date.setEventCode(event);

            // CalendarTableModel で row, col は使ってない
            table.setValueAt(date, 0, 0);
        });

        tableModel.fireTableDataChanged();
    }

    /**
     * AppointModel のリストを登録する.
     * @param list
     */
    public void setAppointmentList(List<AppointmentModel> list) {
        if (list == null || list.isEmpty()) { return; }

        list.forEach(appoint -> {
            appoint.setState(AppointmentModel.TT_HAS);
            String mmlToday = SimpleDate.simpleDateToMmldate(today);
            String mmlAppointDate = ModelUtils.getDateAsString(appoint.getDate());

            // 今日以降のものだけ登録
            if (mmlAppointDate.compareTo(mmlToday) >= 0 ) {
                SimpleDate date = SimpleDate.mmlDateToSimpleDate(mmlAppointDate);
                date.setEventCode(CalendarEvent.getCode(appoint.getName()));

                map.put(mmlAppointDate, appoint);
                table.setValueAt(date, 0, 0);
            }
        });

        tableModel.fireTableDataChanged();
    }

    /**
     * eventCode のイベントをクリアする.
     * @param eventCode
     */
    private void clearMark(String eventCode) {

        boolean changed = false;

        for (int row=0; row<tableModel.getRowCount(); row++) {
            for (int col=0; col<tableModel.getColumnCount(); col++) {
                SimpleDate date = (SimpleDate) table.getValueAt(row, col);

                if (date.getEventCode().equals(eventCode)) {
                    date.setEventCode(null);
                    changed = true;
                }
            }
        }

        if (changed) { tableModel.fireTableDataChanged(); }
    }

    //////////////   Drag Support //////////////////

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {

        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        if (row == -1 || col == -1) { return; }

        dragRow = row;
        dragCol = col;
        //MedicalEvent me = days[row][col];
        //MedicalEvent me = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(row, col)));
        SimpleDate date = (SimpleDate) table.getValueAt(row, col);
        String mmlDate = SimpleDate.simpleDateToMmldate(date);
        AppointmentModel appo = map.get(mmlDate);

        if (appo == null) {
            return;
        }

        Transferable t = new AppointEntryTransferable(appo);
        Cursor cursor = DragSource.DefaultCopyDrop;
        int action = event.getDragAction();
        if (action == DnDConstants.ACTION_MOVE) {
            cursor = DragSource.DefaultMoveDrop;
        }

        // Starts the drag
        dragSource.startDrag(event, cursor, t, this);
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent event) {

        if (! event.getDropSuccess() || event.getDropAction() == DnDConstants.ACTION_COPY) {
            return;
        }

        processCancel(dragRow, dragCol);
    }

    @Override
    public void dragEnter(DragSourceDragEvent event) {
    }

    @Override
    public void dragOver(DragSourceDragEvent event) {
    }

    @Override
    public void dragExit(DragSourceEvent event) {
    }

    @Override
    public void dropActionChanged( DragSourceDragEvent event) {
    }

    //////////// Drop Support ////////////////

    @Override
    public void drop(DropTargetDropEvent e) {

        if (! isDropAcceptable(e)) {
            e.rejectDrop();
            setDropTargetBorder(false);
            return;
        }

        // Transferable を取得する
        final Transferable tr = e.getTransferable();

        // Drop 位置を得る
        final Point loc = e.getLocation();

        // accept?
        int action = e.getDropAction();
        e.acceptDrop(action);
        //e.getDropTargetContext().dropComplete(true);
        setDropTargetBorder(false);

        int row = table.rowAtPoint(loc);
        int col = table.columnAtPoint(loc);
        //System.out.println("row = " + droppedRow + " col = " + droppedCol);
        if (row == -1 || col == -1) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        SimpleDate date = (SimpleDate) tableModel.getValueAt(row, col);

        // outOfMonth ?
        if (tableModel.getYear() != date.getYear() || tableModel.getMonth() != date.getMonth()) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        // 本日以前
        if (today.compareTo(date) >= 0) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        // Drop 処理
        AppointmentModel source = null;
        try {
            source = (AppointmentModel) tr.getTransferData(AppointEntryTransferable.appointFlavor);

        } catch (UnsupportedFlavorException | IOException ue) {
            System.out.println(ue);
            source = null;
        }
        if (source == null) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        processAppoint(row, col, source.getName(), source.getMemo());

        e.getDropTargetContext().dropComplete(true);
    }

    public boolean isDragAcceptable(DropTargetDragEvent evt) {
        return (evt.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
    }

    public boolean isDropAcceptable(DropTargetDropEvent evt) {
        return (evt.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
    }

    /** DropTaregetListener interface method */
    @Override
    public void dragEnter(DropTargetDragEvent e) {
        if (! isDragAcceptable(e)) {
            e.rejectDrag();
        }
    }

    /** DropTaregetListener interface method */
    @Override
    public void dragExit(DropTargetEvent e) {
        setDropTargetBorder(false);
    }

    /** DropTaregetListener interface method */
    @Override
    public void dragOver(DropTargetDragEvent e) {
        if (isDragAcceptable(e)) {
            setDropTargetBorder(true);
            // Drop 位置を得る
            Point loc = e.getLocation();
            int row = table.rowAtPoint(loc);
            int col = table.columnAtPoint(loc);
            table.changeSelection(row, col, false, false);
        }
    }

    /** DropTaregetListener interface method */
    @Override
    public void dropActionChanged(DropTargetDragEvent e) {
        if (! isDragAcceptable(e)) {
            e.rejectDrag();
        }
    }

    private void setDropTargetBorder(final boolean b) {
        Color c = b ? GUIFactory.getDropOkColor() : this.getBackground();
        table.setBorder(BorderFactory.createLineBorder(c, 2));
    }

    private void doPopup(MouseEvent e) {

        // ReadOnly 時の予約は不可
        if (context.isReadOnly()) {
            return;
        }

        popedRow = table.rowAtPoint(e.getPoint());
        popedCol = table.columnAtPoint(e.getPoint());
        if (popedRow == -1 || popedCol == -1) {
            return;
        }

        SimpleDate date = (SimpleDate)tableModel.getValueAt(popedRow, popedCol);
        AppointmentModel appoint = map.get(SimpleDate.simpleDateToMmldate(date));

        // 予約のない日. popup menu がキャンセルのみなので
        if (appoint == null) { return; }

        // 本日以前の予約は不可
        if (today.compareTo(date) >= 0) { return; }

        appointMenu.show(e.getComponent(),e.getX(), e.getY());
    }

    public void appointInspect(ActionEvent e) {
        //processAppoint(popedRow, popedCol, "再診", null);
    }

    public void appointTest(ActionEvent e) {
        processAppoint(popedRow, popedCol, "検体検査", null);
    }

    public void appointImage(ActionEvent e) {
        processAppoint(popedRow, popedCol, "画像診断", null);
    }

    public void appointOther(ActionEvent e) {
        processAppoint(popedRow, popedCol, "その他", null);
    }

    public void appointCancel() {
        processCancel(popedRow, popedCol);
    }

    /**
     * 予約を設定する.
     * @param row
     * @param col
     * @param appointName
     * @param memo
     */
    private void processAppoint(int row, int col, String appointName, String memo) {

        SimpleDate date = (SimpleDate)tableModel.getValueAt(row, col);
        String mmlDate = SimpleDate.simpleDateToMmldate(date);
        AppointmentModel appoint = map.get(mmlDate);

        if (appoint == null) {
            appoint = new AppointmentModel();
            appoint.setDate(ModelUtils.getDateAsObject(mmlDate));
            map.put(mmlDate, appoint);
        }

        int oldState = appoint.getState();
        int next = 0;
        switch (oldState) {

            case AppointmentModel.TT_NONE:
                next = AppointmentModel.TT_NEW;
                break;

            case AppointmentModel.TT_NEW:
                next = AppointmentModel.TT_NEW;
                break;

            case AppointmentModel.TT_HAS:
                next = AppointmentModel.TT_REPLACE;
                break;

            case AppointmentModel.TT_REPLACE:
                next = AppointmentModel.TT_REPLACE;
                break;
        }
        appoint.setState(next);
        appoint.setName(appointName);
        appoint.setMemo(memo);
        date.setEventCode(CalendarEvent.getCode(appointName));

        ((AbstractTableModel)table.getModel()).fireTableCellUpdated(popedRow, popedCol);

        boundSupport.firePropertyChange(CareMapDocument.APPOINT_PROP, null, appoint);

        if (! dirty) {
            dirty = true;
            parent.setDirty(dirty);
        }
    }

    /**
     * 予約をキャンセルする.
     * @param row
     * @param col
     */
    private void processCancel(int row, int col) {

        SimpleDate date = (SimpleDate)tableModel.getValueAt(row, col);
        AppointmentModel appoint = map.get(SimpleDate.simpleDateToMmldate(date));

        if (appoint == null) { return; }

        int oldState = appoint.getState();
        int nextState = 0;

        switch (oldState) {
            case AppointmentModel.TT_NONE:
                break;

            case AppointmentModel.TT_NEW:
                nextState = AppointmentModel.TT_NONE;
                break;

            case AppointmentModel.TT_HAS:
                nextState = AppointmentModel.TT_REPLACE;
                break;

            case AppointmentModel.TT_REPLACE:
                nextState = AppointmentModel.TT_REPLACE;
                break;
        }

        appoint.setState(nextState);
        appoint.setName(null);
        date.setEventCode(null);

        ((AbstractTableModel)table.getModel()).fireTableCellUpdated(popedRow, popedCol);

        boundSupport.firePropertyChange(CareMapDocument.APPOINT_PROP, null, appoint);

        if (! dirty) {
            dirty = true;
            parent.setDirty(dirty);
        }
    }
/*
    private class CalendarTableTransferHandler extends PatchedTransferHandler {
        private static final long serialVersionUID = 1L;

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {

        }

        @Override
        public boolean canImport(TransferSupport support) {

        }

        @Override
        public boolean importData(TransferSupport support) {

        }
    }
*/
    /**
     * CalendarPool Class
     */
    public static class SimpleCalendarPool {

        private static SimpleCalendarPool instance = new SimpleCalendarPool();
        private HashMap<String, List> poolDictionary = new HashMap<>();

        private SimpleCalendarPool() {
        }

        public static SimpleCalendarPool getInstance() {
            return instance;
        }

        public synchronized SimpleCalendarPanel acquireSimpleCalendar(int n) {
            List pool = poolDictionary.get(String.valueOf(n));
            if (pool != null) {
                int size = pool.size();
                size--;
                return (SimpleCalendarPanel)pool.remove(size);
            }
            return new SimpleCalendarPanel(n);
        }

        public synchronized void releaseSimpleCalendar(SimpleCalendarPanel c) {
            int n = c.getRelativeMonth();
            String key = String.valueOf(n);
            List pool = poolDictionary.get(key);
            if (pool == null) {
                pool = new ArrayList<>(5);
                poolDictionary.put(key, pool);
            }
            pool.add(c);
        }
    }
}

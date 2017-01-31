package open.dolphin.impl.care;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.beans.*;
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

    private int year;
    private int month;
    private int numRows;
    private int firstCol;
    private int lastCol;
    private GregorianCalendar firstDay;
    private GregorianCalendar lastDay;
    private GregorianCalendar today;
    private String birthday;

    private CalendarTable table;
    private CalendarTableModel tableModel;
    private HashMap<String, MedicalEvent> map = new HashMap<>();
    private MedicalEvent[][] days;

    // DnD
    private DragSource dragSource;
    private int dragRow;
    private int dragCol;

    private int relativeMonth;

    private Chart context;
    private CareMapDocument parent;
    private boolean dirty;

    private JPopupMenu appointMenu;
    private int popedRow;
    private int popedCol;

    private String markEvent = "-1";

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
        today = new GregorianCalendar();
        today.clear(Calendar.MILLISECOND);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.HOUR_OF_DAY);
        GregorianCalendar gc = (GregorianCalendar)today.clone();

        // Create requested month calendar
        // Add relative number to create
        gc.add(Calendar.MONTH, relativeMonth);
        this.year = gc.get(Calendar.YEAR);
        this.month = gc.get(Calendar.MONTH);
        //table = createCalendarTable(gc);
        table = new CalendarTable(gc);
        tableModel = (CalendarTableModel) table.getModel();

        days = createDays(gc);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                if (row != -1 && col != -1) {

                    SimpleDate date = (SimpleDate) table.getValueAt(row, col);
                    //MedicalEvent evt = days[row][col];
                    MedicalEvent evt = map.get(SimpleDate.simpleDateToMmldate(date));

                    if (evt.getMedicalCode() != null) {
                        boundSupport.firePropertyChange(CareMapDocument.SELECTED_DATE_PROP, null, evt.getDisplayDate());

                    } else if (evt.getAppointmentName() != null) {
                        boundSupport.firePropertyChange(CareMapDocument.SELECTED_APPOINT_DATE_PROP, null, evt.getDisplayDate());
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

    public void setChartContext(Chart context) {
        this.context = context;
        birthday = context.getPatient().getBirthday().substring(5);
    }

    public void setParent(CareMapDocument doc) {
        parent = doc;
    }

    public String getCalendarTitle() {
        return String.format("%s年%s月", year, month+1);
    }

    public int getRelativeMonth() {
        return relativeMonth;
    }

    public boolean isThisMonth() {
        return relativeMonth == 0 ? true : false;
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

    public String getFirstDate() {
        return MMLDate.getDate(firstDay);
    }

    public String getLastDate() {
        return MMLDate.getDate(lastDay);
    }

    /**
     * 予約のある日をリストで返す.
     * @return 予約日リスト
     */
    public List<AppointmentModel> getAppointDays() {

        List<AppointmentModel> results = new ArrayList<>();
        MedicalEvent event = null;
        AppointmentModel appoint = null;

        // 1 週目を調べる
        for (int col = firstCol; col < 7; col++) {
            //event = days[0][col];
            event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(0, col)));
            appoint = event.getAppointEntry();
            if (appoint != null && appoint.getName() != null) {
                results.add(appoint);
            }
        }

        // 2 週目以降を調べる
        for (int row = 1; row < numRows - 1; row++) {
            for (int col = 0; col < 7; col++) {
                //event = days[row][col];
                event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(row, col)));
                appoint = event.getAppointEntry();
                if (appoint != null && appoint.getName() != null) {
                    results.add(appoint);
                }
            }
        }

        // 最後の週を調べる
        for (int col = 0; col < lastCol + 1; col++) {
            //event = days[numRows - 1][col];
            event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(numRows -1 , col)));
            appoint = event.getAppointEntry();
            if (appoint != null && appoint.getName() != null) {
                results.add(appoint);
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
        MedicalEvent event = null;
        AppointmentModel appoint = null;

        // 1 週目を調べる
        for (int col = firstCol; col < 7; col++) {
            //event = days[0][col];
            event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(0, col)));
            appoint = event.getAppointEntry();
            if (appoint != null && appoint.getState() != AppointmentModel.TT_NONE) {
                results.add(appoint);
            }
        }

        // 2週目以降を調べる
        for (int row = 1; row < numRows - 1; row++) {
            for (int col = 0; col < 7; col++) {
                //event = days[row][col];
                event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(row, col)));
                appoint = event.getAppointEntry();
                if (appoint != null && appoint.getState() != AppointmentModel.TT_NONE) {
                    results.add(appoint);
                }
            }
        }

        // 最後の週を調べる
        for (int col = 0; col < lastCol + 1; col++) {
            //event = days[numRows - 1][col];
            event = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(numRows -1 , col)));
            appoint = event.getAppointEntry();
            if (appoint != null && appoint.getState() != AppointmentModel.TT_NONE) {
                results.add(appoint);
            }
        }

        return results;
    }

    public void setModuleList(String event, List list) {

        markEvent = event;
        clearMark();

        if (list == null || list.isEmpty()) {
            return;
        }

        int size = list.size();
        String mkDate = null;
        MedicalEvent me = null;
        int index = 0;
        int[] ymd = null;
        int row = 0;
        int col = 0;

        ModuleModel module = null;

        for (int i = 0; i < size; i++) {

            module = (ModuleModel)list.get(i);
            //mkDate = ModelUtils.getDateAsString(module.getModuleInfo().getConfirmDate());
            mkDate = ModelUtils.getDateAsString(module.getConfirmed());
            index = mkDate.indexOf('T');
            if (index > 0) {
                mkDate = mkDate.substring(0, index);
            }
            ymd = MMLDate.getCalendarYMD(mkDate);

            int shiftDay = ymd[2] + (firstCol -1);
            row = shiftDay / 7;
            col = shiftDay % 7;

            //me = days[row][col];
            System.out.println("-------  module event = " + markEvent);
            SimpleDate date = (SimpleDate)table.getValueAt(row, col);
            System.out.println("-------  module date = " + SimpleDate.simpleDateToMmldate(date));
            me = map.get(SimpleDate.simpleDateToMmldate(date));
            me.setMedicalCode(markEvent);
            date.setEventCode(markEvent);

            ((AbstractTableModel)table.getModel()).fireTableCellUpdated(row, col);
        }
    }

    public void setImageList(String event, List list) {

        markEvent = event;
        clearMark();

        if (list == null || list.isEmpty()) {
            return;
        }

        int size = list.size();
        String mkDate = null;
        MedicalEvent me = null;
        int index = 0;
        int[] ymd = null;
        int row = 0;
        int col = 0;

        ImageEntry image = null;

        for (int i = 0; i < size; i++) {

            image = (ImageEntry)list.get(i);
            mkDate = image.getConfirmDate();
            index = mkDate.indexOf('T');
            if (index > 0) {
                mkDate = mkDate.substring(0, index);
            }
            //System.out.println("PVT date: " + pvtDate);
            ymd = MMLDate.getCalendarYMD(mkDate);

            int shiftDay = ymd[2] + (firstCol -1);
            row = shiftDay / 7;
            col = shiftDay % 7;

            //me = days[row][col];
            me = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)table.getValueAt(row, col)));
            me.setMedicalCode(markEvent);

            ((AbstractTableModel)table.getModel()).fireTableCellUpdated(row, col);
        }
    }

    public void setAppointmentList(List list) {

        // 当月以降のカレンダのみ検索する
        if (relativeMonth < 0 ) {
            return;
        }

        // 空ならリターン
        if ( list == null || list.isEmpty() ) {
            return;
        }

        // 当月であれば本日の３日前から検索，そうでない場合はカレンダの最初の日から検索する
        String startDate = isThisMonth() ? MMLDate.getDayFromToday(-3) : MMLDate.getDate(firstDay);

        // 表示する
        int size = list.size();
        for (int i = 0; i < size; i++) {
            AppointmentModel ae = (AppointmentModel)list.get(i);
            ae.setState(AppointmentModel.TT_HAS);
            String date = ModelUtils.getDateAsString(ae.getDate());
            int index = date.indexOf('T');
            if (index > 0) {
                date = date.substring(0, index);
            }

            // startDate 以前の場合は表示しない
            if (date.compareTo(startDate) < 0 ) {
                continue;
            }

            int[] ymd = MMLDate.getCalendarYMD(date);

            int shiftDay = ymd[2] + (firstCol -1);
            int row = shiftDay / 7;
            int col = shiftDay % 7;

            //MedicalEvent me = days[row][col];
            SimpleDate dt = (SimpleDate)table.getValueAt(row, col);
            MedicalEvent me = map.get(SimpleDate.simpleDateToMmldate(dt));
            me.setAppointEntry(ae);

            dt.setEventCode(CalendarEvent.getCode(ae.getName()));

            System.out.println("-------  appo name = " + ae.getName());
            System.out.println("-------  appo code = " + CalendarEvent.getCode(ae.getName()));


            ((AbstractTableModel)table.getModel()).fireTableCellUpdated(row, col);
        }
    }

    /**
     * 現在の表示をクリアする
     */
    private void clearMark() {
        MedicalEvent me = null;
        boolean exit = false;
        //String val = null;

        for (int row = 0; row < numRows; row++) {

            for (int col = 0; col < 7; col++) {

                //me = days[row][col];
                SimpleDate date = (SimpleDate)tableModel.getValueAt(row, col);
                me = map.get(SimpleDate.simpleDateToMmldate(date));

                if (me.isToday()) {
                    exit = true;
                    break;

                } else if (me.getMedicalCode() != null) {

                    me.setMedicalCode(null);
                    date.setEventCode(null);
                    ((AbstractTableModel)table.getModel()).fireTableCellUpdated(row, col);
                }
            }
            if (exit) {
                break;
            }
        }
    }

    //////////////   Drag Support //////////////////

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {

        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        if (row == -1 || col == -1) {
            return;
        }

        dragRow = row;
        dragCol = col;
        //MedicalEvent me = days[row][col];
        MedicalEvent me = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(row, col)));
        AppointmentModel appo = me.getAppointEntry();
        if (appo == null) {
            //System.out.println("No Appoint");
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

        // outOfMonth ?
        //MedicalEvent evt = days[row][col];
        MedicalEvent evt = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(row, col)));
        if (evt == null || evt.isOutOfMonth()) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        // 本日以前
        if (evt.before(today)) {
            e.getDropTargetContext().dropComplete(false);
            return;
        }

        // Drop 処理
        AppointmentModel source = null;
        try {
            source = (AppointmentModel)tr.getTransferData(AppointEntryTransferable.appointFlavor);

        } catch (Exception ue) {
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

        // クリックされた位置の MedicalEvent
        //MedicalEvent me = days[popedRow][popedCol];
        MedicalEvent me = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(popedRow, popedCol)));

        // 予約のない日
        // popup menu がキャンセルのみなので
        if (me.getAppointmentName() == null) {
            return;
        }

        // 月外の日の予約は不可
        //if (me.isOutOfMonth()) {
        //return;
        //}

        // 本日以前の予約は不可
        if (me.before(today)) {
            return;
        }

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

    private void processAppoint(int row, int col, String appointName, String memo) {

        //MedicalEvent entry = days[row][col];
        MedicalEvent entry = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(row, col)));
        AppointmentModel appoint = entry.getAppointEntry();
        System.out.println("------name = " + appointName);

        if (appoint == null) {
            appoint = new AppointmentModel();
            appoint.setDate(ModelUtils.getDateAsObject(entry.getDisplayDate()));
            entry.setAppointEntry(appoint);
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


        SimpleDate date = (SimpleDate)table.getValueAt(row, col);
        date.setEventCode(CalendarEvent.getCode(appointName));

        ((AbstractTableModel)table.getModel()).fireTableCellUpdated(popedRow, popedCol);

        boundSupport.firePropertyChange(CareMapDocument.APPOINT_PROP, null, appoint);

        if (! dirty) {
            dirty = true;
            parent.setDirty(dirty);
        }
    }

    private void processCancel(int row, int col) {

        //MedicalEvent entry = days[row][col];
        MedicalEvent entry = map.get(SimpleDate.simpleDateToMmldate((SimpleDate)tableModel.getValueAt(row, col)));
        AppointmentModel appoint = entry.getAppointEntry();
        if (appoint == null) {
            return;
        }

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

        SimpleDate date = (SimpleDate) table.getValueAt(row, col);
        date.setEventCode(null);

        ((AbstractTableModel)table.getModel()).fireTableCellUpdated(popedRow, popedCol);

        boundSupport.firePropertyChange(CareMapDocument.APPOINT_PROP, null, appoint);

        if (! dirty) {
            dirty = true;
            parent.setDirty(dirty);
        }
    }

    /**
     * カレンダテーブルのデータを生成する
     */
    private MedicalEvent[][] createDays(GregorianCalendar gc) {

        MedicalEvent[][] data = null;

        // Ｎケ月前／先の今日と同じ日
        int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);

        // 作成するカレンダ月の日数
        int numDaysOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 最後の日が月の何週目か
        gc.add(Calendar.DAY_OF_MONTH, numDaysOfMonth - dayOfMonth);  // Last day
        lastDay = (GregorianCalendar)gc.clone();                     // Save last day
        numRows = gc.get(Calendar.WEEK_OF_MONTH);                    // Week of month

        // それは何カラム目か
        lastCol = gc.get(Calendar.DAY_OF_WEEK);
        lastCol--;

        // 月の最初の日
        numDaysOfMonth--;
        gc.add(Calendar.DAY_OF_MONTH, -numDaysOfMonth);
        firstDay = (GregorianCalendar)gc.clone();

        // 週の何日目か
        firstCol = gc.get(Calendar.DAY_OF_WEEK);
        firstCol--;

        // この月のカレンダーに表示する最初の日
        gc.add(Calendar.DAY_OF_MONTH, -firstCol);

        // データ配列を生成
        data = new MedicalEvent[numRows][7];

        // 一日づつ増加させながら埋め込み
        MedicalEvent me;
        boolean b;
        for (int i = 0; i < numRows; i++) {

            for (int j = 0; j < 7; j++) {

                me = new MedicalEvent(
                        gc.get(Calendar.YEAR),
                        gc.get(Calendar.MONTH),
                        gc.get(Calendar.DAY_OF_MONTH),
                        gc.get(Calendar.DAY_OF_WEEK));

                // 月外の日か
                b = month == gc.get(Calendar.MONTH) ? true : false;
                me.setOutOfMonth(!b);

                // 今日か
                b = today.equals(gc) ? true : false;
                me.setToday(b);

                data[i][j] = me;
                SimpleDate sd = new SimpleDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
                map.put(SimpleDate.simpleDateToMmldate(sd), me);

                // 次の日
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return data;
    }

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

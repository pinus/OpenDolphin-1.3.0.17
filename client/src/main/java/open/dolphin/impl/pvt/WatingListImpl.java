package open.dolphin.impl.pvt;

import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import open.dolphin.JsonConverter;
import open.dolphin.client.*;
import open.dolphin.delegater.DolphinClientContext;
import open.dolphin.delegater.PvtDelegater;
import open.dolphin.delegater.PatientDelegater;
import open.dolphin.dto.PvtStateSpec;
import open.dolphin.helper.ReflectAction;
import open.dolphin.infomodel.*;
import open.dolphin.project.Project;
import open.dolphin.table.IndentTableCellRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.ui.AdditionalTableSettings;
import open.dolphin.ui.ExecuteScript;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJSheet;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;

/**
 * 受付リスト。
 * @author Kazushi Minagawa, Digital Globe, Inc.,
 * @author pns
 */
public class WatingListImpl extends AbstractMainComponent {

    private static final String NAME = "受付リスト";

    // アイコン
    protected static final ImageIcon DONE_ICON = GUIConst.ICON_CHECK_LIGHTBLUE_16;
    protected static final ImageIcon OPEN_ICON = GUIConst.ICON_BOOK_OPEN_16;
    protected static final ImageIcon OPEN_READ_ONLY_ICON = GUIConst.ICON_BOOK_OPEN_16;
    protected static final ImageIcon UNFINISHED_ICON = GUIConst.ICON_CHECK_RED_16;
    protected static final ImageIcon TEMPORARY_ICON = GUIConst.ICON_ONEDIT_16;
    protected static final ImageIcon OPEN_USED_NONE = GUIConst.ICON_USER_WHITE_16;
    protected static final ImageIcon OPEN_USED_SAVE = GUIConst.ICON_USER_BLUE_16;
    protected static final ImageIcon OPEN_USED_UNFINISHED = GUIConst.ICON_USER_RED_16;

    private ImageIcon KUTU_ICON = GUIConst.ICON_ARROW_CIRCLE_DOUBLE_16;

    // JTableレンダラ用のカラー
    private static final Color MALE_COLOR = new Color(230,243,243);
    private static final Color FEMALE_COLOR = ClientContext.getColor("watingList.color.female");
    private static final Color CANCEL_PVT_COLOR = ClientContext.getColor("watingList.color.pvtCancel");
    protected static final Color SHOSHIN_COLOR = new Color(180,220,240); //青っぽい色
    protected static final Color KARTE_EMPTY_COLOR = new Color(250,200,160); //茶色っぽい色
    protected static final Color DIAGNOSIS_EMPTY_COLOR = new Color(243,255,15); //黄色
    // 来院情報テーブルの年齢カラム
    private int AGE_COLUMN;
    // 年齢生年月日メソッド
    private final String[] AGE_METHOD = new String[]{"getPatientAgeBirthday", "getPatientBirthday"};
    // PVT Table
    private JTable pvtTable;
    private ObjectReflectTableModel pvtTableModel;
    // Preference
    private Preferences preferences;
    // 性別レンダラフラグ
    private boolean sexRenderer;
    // 年齢表示
    private boolean ageDisplay;
    // 運転日
    private Date operationDate;
    // 受付 DB をチェックした Date
    private Date checkedTime;
    // 来院患者数
    private int pvtCount;
    // デフォルトのチェック間隔
    private static int DEFAULT_CHECK_INTERVAL = 30; // デフォルト値
    // チェック間隔
    private int checkInterval;
    // 次のチェックまでの残り時間
    private int intervalToNextCheck;
    // 選択されている患者情報
    private PatientVisitModel[] selectedPvt; // 複数行選択対応
    private int[] saveSelectedIndex; // 複数行選択対応
    // handler of PvtChecker
    private ScheduledFuture timerHandler;
    // 定期チェックの runnable: タイマーで定期起動，kutuBtn，PvtBroadcastReceiver で臨時起動される
    private PvtChecker pvtChecker;
    // PvtChecker の臨時起動等，runnable を乗せるための ExecutorService
    private ExecutorService executor;
    // PvtChecker を定期起動するための ExecutorService
    private ScheduledExecutorService schedule;

    private Logger logger;
    private WatingListPanel view;
    private static final Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 9);

    private int initialAtokMemSize = ExecuteScript.getAtok24MemSize();

    /**
     * Creates new WatingList
     */
    public WatingListImpl() {
        setName(NAME);
        // 使い回すオブジェクト
        schedule = Executors.newSingleThreadScheduledExecutor();
        executor = Executors.newSingleThreadExecutor();
        pvtChecker = new PvtChecker();
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        setup();
        initComponents();
        connect();
        // pvt 定期チェック開始
        restartCheckTimer();
        // pvt broadcaster 受信待ちスレッド開始
        // getUserAsPVTServer をリサイクル利用
        if (Project.getUseAsPVTServer() ) {;
            startPvtMessageReceiver();
        }

        if (checkInterval == 0) {
            // 定期チェック off の場合 (checkInterval = 0)
            // 初回のチェック。以後は ReceivePvtBroadcast と，下のタイマーで更新
            checkFullPvt();
            // １分ごとに setPvtCount() を呼んで待ち時間を更新
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    setPvtCount();
                    setCheckedTime(new Date()); // これは時計になる
                }
            };
            schedule.scheduleAtFixedRate(r, 0, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * ロガー等を取得する。
     */
    private void setup() {
        logger = Logger.getLogger(this.getClass());
        preferences = Preferences.userNodeForPackage(this.getClass());
        sexRenderer = preferences.getBoolean("sexRenderer", false);
        ageDisplay = preferences.getBoolean("ageDisplay", true);
        checkInterval = Project.getPreferences().getInt(Project.PVT_CHECK_INTERVAL, DEFAULT_CHECK_INTERVAL);
    }

    /**
     * GUI コンポーネントを初期化しレアイアウトする。
     */
    private void initComponents() {

        //view = new WatingListView();
        view = new WatingListPanel();
        setUI(view);

        // ラベル初期化
        view.getKutuBtn().setIcon(KUTU_ICON);

        // popup で状態アイコンの legend を出す
        JLabel legend = view.getLegendLbl();
        legend.setIcon(GUIConst.ICON_QUESTION_16);
        legend.setText("");
        view.getLegendLbl().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu legend = new LegendPopup();
                legend.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        // ATOK メモリモニタ クリックで ATOK リセット
        view.getAtokLbl().setVisible(false);
        //setAtokLabel();
        //view.getAtokLbl().addMouseListener(new MouseAdapter(){
        //    @Override
        //    public void mouseClicked(MouseEvent e) {
        //        view.getAtokLbl().setText("再起動中");
        //        ExecuteScript.restartAtok24();
        //        setAtokLabel();
        //    }
        //});

        // 来院テーブル用のパラメータ
        String[] columnNames = {" 受付","　患者 ID","　来院時間","　氏　　名","　性別","　生年月日","　ドクター", "　メ モ"," 予約","状態"};
        String[] methodNames = {"getNumber","getPatientId","getPvtDateTrimDate","getPatientName","getPatientGenderDesc",
                                "getPatientAgeBirthday","getAssignedDoctorName","getMemo","getAppointment","getState"};
        Class[] classes = {java.lang.Integer.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,
                            java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.String.class,
                            java.lang.String.class,java.lang.Integer.class};
        int[] columnWidth = {34,80,72,140,50,150,75,50,40,30};

        int startNumRows = ClientContext.getInt("watingList.startNumRows");
        int rowHeight = ClientContext.getInt("watingList.rowHeight");

        // 生成する
        pvtTable = view.getTable();
        pvtTableModel = new ObjectReflectTableModel(columnNames,startNumRows, methodNames, classes);
        pvtTable.setModel(pvtTableModel);

        // 来院情報テーブルの属性を設定する
        pvtTable.setRowHeight(rowHeight);
        pvtTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pvtTable.setRowSelectionAllowed(true);
        // Retina 対応
        // pvtTable.setShowGrid(true);
        // pvtTable.setGridColor(Color.WHITE);
        // sorter を設定
        TableRowSorter<ObjectReflectTableModel> sorter = new TableRowSorter<ObjectReflectTableModel>(pvtTableModel) {
            // ASCENDING -> DESENDING -> 初期状態 と切り替える
            @Override
            public void toggleSortOrder(int column) {
                if(column >= 0 && column < getModelWrapper().getColumnCount() && isSortable(column)) {
                    List<RowSorter.SortKey> keys = new ArrayList<RowSorter.SortKey>(getSortKeys());
                    if(!keys.isEmpty()) {
                        RowSorter.SortKey sortKey = keys.get(0);
                        if(sortKey.getColumn() == column && sortKey.getSortOrder() == SortOrder.DESCENDING) {
                            setSortKeys(null);
                            return;
                        }
                    }
                }
                super.toggleSortOrder(column);
            }
        };
        pvtTable.setRowSorter(sorter);

        // コラム幅の設定
        for (int i = 0; i <columnWidth.length; i++) {
            TableColumn column = pvtTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
            if (i != 3 && i != 5 && i != 7) { //固定幅
                column.setMaxWidth(columnWidth[i]);
                column.setMinWidth(columnWidth[i]);
            }
        }

        // レンダラを生成する
        MaleFemaleRenderer sRenderer = new MaleFemaleRenderer();
        CenterRenderer centerRenderer = new CenterRenderer();
        KarteStateRenderer stateRenderer = new KarteStateRenderer();
        int row = 0;
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(centerRenderer); // 0 受付
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(sRenderer); // 1 患者 ID
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(centerRenderer); // 2 来院時間
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(sRenderer); // 3 氏名
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(centerRenderer); // 4 性別
        pvtTable.getColumnModel().getColumn(AGE_COLUMN = row++).setCellRenderer(sRenderer); // 5 生年月日
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(sRenderer); // 6 ドクター
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(sRenderer); // 7 メモ
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(sRenderer); // 8 予約
        pvtTable.getColumnModel().getColumn(row++).setCellRenderer(stateRenderer);

        // 年齢表示をしない場合はメソッドを変更する
        if (!ageDisplay) {
            methodNames[AGE_COLUMN] = AGE_METHOD[1];
        }
        // 生年月日コラムに comparator を設定「32.10 歳(S60-01-01)」というのをソートできるようにする
        sorter.setComparator(AGE_COLUMN, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                String birthday1;
                String birthday2;
                if (ageDisplay) {
                    birthday1 = ModelUtils.getMmlBirthdayFromAge((String)o1);
                    birthday2 = ModelUtils.getMmlBirthdayFromAge((String)o2);
                    return birthday2.compareTo(birthday1);
                } else {
                    birthday1 = (String)o1;
                    birthday2 = (String)o2;
                    return birthday1.compareTo(birthday2);
                }
            }
        });

        // 日付ラベルに値を設定する
        setOperationDate(new Date());
        // チェック間隔情報を設定する
        setCheckInterval(checkInterval);
        // 来院数を設定する
        setPvtCount(0);
    }

    /**
     * コンポーネントにイベントハンドラーを登録し相互に接続する。
     */
    private void connect() {

        // Chart のリスナになる
        // 患者カルテの Open/Save/SaveTemp の通知を受けて受付リストの表示を制御する
        // EventHandler で this.updateState(newValue) が生成される
        ChartImpl.addPropertyChangeListener(ChartImpl.CHART_STATE, EventHandler.create(PropertyChangeListener.class, this, "updateState", "newValue"));

        // 靴のアイコンをクリックした時来院情報を検索する
        view.getKutuBtn().addActionListener(EventHandler.create(ActionListener.class, this, "checkFullPvt"));

        // コンテキストメニューを登録する
        ContextListener l = new ContextListener(pvtTable);
        AdditionalTableSettings.setTable(view.getTable(), l);

        // ListSelectionListener を組み込む
        pvtTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int[] rows = pvtTable.getSelectedRows();
                    if (rows == null) {
                        setSelectedPvt(null);
                    } else {
                        PatientVisitModel[] patients = new PatientVisitModel[rows.length];
                        for (int i=0; i < rows.length; i++) {
                            rows[i] = pvtTable.convertRowIndexToModel(rows[i]);
                            patients[i] = (PatientVisitModel) pvtTableModel.getObject(rows[i]);
                        }
                        setSelectedPvt(patients);
                    }
                }
            }
        });
    }

    /**
     * メインウインドウのタブで受付リストに切り替わった時
     * コールされる。
     */
    @Override
    public void enter() {
        IMEControl.setImeOff(view);
        controlMenu();
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void stop() {
    }

    /**
     * 選択されている来院情報の患者オブジェクトを返す。
     * 複数行対応 by pns
     * @return 患者オブジェクト
     */
    public PatientModel[] getPatinet() {
        if (selectedPvt == null || selectedPvt.length == 0) { return null; }

        PatientModel[] pm = new PatientModel[selectedPvt.length];
        for (int i=0; i < selectedPvt.length; i++) {
            pm[i] = selectedPvt[i].getPatient();
        }
        return pm;
    }

    /**
     * 性別レンダラかどうかを返す。
     * @return 性別レンダラの時 true
     */
    public boolean isSexRenderer() {
        return sexRenderer;
    }

    /**
     * レンダラをトグルで切り替える。
     */
    public void switchRenderere() {
        sexRenderer = !sexRenderer;
        preferences.putBoolean("sexRenderer", sexRenderer);
        if (pvtTable != null) {
            pvtTableModel.fireTableDataChanged();
        }
    }

    /**
     * 年齢表示をオンオフする。
     */
    public void switchAgeDisplay() {
        ageDisplay = !ageDisplay;
        preferences.putBoolean("ageDisplay", ageDisplay);
        if (pvtTable != null) {
            String method = ageDisplay ? AGE_METHOD[0] : AGE_METHOD[1];
            pvtTableModel.setMethodName(method, AGE_COLUMN);
        }
    }

    /**
     * 来院情報を取得する日を設定する。
     * @param date 取得する日
     */
    private void setOperationDate(Date date) {
        operationDate = date;
        String formatStr = ClientContext.getString("watingList.state.dateFormat");
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr); // 2006-11-20(水)
        view.getDateLbl().setText(sdf.format(operationDate));
    }

    /**
     * 来院情報をチェックした時刻を設定する。
     * @param date チェックした時刻
     */
    private void setCheckedTime(Date date) {
        checkedTime = date;
        String formatStr = ClientContext.getString("watingList.state.timeFormat");
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        view.getCheckedTimeLbl().setText(sdf.format(checkedTime));
    }

    /**
     * 来院情報のチェック間隔(Timer delay)を設定する。
     * @param interval チェック間隔 sec
     */
    public void setCheckInterval(int interval) {
        checkInterval = interval;

        String text;
        if (interval == 0) {
            text = ""; // 定期チェックなし
        } else {
            text = String.format("チェック間隔：%d秒", interval);
        }
        view.getIntervalLbl().setText(text);
    }

    /**
     * 来院数を設定する。
     * @param cnt 来院数
     */
    public void setPvtCount(int cnt) {
        pvtCount = cnt;
        setPvtCount();
    }
    /**
     * 来院数，待人数，待時間表示
     */
    private void setPvtCount() {
        int waitingCount = 0;
        String waitingTime = "00:00";
        List dataList = pvtTableModel.getObjectList();

        if (pvtCount > 0) {
            boolean found = false;
            int continuousCount = 0;

            for (int i = 0; i < pvtCount; i++) {
                PatientVisitModel pvt = (PatientVisitModel) dataList.get(i);
                int state = pvt.getState();
                if (state == KarteState.CLOSE_NONE || state == KarteState.OPEN_NONE) {
                    // 診察未終了レコードをカウント，最初に見つかった未終了レコードの時間から待ち時間を計算
                    waitingCount++;
                    // 診療未終了レコードの連続回数をカウント
                    continuousCount++;

                    if (!found) {
                        Date pvtDate = ModelUtils.getDateTimeAsObject(pvt.getPvtDate());
                        waitingTime = DurationFormatUtils.formatPeriod(pvtDate.getTime(), new Date().getTime(), "HH:mm");
                        found = true;
                    }
                } else {
                    // 診療未終了レコードが２件以下しかなければ待ち時間に入れない（待合室にいなかった人と判断）
                    if (continuousCount > 0 && continuousCount <= 2) {
                        //System.out.println("continuous count: " + continuousCount);
                        found = false;
                        continuousCount = 0;
                    }
                }
            }
        }
        view.getCountLbl().setText(String.format("来院数%d人，待ち%d人，待ち時間 %s", pvtCount, waitingCount, waitingTime));
    }

    /**
     * テーブル及び靴アイコンの enable/diable 制御を行う。
     * 複数行選択対応 by pns
     * @param busy pvt 検索中は true
     */
    public void setBusy(boolean busy) {

        view.getKutuBtn().setEnabled(!busy);

        if (busy) {
            getContext().block();
            saveSelectedIndex = pvtTable.getSelectedRows();
        } else {
            getContext().getGlassPane().setText("");
            getContext().unblock();
            if (saveSelectedIndex != null && saveSelectedIndex.length != 0) {
                for (int i=0; i < saveSelectedIndex.length; i++) {
                    pvtTable.getSelectionModel().addSelectionInterval(saveSelectedIndex[i], saveSelectedIndex[i]);
                }
            }
        }
    }

    /**
     * 選択されている来院情報を設定返す。
     * 複数行選択対応 by pns
     * @return 選択されている来院情報
     */
    public PatientVisitModel[] getSelectedPvt() {
        return selectedPvt;
    }

    /**
     * 選択された来院情報を設定する。
     * 複数行選択対応 by pns
     * @param 選択された来院情報
     */
    public void setSelectedPvt(PatientVisitModel[] selectedPvt) {
        this.selectedPvt = selectedPvt;
        controlMenu();
    }

    /**
     * カルテオープンメニューを制御する。
     * 複数行選択対応 by pns
     */
    private void controlMenu() {
        getContext().enabledAction(GUIConst.ACTION_OPEN_KARTE, canOpen());
    }

    /**
     * ATOK メモリモニタ
     */
    //private void setAtokLabel() {
    //    JLabel atokLabel = view.getAtokLbl();
    //    double mem = ExecuteScript.getAtok24MemSize();
    //    double increase = mem - initialAtokMemSize;
    //    atokLabel.setText(String.format("ATOK : %.1f MB（%.1f）", mem/1024D, increase/1024D));
    //    if (mem == 0) { atokLabel.setVisible(false); }
    //    else if (mem < 200000) { atokLabel.setIcon(GUIConst.PROGRSS_BAR_1); }
    //    else if (mem < 300000) { atokLabel.setIcon(GUIConst.PROGRSS_BAR_2); }
    //    else if (mem < 400000) { atokLabel.setIcon(GUIConst.PROGRSS_BAR_3); }
    //    else { atokLabel.setIcon(GUIConst.PROGRSS_BAR_4); }
    //}

    /**
     * Popupメニューから，現在選択されている全ての患者のカルテを開く。
     * 複数行選択対応 by pns
     */
    public void openKarte() {
        PatientVisitModel pvtModel[] = getSelectedPvt();
        if (pvtModel == null) { return; }

        for (int i=0; i < pvtModel.length; i++) {
            openKarte(pvtModel[i]);
        }
    }

    /**
     * 指定されたカルテを開く
     * @param pvtModel
     */
    public void openKarte(final PatientVisitModel pvtModel) {

        if (canOpen(pvtModel)) {
            stopCheckTimer();
            setBusy(true);

            // isReadOnly対応
            Runnable r = new Runnable() {
                @Override
                public void run() {

                    // 健康保険情報をフェッチする
                    PatientDelegater ptdl = new PatientDelegater();
                    ptdl.fetchHealthInsurance(pvtModel.getPatient());

                    // 現在の state をサーバからとってくる
                    PvtDelegater pvdl = new PvtDelegater();
                    int state = pvdl.getPvtState(pvtModel.getId());
                    // 読んだら table を update 　　　→ カルテが開くと update がよばれるのでここでは不要
                    //int row = getRowForPvt(pvtModel);
                    //pvtModel.setState(state);
                    //pvtTableModel.fireTableRowsUpdated(row, row);

                    // すでに OPEN ならどっかで開いているということなので編集不可に設定
                    if (KarteState.isOpen(state)) {
                        openReadOnlyKarte(pvtModel, state);
                    }
                    // OPEN でなければ，通常どおりオープン （Dolphin#openKarte を呼ぶ）
                    else  { getContext().openKarte(pvtModel); }
                    setBusy(false);
                    // startCheckTimer(); // openKarte すると ChartImpl が open するので，updateState が必ず呼ばれるので，そちらで startCheckTimer される
                }
            };
            // ここは database とは関係ないので thread で
            Thread t = new Thread(r);
            t.start();

        } else {
            // 既に開かれていれば，そのカルテを前に
            ChartImpl.toFront(pvtModel);
            EditorFrame.toFront(pvtModel);
        }
    }

    /**
     * Read Only でカルテを開く
     * @param pvtModel
     * @param state
     */
    @Override
    public void openReadOnlyKarte(final PatientVisitModel pvtModel, final int state) {
        // 元々 ReadOnly のユーザーならそのまま開いて OK
        if (Project.isReadOnly()) {
            getContext().openKarte(pvtModel);
            return;
        }

        // ダイアログで確認する
        String ptName = pvtModel.getPatientName();
        String[] options = {"閲覧のみ", "強制的に編集", "キャンセル"};

        JOptionPane pane = new JOptionPane(
            "<html>" +
            "<h3>"+ ptName + " 様のカルテは他の端末で編集中です</h3>" +
            "<p><nobr>強制的に編集した場合、後から保存したカルテが最新カルテになります<nobr></p></html>",
            JOptionPane.WARNING_MESSAGE);
        pane.setOptions(options);
        pane.setInitialValue(options[0]);
        pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 2);
        // メッセージが改行しないように大きさをセット（JDialog なら自動でやってくれる）
        Dimension size = pane.getPreferredSize();
        size.width = 540; // cut and try
        pane.setPreferredSize(size);

        // JOptionPane から component を再帰検索して forceEditBtn を取り出す
        JButton tmpForce = null;

        Component[] components = pane.getComponents();
        List<Component> cc = java.util.Arrays.asList(components);

        while (!cc.isEmpty()) {
            List<Component> stack = new ArrayList<Component>();
            for (Component c: cc) {
                if (c instanceof JButton) {
                    JButton button = (JButton) c;
                    String name = button.getText();
                    if (options[1].equals(name)) { tmpForce = button; }

                } else if (c instanceof JComponent) {
                    components = ((JComponent)c).getComponents();
                    stack.addAll(java.util.Arrays.asList(components));
                }
            }
            cc = stack;
        }
        final JButton forceEditBtn = tmpForce;

        MyJSheet dialog = MyJSheet.createDialog(pane, getContext().getFrame());
        dialog.addSheetListener(new SheetListener(){
            @Override
            public void optionSelected(SheetEvent se) {
                int result = se.getOption();
                if (result == 0) { // 閲覧
                    pvtModel.setState(KarteState.READ_ONLY);
                    getContext().openKarte(pvtModel);
                } else if (result == 1) { // 強制編集
                    pvtModel.setState(KarteState.toClosedState(state));
                    getContext().openKarte(pvtModel);
                }
                // それ以外はキャンセル
            }
        });

        // 強制編集ボタンにショートカット登録
        ActionMap am = dialog.getRootPane().getActionMap();
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "force-edit");
        am.put("force-edit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forceEditBtn.doClick();
            }
        });

        dialog.show();
    }

    /**
     * 現在の selectedPvt が canOpen かどうか判定 by pns
     * １つでも開けられないものがあれば false とする
     */
    private boolean canOpen() {

        PatientVisitModel[] pvt = getSelectedPvt();

        if (pvt == null || pvt.length == 0) {
            return false;
        } else {
            for (int i=0; i < pvt.length; i++) {
                // 既に開かれているカルテを openKarte すると toFront になるので，
                // open できないカルテは cancel されたカルテのみである
                if (isKarteCanceled(pvt[i])) { return false; }
            }
        }
        return true;
    }

    /**
     * 指定されたカルテを開くことが可能かどうかを返す。
     * @return 開くことが可能な時 true
     */
    private boolean canOpen(PatientVisitModel pvt) {
        if (pvt == null || isKarteCanceled(pvt)) { return false; }
        return !ChartImpl.isKarteOpened(pvt);
    }

    /**
     * 受付がキャンセルされているかどうかを返す。
     * @return キャンセルされている時 true
     */
    private boolean isKarteCanceled(PatientVisitModel pvtModel) {
        return pvtModel != null && pvtModel.getState() == KarteState.CANCEL_PVT;
    }

    /**
     * チャートステートの状態をデータベースに書き込む.
     * ChartImpl の windowOpened, windowClosed で呼ばれる
     * state, byomeiCount, byomeiCountToday が変化している可能性がある
     * @param updated
     */
    public void updateState(final PatientVisitModel updated) {

        //update の最中は PvtChecker を止めて，update が終了したら再開する
        stopCheckTimer();

        final int row = getRowForPvt(updated);
        if (row < 0) {
            logger.info("Something weird! Updated pvt is not found in the present pvtTable");
            return;
        }
        final int state = updated.getState();

        if (state != KarteState.READ_ONLY) {
            // データベースへの書き込み
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    PvtDelegater pdl = new PvtDelegater();
                    int serverState = pdl.getPvtState(updated.getId());
                    // サーバが NONE 以外かつクライアントが NONE の時はサーバの state を優先する
                    // i.e. NONE のカルテを強制変更でひらいて，変更しないで終了した場合，他の端末で SAVE になったのを NONE に戻してしまうのを防ぐ
                    if (! KarteState.isNone(serverState) && KarteState.isNone(state)) {
                        updated.setState(serverState);
                    }
                    pdl.updatePvt(updated);
                    pvtTableModel.fireTableRowsUpdated(row, row);

                    startCheckTimer();
                }
            };
            executor.submit(r);
        } else {
            // ReadOnly の時，state を読み直す
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    PvtDelegater pdl = new PvtDelegater();
                    updated.setState(pdl.getPvtState(updated.getId()));
                    pvtTableModel.fireTableRowsUpdated(row, row);
                    startCheckTimer();
                }
            };
            executor.submit(r);
        }
    }

    /**
     * 終了時にカルテが OPEN になったまま残るのを防ぐためにすべて CLOSED に変換
     * @return
     */
    @Override
    public Callable<Boolean> getStoppingTask() {
        logger.info("WatingListImpl stoppingTask starts");

        Callable<Boolean> longTask = new Callable<Boolean>() {

            @Override
            public Boolean call() {
                // 開いているカルテを調べる
                for(ChartImpl chart : ChartImpl.getAllChart()) {
                    logger.info("Closing remained chart: " + chart);

                    PatientVisitModel pvt = chart.getPatientVisit();

                    // 今日の受診と関係ないカルテは無視
                    if (pvt.getPvtDate() == null) { continue; }

                    // 今日の受診がある場合は pvt status を変更する（open -> close)
                    int oldState = pvt.getState();
                    boolean isEmpty = new DocumentPeeker(pvt).isKarteEmpty();
                    int newState = KarteState.toClosedState(oldState, isEmpty);

                    // サーバに書き込む
                    PvtDelegater pdl = new PvtDelegater();
                    // NONE 以外から NONE への状態変更はありえないので無視
                    // NONE のカルテを強制変更でひらいて，変更しないで終了した場合，他の端末で SAVE になったのを NONE に戻してしまうのを防ぐ
                    int serverState = pdl.getPvtState(pvt.getId());
                    if (KarteState.isNone(serverState) || ! KarteState.isNone(newState)) {
                        pvt.setState(newState);
                        pdl.updatePvt(pvt);
                    }
                }
                return true;
            }
        };

        return longTask;
    }

    /**
     * 与えられた pvt に対応する TableModel の行を返す
     * 要素が別オブジェクトになっている場合があるため、レコードIDで探す
     * @param pvt
     * @return
     */
    private int getRowForPvt(PatientVisitModel updated) {
        List pvtList = pvtTableModel.getObjectList();
        //setPvtCount(pvtTableModel.getObjectCount());
        int updatedRow = -1;

        for (int i = 0; i < pvtList.size(); i++) {
            PatientVisitModel pvt = (PatientVisitModel) pvtList.get(i);
            if (updated.getId() == pvt.getId()) {
                updatedRow = i;
                break;
            }
        }
        return updatedRow;
    }

    /**
     * 選択した患者の受付をキャンセルする。
     * 複数行選択対応 by pns
     */
    public void cancelVisit() {

        StringBuilder ptNames = new StringBuilder();

        if (selectedPvt == null || selectedPvt.length == 0) { return; }
        // 名前リスト作成
        for (int i=0; i < selectedPvt.length; i++) {
            ptNames.append(selectedPvt[i].getPatientName());
            ptNames.append("，");
        }

        // ダイアログを表示し確認する
        Object[] cstOptions = new Object[]{"はい", "いいえ"};
        ptNames.deleteCharAt(ptNames.length()-1); // 最後の "," を取り除く
        ptNames.append(" 様の受付を取り消しますか?");

        int select = MyJSheet.showOptionDialog(
                getContext().getFrame(),
                ptNames.toString(),
                ClientContext.getFrameTitle(getName()),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                cstOptions,"はい");

        // 受付を取り消す
        if (select == JOptionPane.OK_OPTION) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    for (PatientVisitModel pvt :selectedPvt) {
                        PvtDelegater pdl = new PvtDelegater();
                        // karte open なら キャンセルできない
                        if (KarteState.isOpen(pdl.getPvtState(pvt.getId()))) {
                            MyJSheet.showMessageSheet(getContext().getFrame(), "編集中のカルテはキャンセルできません");
                        } else {
                            pvt.setState(KarteState.CANCEL_PVT);
                            pdl.updatePvt(pvt);
                        }
                    }
                    MinMax row = new MinMax(pvtTable.getSelectedRows());
                    pvtTableModel.fireTableRowsUpdated(row.min, row.max);
                }
            };
            executor.submit(r);
        }
    }

    /**
     * チェックタイマーをリスタートする。
     */
    public void restartCheckTimer() {
        stopCheckTimer();
        startCheckTimer(0);
    }
    /**
     * チェックタイマーを止める(タスクが起動済みの場合はこのタスクを実行しているスレッドに割り込まないで完了を待つ)
     */
    private void stopCheckTimer() {
        if (timerHandler != null) {
            intervalToNextCheck = (int) timerHandler.getDelay(TimeUnit.SECONDS);
            boolean succeeded = timerHandler.cancel(false);
            timerHandler = null;
            //logger.info("StopCheckTimer succeeded = " + succeeded);
        } else {
            //logger.info("StopCheckTimer: no timeHandler");
        }
    }
    /**
     * チェックタイマーを second 秒後にスタートする
     * Thread から呼ばれるので，stop されないで２回呼ばれることがある
     * @param second
     */
    private void startCheckTimer(int second) {
        if (timerHandler != null ) { stopCheckTimer(); }
        // checkInterval = 0 のときはタイマーをスタートさせない
        if (checkInterval == 0) {
            //logger.info("pvt check timer is off");
        } else {
            timerHandler = schedule.scheduleWithFixedDelay(pvtChecker, second, checkInterval, TimeUnit.SECONDS);
            //logger.info("startCheckTimer in " + intervalToNextCheck + " sec");
        }
    }

    /**
     * チェックタイマーを止めた時点での残り時間から再開する
     */
    private void startCheckTimer() {
        startCheckTimer((intervalToNextCheck > 0)? intervalToNextCheck : 0);
    }
    /**
     * 更新ボタンで呼ばれる。定期チェックの方はリセットする
     */
    public void checkFullPvt() {
        stopCheckTimer();
        executor.submit(pvtChecker);
        startCheckTimer(checkInterval);
    }

    /**
     * 患者来院情報をチェックするタスク
     */
    private class PvtChecker implements Runnable {

        private PatientVisitModel pvt;
        private PvtDelegater delegater = new PvtDelegater();
        private long startTime = 0; // check にかかる時間をログ出力

        @Override
        public void run() {
            startTime = new Date().getTime();

            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    setBusy(true);
                }
            });

            final Date date = new Date();
            final String[] dateToSerach = ModelUtils.getSearchDateAsString(date);

            // 現在のテーブルサイズを firstResult とする
            List dataList = pvtTableModel.getObjectList();
            int firstResult = dataList != null ? dataList.size() : 0;
            //logger.info("check PVT at " + date + " firstResult = " + firstResult);

            // 検索する
            final List<PatientVisitModel> result = delegater.getPvt(dateToSerach, firstResult);
            int newVisitCount = result != null ? result.size() : 0;
            //logger.info("new visits = " + newVisitCount);

            // 結果を追加する
            if (newVisitCount > 0) {
                List newList = new ArrayList();
                for (int i = 0; i < newVisitCount; i++) {
                    pvt = (PatientVisitModel) result.get(i);
                    // 受付番号セット
                    pvt.setNumber(firstResult+i+1);
                    // 受付リスト追加
                    newList.add(pvt);
                }
                dataList.addAll(newList);
                pvtTableModel.fireTableRowsInserted(firstResult, firstResult + newVisitCount - 1);
            }

            // pvtState をアップデート pvtStateList.size は table の ObjectCount と同じはず
            List<PvtStateSpec> pvtStateList = delegater.getPvtState();

            if (pvtStateList != null && (pvtStateList.size() == pvtTableModel.getObjectCount())) {

                PatientVisitModel myPvt;
                ArrayList<Integer> changedRows = new ArrayList<Integer>();

                for (int i=0; i < pvtStateList.size(); i++) {
                    PvtStateSpec serverPvt = pvtStateList.get(i);
                    myPvt = (PatientVisitModel) pvtTableModel.getObject(i);

                    // サーバと違いがあればアップデート
                    // 自分でカルテを開いている場合はアップデートしない
                    if (!ChartImpl.isKarteOpened(myPvt)) {
                        boolean lineChanged = false;
                        if (myPvt.getState() != serverPvt.getState()) {
                            //logger.info("PVT state changed row=" + i + " state=" + serverPvt.getState());
                            myPvt.setState(serverPvt.getState());
                            lineChanged = true;
                        }
                        if (myPvt.getByomeiCount() != serverPvt.getByomeiCount()) {
                            //logger.info("PVT byomeiCount changed row=" + i + " byomeiCount=" + serverPvt.getByomeiCount());
                            myPvt.setByomeiCount(serverPvt.getByomeiCount());
                            lineChanged = true;
                        }
                        if (myPvt.getByomeiCountToday() != serverPvt.getByomeiCountToday()) {
                            //logger.info("PVT byomeiCountToday changed row=" + i + " byomeiCountToday=" + serverPvt.getByomeiCountToday());
                            myPvt.setByomeiCountToday(serverPvt.getByomeiCountToday());
                            lineChanged = true;
                        }
                        if (lineChanged) changedRows.add(i);
                    }
                }
                if (changedRows.size() > 0) {
                    MinMax row = new MinMax(changedRows);
                    pvtTableModel.fireTableRowsUpdated(row.min, row.max);
                    //logger.info("row changed from " + row.min + " to " + row.max);
                }
            }

            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    setCheckedTime(date);
                    setPvtCount(pvtTableModel.getObjectCount());
                    setBusy(false);
                }
            });

            // ATOK ラベル更新
            //setAtokLabel();

            //logger.info("check time " + (new Date().getTime()-startTime) + " msec");
        }
    }

    /**
     * PvtServer からの pvt meessage を受け取って pvtTableModel を更新する
     */
    private void startPvtMessageReceiver () {

        DolphinClientContext.getContext().setEndpoint(new Endpoint(){
            @Override
            public void onOpen(Session session, EndpointConfig config) {
                session.addMessageHandler(new MessageHandler.Whole<String>() {
                   @Override
                    public void onMessage(String message) {
                        // logger.debug("WatingListImpl: received pvt = " + message);
                        PatientVisitModel hostPvt = JsonConverter.fromJson(message, PatientVisitModel.class);

                        // 送られてきた pvt と同じものを local で探す
                        // pvtDate で判定する → Patient ID が同じでも，pvtDate が違えば違う受付と判断する
                        PatientVisitModel localPvt = null;
                        int row = -1;

                        for (int i=0; i<pvtTableModel.getObjectCount(); i++) {
                            PatientVisitModel p = (PatientVisitModel) pvtTableModel.getObject(i);
                            if (p.getPvtDate().equals(hostPvt.getPvtDate())) {
                                localPvt = p;
                                row = i;
                                break;
                            }
                        }

                        if (localPvt != null) {
                            logger.info("pvt state local = " + localPvt.getState() + ", server = " + hostPvt.getState());

                            // localPvt がみつかった場合，更新である
                            hostPvt.setNumber(localPvt.getNumber());
                            pvtTableModel.getObjectList().set(row, hostPvt);
                            // changeRow を fire，ただしカルテが開いていたら fire しない
                            if (! ChartImpl.isKarteOpened(localPvt)) {
                                pvtTableModel.fireTableRowsUpdated(row, row);
                            }
                            // 待ち時間更新
                            setPvtCount();

                        } else{
                            // localPvt がなければ，それは追加である
                            row = pvtTableModel.getObjectCount();
                            // 番号付加
                            hostPvt.setNumber(row+1);
                            pvtTableModel.addRow(hostPvt);
                            //logger.info("pvt added at row " + row);
                            // 患者数セット
                            setPvtCount(row+1);
                        }
                    }
                });
            }
            @Override
            public void onError(Session session, Throwable t) {
                System.out.println("WatingListImp: WebSocket error: " + t.toString());
            }

            @Override
            public void onClose(Session session, CloseReason reason) {
                System.out.println("WatingListImpl: WebSocket colosed: " + reason.getReasonPhrase());
            }
        });
    }

    /**
     * 
     */
    private class TableCellRendererBase extends DefaultTableCellRenderer {

        /**
         * Show holizontal grid
         * @param graphics
         */
        @Override
        public void paint(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            super.paint(graphics);

            g.setColor(Color.WHITE);
            g.drawLine(0, getHeight(), getWidth(), getHeight());
            g.dispose();
        }
    }

    /**
     * KarteStateRenderer
     * カルテ（チャート）の状態をレンダリングするクラス。 modified by pns
     */
    private class KarteStateRenderer extends TableCellRendererBase {
        private static final long serialVersionUID = -7654410476024116413L;

        public KarteStateRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean isFocused,
                int row, int col) {

            PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(table.convertRowIndexToModel(row));

            // 背景色の設定
            if (isSexRenderer()) {

                if (pvt !=null && pvt.getPatient().getGender().equals(IInfoModel.MALE)) {
                    this.setBackground(MALE_COLOR);
                } else if (pvt !=null && pvt.getPatient().getGender().equals(IInfoModel.FEMALE)) {
                    this.setBackground(FEMALE_COLOR);
                } else {
                    this.setBackground(table.getBackground());
                }

            } else {
                this.setBackground(table.getBackground());
            }

            // 文字色
            Color fore = pvt != null && pvt.getState() == KarteState.CANCEL_PVT ? CANCEL_PVT_COLOR : table.getForeground();
            this.setForeground(fore);

            // state の value チェック，本日のカルテがあるかどうか（今日のカルテはないけど，以前のカルテを編集しただけの場合）
            if (value != null && value instanceof Integer) {
                int state = ((Integer) value).intValue();

                switch (state) {

                    // 診察終了
                    case KarteState.CLOSE_SAVE:
                        this.setIcon(DONE_ICON);
                        break;

                    // カルテ記載がまだ
                    case KarteState.CLOSE_UNFINISHED:
                        this.setIcon(UNFINISHED_ICON);
                        break;

                    // 仮保存
                    case KarteState.CLOSE_TEMP:
                        this.setIcon(TEMPORARY_ICON);
                        break;

                    // READ_ONLY で開いている
                    case KarteState.READ_ONLY:
                        this.setIcon(OPEN_ICON);
                        break;

                    // カルテが開いている
                    case KarteState.OPEN_NONE:
                    case KarteState.OPEN_TEMP:
                        if (ChartImpl.isKarteOpened(pvt)) { this.setIcon(OPEN_ICON); }
                        else { this.setIcon(OPEN_USED_NONE); }
                        break;

                    case KarteState.OPEN_SAVE:
                        if (ChartImpl.isKarteOpened(pvt)) { this.setIcon(OPEN_ICON); }
                        else { this.setIcon(OPEN_USED_SAVE); }
                        break;

                    case KarteState.OPEN_UNFINISHED:
                        if (ChartImpl.isKarteOpened(pvt)) { this.setIcon(OPEN_ICON); }
                        else { this.setIcon(OPEN_USED_UNFINISHED); }
                        break;

                    // その他の場合はアイコン無し CLOSE_NONE はここ
                    default:
                        this.setIcon(null);
                        break;
                }

                // cancel 以外では，バックグランドも設定
                if (pvt.getState() != KarteState.CANCEL_PVT) {
                    if (pvt.isShoshin()) { this.setBackground(SHOSHIN_COLOR); }
                    if (!pvt.hasByomei()) { this.setBackground(DIAGNOSIS_EMPTY_COLOR); }
                }

                this.setText("");

            } else {
                setIcon(null);
                this.setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }

    /**
     * KarteStateRenderer
     * カルテ（チャート）の状態をレンダリングするクラス。
     */
    private class MaleFemaleRenderer extends TableCellRendererBase {

        public MaleFemaleRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean isFocused,
                int row, int col) {

            PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(table.convertRowIndexToModel(row));

            if (isSelected) {
                this.setBackground(table.getSelectionBackground());
                Color fore = pvt != null && pvt.getState() == KarteState.CANCEL_PVT ? CANCEL_PVT_COLOR : table.getSelectionForeground();
                this.setForeground(fore);

            } else {
                if (isSexRenderer()) {

                    if (pvt !=null && pvt.getPatient().getGender().equals(IInfoModel.MALE)) {
                        this.setBackground(MALE_COLOR);
                    } else if (pvt !=null && pvt.getPatient().getGender().equals(IInfoModel.FEMALE)) {
                        this.setBackground(FEMALE_COLOR);
                    } else {
                        this.setBackground(table.getBackground());
                    }

                } else {
                    this.setBackground(table.getBackground());
                }

                Color fore = pvt != null && pvt.getState() == KarteState.CANCEL_PVT ? CANCEL_PVT_COLOR : table.getForeground();
                this.setForeground(fore);
            }

            if (value != null && value instanceof String) {
                switch (col) {
                    case 1: // ID
                    case 3: // 名前
                    case 5: // 生年月日
                        this.setText(IndentTableCellRenderer.addIndent((String)value, IndentTableCellRenderer.WIDE, this.getForeground()));
                        this.setFont(NORMAL_FONT);
                        break;
                    case 6: // 診療科→ドクターに変更
                    case 7: // メモ
                        this.setText(IndentTableCellRenderer.addIndent((String)value, IndentTableCellRenderer.WIDE, this.getForeground()));
                        this.setFont(SMALL_FONT);
                        break;
                    default:
                        this.setText((String) value);
                        this.setFont(NORMAL_FONT);
                }
            } else {
                setIcon(null);
                this.setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }

    private class CenterRenderer extends MaleFemaleRenderer {

        public CenterRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }
    }

    /**
     * 受付リストのコンテキストメニュークラス。
     * modified by pns
     */
    private class ContextListener extends AbstractMainComponent.ContextListener<PatientVisitModel> {

        public ContextListener(JTable table) {
            super(table);
        }

        @Override
        public void openKarteCommand(PatientVisitModel pvt) {
            openKarte(pvt);
        }

        @Override
        public void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {

                contextMenu.removeAll();
                String pop3 = ClientContext.getString("watingList.popup.oddEvenRenderer");
                String pop4 = ClientContext.getString("watingList.popup.sexRenderer");
                String pop5 = "年齢表示";

                if (canOpen()) {
                    String pop1 = ClientContext.getString("watingList.popup.openKarte");
                    String pop2 = ClientContext.getString("watingList.popup.cancelVisit");
                    JMenuItem openKarte = new JMenuItem(new ReflectAction(pop1, WatingListImpl.this, "openKarte"));
                    JMenuItem cancelVisit = new JMenuItem(new ReflectAction(pop2, WatingListImpl.this, "cancelVisit"));
                    openKarte.setIconTextGap(8);
                    cancelVisit.setIconTextGap(8);
                    contextMenu.add(openKarte);
                    contextMenu.add(cancelVisit);
                    contextMenu.addSeparator();
                }

                JRadioButtonMenuItem oddEven = new JRadioButtonMenuItem(new ReflectAction(pop3, WatingListImpl.this, "switchRenderere"));
                JRadioButtonMenuItem sex = new JRadioButtonMenuItem(new ReflectAction(pop4, WatingListImpl.this, "switchRenderere"));
                ButtonGroup bg = new ButtonGroup();
                bg.add(oddEven);
                bg.add(sex);
                contextMenu.add(oddEven);
                contextMenu.add(sex);
                if (sexRenderer) {
                    sex.setSelected(true);
                } else {
                    oddEven.setSelected(true);
                }

                JCheckBoxMenuItem item = new JCheckBoxMenuItem(pop5);
                contextMenu.add(item);
                item.setSelected(ageDisplay);
                item.addActionListener(EventHandler.create(ActionListener.class, WatingListImpl.this, "switchAgeDisplay"));
                oddEven.setIconTextGap(12);
                sex.setIconTextGap(12);
                item.setIconTextGap(12);

                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * 配列の中から最大値，最小値を調べる
     */
    private class MinMax {
        public int max = -1;
        public int min = -1;

        public MinMax(int[] rows) {
            set(rows);
        }
        public MinMax(ArrayList<Integer> list) {
            if (list == null || list.isEmpty()) { return; }
            int[] rows = new int[list.size()];
            int count = 0;
            for(Integer i: list) { rows[count++] = i; }
            set(rows);
        }
        private void set(int[] rows) {
            if (rows == null || rows.length == 0) { return; }

            min = rows[0]; max = rows[0];
            if (rows.length == 1) { return; }

            for (int i=1; i < rows.length ; i++) {
                min = Math.min(min, rows[i]);
                max = Math.max(max, rows[i]);
            }
        }
    }
}

package open.dolphin.impl.care;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import javax.swing.*;
import open.dolphin.client.*;
import open.dolphin.delegater.AppointmentDelegater;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.project.Project;

/**
 * CareMap Document.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class CareMapDocument extends AbstractChartDocument {

    public static final String MARK_EVENT_PROP = "MARK_EVENT_PROP";
    public static final String PERIOD_PROP = "PERIOD_PROP";
    public static final String CALENDAR_PROP = "CALENDAR_PROP";
    public static final String SELECTED_DATE_PROP = "SELECTED_DATE_PROP";
    public static final String SELECTED_APPOINT_DATE_PROP = "SELECTED_DATE_PROP";
    public static final String APPOINT_PROP = "APPOINT_PROP";

    private enum Order {
        処方("medOrder"), 処置("treatmentOrder"), 指導("instractionChargeOrder"),
        ラボテスト("testOrder"), 生体検査("physiologyOrder"), 放射線("radiologyOrder"),
        ;
        private final String code;
        private Order(String c) { code = c; }
        public String code() { return code; }
    }

    private enum Appoint {
        再診(new Color(255,165,0)), 検体検査(new Color(255,69,0)),
        画像診断(new Color(119,200,211)), その他(new Color(251, 239, 128)),
        ;
        private final Color color;
        private Appoint(Color c) { color = c; }
        public Color color() { return color; }
    }

    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;
    private static final String TITLE = "治療履歴";

    //private JComboBox<String> orderCombo;
    private OrderHistoryPanel history;
    private AppointTablePanel appointTable;
    //private ImageHistoryPanel imagePanel;
    private JPanel historyContainer;
    //private final String imageEvent = "image";
    // Calendars
    private SimpleCalendarPanel c0;
    private SimpleCalendarPanel c1;
    private SimpleCalendarPanel c2;
    // 抽出期間
    private Period selectedPeriod;
    private int origin; // 基準となる月（今月＝０）
    private PropertyChangeSupport boundSupport;
    // 3ヶ月分のカレンダーセット
    private HashMap<Integer, SimpleCalendarPanel> calendarCache;
    // SimpleCalendarPool のインスタンス
    private SimpleCalendarPanel.SimpleCalendarPool simpleCalendar;
    // Order コンボで選択されている event code
    private String selectedEvent;

    private JButton updateAppoBtn; // 予約の更新はこのボタンで行う

    // モジュール検索関連
    private List<List<ModuleModel>> allModules;
    private List<List<AppointmentModel>> allAppointments;
    //private List<List<ImageEntry>> allImages;

    //private javax.swing.Timer taskTimer;

    public CareMapDocument() {
    }

    /**
     * 初期化する.
     * start() から呼ばれる
     */
    private void initialize() {
        setTitle(TITLE);

        calendarCache = new HashMap<>();
        Chart chartCtx = getContext();

        JPanel myPanel = getUI();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        // 先月、今月、来月のカレンダーを生成する
        simpleCalendar = SimpleCalendarPanel.SimpleCalendarPool.getInstance();
        c0 = simpleCalendar.acquireSimpleCalendar(origin - 1);
        c1 = simpleCalendar.acquireSimpleCalendar(origin);
        c2 = simpleCalendar.acquireSimpleCalendar(origin + 1);
        c0.setChartContext(chartCtx);
        c1.setChartContext(chartCtx);
        c2.setChartContext(chartCtx);
        c0.setParent(this);
        c1.setParent(this);
        c2.setParent(this);
        calendarCache.put(origin - 1, c0);
        calendarCache.put(origin,     c1);
        calendarCache.put(origin + 1, c2);

        // 3ケ月分のカレンダーを配置する
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        composeCalendarPanel(panel);

        // カレンダーの範囲を１ケ月以に戻すボタン
        JButton prevBtn = new JButton(GUIConst.ICON_GO_PREVIOUS_16);
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // クリックされたら (c0 | c1 | c2) -> (c0=test | c1=c0 | c2=c1)
                origin--;
                SimpleCalendarPanel save = c0;
                SimpleCalendarPanel test = calendarCache.get(origin - 1);

                if (test != null) {
                    // Pool されていた場合
                    c0 = test;

                } else {
                    // 新規に作成
                    c0 = simpleCalendar.acquireSimpleCalendar(origin - 1);
                    c0.setChartContext(getContext());
                    c0.setParent(CareMapDocument.this);
                    // カレンダの日をクリックした時に束縛属性通知を受けるリスナ
                    addPropertyChangeListenerToCalendarPanel(c0);
                    calendarCache.put(origin - 1, c0);
                }

                c2 = c1;
                c1 = save;
                composeCalendarPanel(panel);

                // オーダ履歴の抽出期間全体が変化したので通知する
                Period p = new Period(this);
                p.setStartDate(c0.getFirstDate());
                p.setEndDate(c2.getLastDate());
                setSelectedPeriod(p);
            }
        });

        // カレンダーの範囲を１ケ月送るボタン
        JButton nextBtn = new JButton(GUIConst.ICON_GO_NEXT_16);
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // クリックされたら (c0 | c1 | c2) -> (c0=c1 | c1=c2 | c2=test)
                origin++;
                SimpleCalendarPanel save = c2;
                SimpleCalendarPanel test = calendarCache.get(origin + 1);

                if (test != null) {
                    // Pool されていた場合
                    c2 = test;

                } else {
                    // 新規に作成する
                    c2 = simpleCalendar.acquireSimpleCalendar(origin + 1);
                    c2.setChartContext(getContext());
                    c2.setParent(CareMapDocument.this);
                    // カレンダの日をクリックした時に束縛属性通知を受けるリスナ
                    addPropertyChangeListenerToCalendarPanel(c2);
                    calendarCache.put(origin + 1, c2);
                }

                c0 = c1;
                c1 = save;
                composeCalendarPanel(panel);

                // オーダ履歴の抽出期間全体が変化したので通知する
                Period p = new Period(this);
                p.setStartDate(c0.getFirstDate());
                p.setEndDate(c2.getLastDate());
                setSelectedPeriod(p);
            }
        });

        // 予約表テーブルを生成する
        updateAppoBtn = new JButton(GUIConst.ICON_FLOPPY_22);
        updateAppoBtn.setEnabled(false);
        updateAppoBtn.addActionListener(e -> save());

        appointTable = new AppointTablePanel(updateAppoBtn);
        appointTable.setParent(this);
        appointTable.setBorder(BorderFactory.createTitledBorder("予約表"));
        appointTable.setPreferredSize(new Dimension(500, 260));

        // オーダ履歴表示用テーブルを生成する
        history = new OrderHistoryPanel();
        history.setPid(chartCtx.getPatient().getPatientId());

        // 画像履歴用のパネルを生成する
        //imagePanel = new ImageHistoryPanel();
        //imagePanel.setMyParent(this);
        //imagePanel.setPid(chartCtx.getPatient().getPatientId());

        // 表示するオーダを選択する Combo, カレンダーの送る、戻るボタンを配置するパネル
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.X_AXIS));

        // オーダ選択用のコンボボックス
        JComboBox<Order> orderCombo = new JComboBox<>(Order.values());
        Dimension dim = new Dimension(100, 26);
        orderCombo.setPreferredSize(dim);
        orderCombo.setMaximumSize(dim);
        //ComboBoxRenderer r = new ComboBoxRenderer();
        //orderCombo.setRenderer(r);
        orderCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // 選択されたオーダをイベント属性に設定する
                String event = ((Order) orderCombo.getSelectedItem()).code();
                setSelectedEvent(event);
            }
        });
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(prevBtn);
        commandPanel.add(Box.createHorizontalStrut(5));
        commandPanel.add(orderCombo);
        commandPanel.add(Box.createHorizontalStrut(5));
        commandPanel.add(nextBtn);
        commandPanel.add(Box.createHorizontalGlue());

        JPanel hanrei = new JPanel();
        hanrei.setLayout(new BoxLayout(hanrei, BoxLayout.X_AXIS));

        hanrei.add(new JLabel("予約( "));
        Arrays.asList(Appoint.values()).forEach(appoint -> {
            Icon icon = new ColorFillIcon(appoint.color());
            AppointLabel label = new AppointLabel(appoint.name(), icon, SwingConstants.CENTER);
            hanrei.add(label);
            hanrei.add(Box.createHorizontalStrut(7));
        });
        hanrei.remove(hanrei.getComponentCount()-1); // 最後の Strut を除去
        hanrei.add(new JLabel(" )"));

        hanrei.add(Box.createHorizontalStrut(7));

        Color birthC = new Color(0,255,255);
        hanrei.add(new JLabel("誕生日", new ColorFillIcon(birthC), SwingConstants.CENTER));
        hanrei.add(Box.createHorizontalStrut(11));

        commandPanel.add(hanrei);

        myPanel.add(panel);
        myPanel.add(Box.createVerticalStrut(7));
        myPanel.add(commandPanel);
        myPanel.add(Box.createVerticalStrut(7));

        // 検査履歴と画像歴の切り替えコンテナ
        historyContainer = new JPanel(new BorderLayout());
        historyContainer.add(history, BorderLayout.CENTER);
        historyContainer.setBorder(BorderFactory.createTitledBorder("履 歴"));
        myPanel.add(historyContainer);

        myPanel.add(Box.createVerticalStrut(7));
        myPanel.add(appointTable);

        myPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        // イベントとリスナの関係を設定する
        addPropertyChangeListenerToCalendarPanel(c0);
        addPropertyChangeListenerToCalendarPanel(c1);
        addPropertyChangeListenerToCalendarPanel(c2);

        // カレンダーセットの変更通知
        addPropertyChangeListener(CALENDAR_PROP, appointTable);
    }

    /**
     * Compose Calendar Panel.
     * @param panel
     */
    private void composeCalendarPanel(JPanel panel) {
        panel.removeAll();

        panel.add(Box.createHorizontalStrut(11));
        panel.add(c0);
        panel.add(Box.createHorizontalStrut(11));
        panel.add(c1);
        panel.add(Box.createHorizontalStrut(11));
        panel.add(c2);
        panel.add(Box.createHorizontalStrut(11));

        panel.revalidate();
    }

    /**
     * SimpleCalendarPanel に PropertyChangeListener を付ける.
     * @param c
     */
    private void addPropertyChangeListenerToCalendarPanel(SimpleCalendarPanel c) {
        // AppointTablePanel で使う
        c.addPropertyChangeListener(APPOINT_PROP, appointTable);
        // カレンダーの日を選択した時に通知されるもの
        c.addPropertyChangeListener(SELECTED_DATE_PROP, history);
        // カレンダ上の予約日を選択された時に通知されるもの
        c.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
    }

    @Override
    public void start() {
        initialize();
        enter();
        // 最初に選択されているオーダの履歴を表示する
        setSelectedEvent(Order.処方.code());
        Period period = new Period(this);
        period.setStartDate(c0.getFirstDate());
        period.setEndDate(c2.getLastDate());
        setSelectedPeriod(period);
    }

    @Override
    public void stop() {
    }

    /**
     * オーダを表示するカラーを返す。
     * @param order
     * @return カラー
     */
    public Color getOrderColor(String order) {
        Color ret = Color.PINK;
        return ret;
    }

    /**
     * 予約のカラーを返す。
     * @param target
     * @return カラー
     */
    public Color getAppointColor(String target) {
        Optional<Appoint> found = Arrays.asList(Appoint.values()).stream()
                .filter(Predicate.isEqual(target)).findFirst();
        return found.isPresent()? found.get().color() : Color.WHITE;
    }

    /**
     * プロパティチェンジリスナを追加する.     *
     * @param prop プロパティ名
     * @param l リスナ
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     * プロパティチェンジリスナを削除する.
     * @param prop プロパティ名
     * @param l リスナ
     */
    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * 表示している期間内にあるモジュールの日をマークする.
     * @param newModules  表示している期間内にあるモジュールのリスト
     */
    public void setAllModules(List<List<ModuleModel>> newModules) {
        if (newModules == null || newModules.isEmpty()) { return; }

        allModules = newModules;

        c0.setModuleList(selectedEvent, allModules.get(0));
        c1.setModuleList(selectedEvent, allModules.get(1));
        c2.setModuleList(selectedEvent, allModules.get(2));

        history.setModuleList(allModules);
    }

    /**
     * 表示している期間内にある予約日をマークする.
     * @param allAppo 表示している期間内にある予約日のリスト
     */
    public void setAllAppointments(List<List<AppointmentModel>> allAppo) {
        if (allAppo == null || allAppo.isEmpty()) { return; }

        allAppointments = allAppo;

        c0.setAppointmentList(allAppointments.get(0));
        c1.setAppointmentList(allAppointments.get(1));
        c2.setAppointmentList(allAppointments.get(2));

        notifyCalendar();
    }

    /**
     * 抽出期間が変更された場合、現在選択されているイベントに応じ、 モジュールまたは画像履歴を取得する。
     */
    public void setSelectedPeriod(Period p) {
        selectedPeriod = p;
        getModuleList(true);
    }

    /**
     * カレンダーセットの変更通知をする。
     */
    private void notifyCalendar() {
        SimpleCalendarPanel[] sc = new SimpleCalendarPanel[3];
        sc[0] = c0;
        sc[1] = c1;
        sc[2] = c2;
        boundSupport.firePropertyChange("CALENDAR_PROP", null, sc);
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * 表示するオーダが変更された場合、選択されたイベントに応じ、 モジュールまたは画像履歴を取得する。
     * @param code
     */
    public void setSelectedEvent(String code) {
        selectedEvent = code;
        getModuleList(false);
    }

    /**
     * 設定されている curEvent と抽出期間からモジュールのリストを取得する.
     * @param withAppo
     */
    private void getModuleList(final boolean withAppo) {
        if (selectedEvent == null || selectedPeriod == null) { return; }

        final ModuleSearchSpec spec = new ModuleSearchSpec();
        spec.setCode(ModuleSearchSpec.ENTITY_SEARCH);
        spec.setKarteId(getContext().getKarte().getId());
        spec.setEntity(selectedEvent);
        spec.setStatus("F");

        // カレンダ別に検索する
        Date[] fromDate = new Date[3];
        fromDate[0] = ModelUtils.getDateTimeAsObject(c0.getFirstDate() + "T00:00:00");
        fromDate[1] = ModelUtils.getDateTimeAsObject(c1.getFirstDate() + "T00:00:00");
        fromDate[2] = ModelUtils.getDateTimeAsObject(c2.getFirstDate() + "T00:00:00");
        spec.setFromDate(fromDate);

        Date[] toDate = new Date[3];
        toDate[0] = ModelUtils.getDateTimeAsObject(c0.getLastDate() + "T23:59:59");
        toDate[1] = ModelUtils.getDateTimeAsObject(c1.getLastDate() + "T23:59:59");
        toDate[2] = ModelUtils.getDateTimeAsObject(c2.getLastDate() + "T23:59:59");
        spec.setToDate(toDate);

        DBTask<Object> task = new DBTask<Object>(getContext()) {
            List<List<ModuleModel>> moduleLists;
            List<List<AppointmentModel>> appointLists;

            @Override
            public Object doInBackground() throws Exception {
                DocumentDelegater ddl = new DocumentDelegater();
                moduleLists = ddl.getModuleList(spec);
                if (withAppo) { appointLists = ddl.getAppoinmentList(spec); }

                return null;
            }

            @Override
            public void succeeded(Object result) {
                setAllModules(moduleLists);
                if (withAppo) { setAllAppointments(appointLists); }
            }
        };

        task.execute();
    }

    @Override
    public void setDirty(boolean dirty) {
        if (isDirty() != dirty) {
            super.setDirty(dirty);
            updateAppoBtn.setEnabled(isDirty());
        }
    }

    /**
     * 新規及び変更された予約を保存する.
     */
    @Override
    public void save() {
        final ArrayList<AppointmentModel> results = new ArrayList<>();

        // カレンダー単位に抽出する
        for (SimpleCalendarPanel c : calendarCache.values()) {

            if (c.getRelativeMonth() >= 0) {

                List<AppointmentModel> list = c.getUpdatedAppoints();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    AppointmentModel appo = list.get(i);

                    // 新規予約のみEJB3.0の関係を設定する
                    if (appo.getKarte() == null) {
                        appo.setKarte(getContext().getKarte());
                    }
                    appo.setCreator(Project.getUserModel());

                    // 確定日、記録日、開始日
                    // 現状の実装はここまで
                    Date confirmed = new Date();
                    appo.setConfirmed(confirmed);
                    appo.setRecorded(confirmed);
                    if (appo.getStarted() == null) {
                        appo.setStarted(confirmed);
                    }
                    // 常にFINAL
                    appo.setStatus(IInfoModel.STATUS_FINAL);

                    results.add(list.get(i));
                }
            }
        }

        if (results.isEmpty()) {
            return;
        }

        final AppointmentDelegater adl = new AppointmentDelegater();

        DBTask task = new DBTask<Void>(getContext()) {

            @Override
            protected Void doInBackground() throws Exception {
                adl.putAppointments(results);
                return null;
            }

            @Override
            public void succeeded(Void result) {
                setDirty(false);
            }
        };

        task.execute();
    }
}

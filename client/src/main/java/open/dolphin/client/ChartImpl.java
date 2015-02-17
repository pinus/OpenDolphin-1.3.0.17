package open.dolphin.client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.Task;
import open.dolphin.helper.WindowSupport;
import open.dolphin.impl.care.CareMapDocument;
import open.dolphin.impl.lbtest.LaboTestBean;
import open.dolphin.impl.pinfo.PatientInfoDocument;
import open.dolphin.infomodel.*;
import open.dolphin.inspector.DiagnosisInspector;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.inspector.MemoInspector;
import open.dolphin.inspector.PatientInspector;
import open.dolphin.project.Project;
import open.dolphin.ui.*;
import open.dolphin.util.GUIDGenerator;
import open.dolphin.util.PreferencesUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;

/**
 * 2号カルテ、傷病名、検査結果履歴等、患者の総合的データを提供するクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class ChartImpl extends AbstractMainTool implements Chart, IInfoModel {
    private static final long serialVersionUID = 1L;

    /** カルテ状態の束縛プロパティ名 */
    public static final String CHART_STATE = "chartStateProp";

    //  Chart インスタンスを管理するstatic 変数
    private static ArrayList<ChartImpl> allCharts = new ArrayList<ChartImpl>(3);
    // Chart 状態の通知を行うための static 束縛サポート
    private static PropertyChangeSupport boundSupport = new PropertyChangeSupport(new Object());
    /// Document Plugin を格納する TabbedPane
    private PNSTabbedPane tabbedPane;
    // Active になっているDocument Plugin
    private HashMap<String, ChartDocument> providers;
    // 患者インスペクタ
    private PatientInspector inspector;
    // Window Menu をサポートする委譲クラス
    private WindowSupport windowSupport;
    // Toolbar
    private JPanel myToolPanel;
    // 検索状況等を表示する共通のパネル
    private StatusPanel statusPanel;
    // 患者来院情報
    private PatientVisitModel pvt;
    // Read Only の時 true
    private boolean readOnly;
    // Chart のステート
    private int chartState;
    // Chart内のドキュメントに共通の MEDIATOR
    private ChartMediator mediator;
    // State Mgr
    private StateMgr stateMgr;
    // MML送信 listener
    private MmlMessageListener mmlListener;
    // CLAIM 送信 listener
    private ClaimMessageListener claimListener;
    // このチャートの KarteBean
    private KarteBean karte;
    // GlassPane
    private BlockGlass blockGlass;
    // 最新の受診歴
    private LastVisit lastVisit;
    // Logger
    private Logger logger;

    private Preferences prefs;
    public static final String PN_FRAME = "chart.frame";

    // getDiagnosisDocument() に loadDocuments() が終わったことを知らせるオブジェクト
    public final boolean[] loadDocumentsDone = {false};

    /**
     * Creates new ChartService
     */
    public ChartImpl() {
        logger = Logger.getLogger(ChartImpl.class);
        prefs = Project.getPreferences();
    }

    /**
     * このチャートのカルテを返す。
     * @return カルテ
     */
    @Override
    public KarteBean getKarte() {
        return karte;
    }

    /**
     * このチャートのカルテを設定する。
     * @param karte このチャートのカルテ
     */
    @Override
    public void setKarte(KarteBean karte) {
        this.karte = karte;
    }

    /**
     * Chart の JFrame を返す。
     * @return チャートウインドウno JFrame
     */
    @Override
    public MainFrame getFrame() {
        return windowSupport.getFrame();
    }

    /**
     * Chart内ドキュメントが共通に使用する Status パネルを返す。
     * @return IStatusPanel
     */
    @Override
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    /**
     * Chart内ドキュメントが共通に使用する Status パネルを設定する。
     * @param statusPanel IStatusPanel
     */
    @Override
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * 来院情報を設定する。
     * @param pvt 来院情報
     */
    @Override
    public void setPatientVisit(PatientVisitModel pvt) {
        this.pvt = pvt;
    }

    /**
     * 来院情報を返す。
     * @return 来院情報
     */
    @Override
    public PatientVisitModel getPatientVisit() {
        return pvt;
    }

    /**
     * ReadOnly かどうかを返す。
     * @return ReadOnlyの時 true
     */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * ReadOnly 属性を設定する。
     * @param readOnly ReadOnly user の時 true
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * このチャートが対象としている患者モデルを返す。
     * @return チャートが対象としている患者モデル
     */
    @Override
    public PatientModel getPatient() {
        return getKarte().getPatient();
    }

    /**
     * このチャートが対象としている患者モデルを設定する。
     * @param patientModel チャートが対象とする患者モデル
     */
    public void setPatientModel(PatientModel patientModel) {
        this.getKarte().setPatient(patientModel);
    }

    /**
     * チャートのステート属性を返す。
     * @return チャートのステート属性
     */
    @Override
    public int getChartState() {
        return chartState;
    }

    /**
     * チャートのステートを設定する。
     * @param chartState チャートステート
     */
    @Override
    public void setChartState(int chartState) {
        this.chartState = chartState;
        //
        // インスタンスを管理する static オブジェクト
        // を使用し束縛リスナへ通知する
        //
        ChartImpl.fireChanged(this);
    }

    /**
     * チャート内で共通に使用する Mediator を返す。
     * @return ChartMediator
     */
    @Override
    public ChartMediator getChartMediator() {
        return mediator;
    }

    /**
     * チャート内で共通に使用する Mediator を設定する。
     * @param mediator ChartMediator
     */
    public void setChartMediator(ChartMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Menu アクションを制御する。
     */
    @Override
    public void enabledAction(String name, boolean enabled) {
        Action action = mediator.getAction(name);
        if (action != null) {
            action.setEnabled(enabled);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * 文書ヒストリオブジェクトを返す。
     * @return 文書ヒストリオブジェクト DocumentHistory
     */
    @Override
    public DocumentHistory getDocumentHistory() {
        return inspector.getDocumentHistory();
    }
//pns^
    /**
     * 病名インスペクタを返す
     */
    public DiagnosisInspector getDiagnosisInspector() {
        return inspector.getDiagnosisInspector();
    }

    public LastVisit getLastVisit() {
        return lastVisit;
    }
//pns$

    /**
     * 引数で指定されたタブ番号のドキュメントを表示する。
     * @param 表示するドキュメントのタブ番号
     */
    @Override
    public void showDocument(int index) {
        int cnt = tabbedPane.getTabCount();
        if (index >= 0 && index <= cnt - 1 && index != tabbedPane.getSelectedIndex()) {
            tabbedPane.setSelectedIndex(index);
        }
    }

    /**
     * チャート内に未保存ドキュメントがあるかどうかを返す。
     * @return 未保存ドキュメントがある時 true
     */
    @Override
    public boolean isDirty() {

        boolean dirty = false;

        if (providers != null && providers.size() > 0) {
            Collection<ChartDocument> docs = providers.values();
            for (ChartDocument doc : docs) {
                if (doc.isDirty()) {
                    dirty = true;
                    break;
                }
            }
        }
        return dirty;
    }

    @Override
    public void start() {

        String sama = ClientContext.getString("sama");
        int maxEstimation = ClientContext.getInt("chart.maxEstimation"); //30000
        //int delay = ClientContext.getInt("chart.timerDelay"); // 200
        String message = "カルテオープン";
        String patientName = getPatientVisit().getPatient().getFullName() + sama;
        String note = patientName + "を開いています...";

        Task task = new Task<KarteBean>(null, message, note, maxEstimation) {

            @Override
            protected KarteBean doInBackground() throws Exception {
                logger.debug("CahrtImpl start task doInBackground");
                //
                // Database から患者のカルテを取得する
                //
                int past = Project.getPreferences().getInt(Project.DOC_HISTORY_PERIOD, -12);
                GregorianCalendar today = new GregorianCalendar();
                today.add(GregorianCalendar.MONTH, past);
                today.clear(Calendar.HOUR_OF_DAY);
                today.clear(Calendar.MINUTE);
                today.clear(Calendar.SECOND);
                today.clear(Calendar.MILLISECOND);
                DocumentDelegater ddl = new DocumentDelegater();
                KarteBean karteBean = ddl.getKarte(getPatientVisit().getPatient().getId(), today.getTime());
                return karteBean;
            }

            @Override
            protected void succeeded(KarteBean karteBean) {
                logger.debug("CahrtImpl start task succeeded");
                karteBean.setPatient(null);

                karteBean.setPatient(getPatientVisit().getPatient());
                setKarte(karteBean);
                initComponents();
                logger.debug("initComponents end");
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        getDocumentHistory().showHistory();
                    }
                });
            }

            @Override
            protected void failed(Throwable t) {
                t.printStackTrace(System.err);
            }
        };
        //task.setMillisToPopup(delay);
        task.execute();
    }

    /**
     * 患者のカルテを検索取得し、GUI を構築する。
     * このメソッドはバックグランドスレッドで実行される。
     */
    public void initComponents() {

        // このチャート の Frame を生成し初期化する。
        // Frame のタイトルを
        // 患者氏名(カナ):患者ID に設定する
        //

        // Frame と MenuBar を生成する
        String title = String.format("%s - %s（%s）: %s",
                ClientContext.getString("chart.chartStr"),
                getPatient().getFullName(),
                getPatient().getKanaName().replace("　", " "),
                getPatient().getPatientId());

        windowSupport = WindowSupport.create(title);

        // チャート用のメニューバーを得る
        JMenuBar myMenuBar = windowSupport.getMenuBar();

        // チャートの JFrame オブジェクトを得る
        final MainFrame frame = windowSupport.getFrame();
        frame.setName("chartFrame");

        // 患者インスペクタを生成する
        inspector = new PatientInspector(this);
        //pns inspector.getPanel().setBorder(BorderFactory.createEmptyBorder(7, 7, 5, 2)); // カット&トライ
        inspector.getPanel().setBorder(BorderFactory.createEmptyBorder()); // カット&トライ

        // Status パネルを生成する
        statusPanel = frame.getStatusPanel();
        //((JPanel)statusPanel).setBorder(BorderFactory.createEmptyBorder());
//pns^
        // lastVisit を設定する
        lastVisit = new LastVisit(this);
//pns$
        // Status パネルに表示する情報を生成する
        // カルテ登録日 Status パネルの右側に配置する
        String rdFormat = ClientContext.getString("common.dateFormat");         // yyyy-MM-dd
        String rdPrifix = ClientContext.getString("common.registeredDatePrefix");     // カルテ登録日:
        String patienIdPrefix = ClientContext.getString("common.registeredDatePatientIdPrefix"); // 患者ID:
        Date date = getKarte().getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat(rdFormat);
        String created = sdf.format(date);
        //statusPanel.setRightInfo(rdPrifix + " " + created);           // カルテ登録日:yyyy/mm/dd

//pns^  status panel 設定
        // 患者ID Status パネルの左に配置する
        //statusPanel.setLeftInfo(patienIdPrefix + " " + getKarte().getPatient().getPatientId()); // 患者ID:xxxxxx
        statusPanel.add("", "message"); // key "message" の JLabel : AbstractChartDocument#enter と LaboTestBean で使う
        statusPanel.addGlue();
        statusPanel.addProgressBar();
        statusPanel.addSeparator();
        statusPanel.add(patienIdPrefix + " " + getKarte().getPatient().getPatientId());
        statusPanel.addSeparator();
        statusPanel.add(rdPrifix + " " + created);
        statusPanel.setTopLineAlpha(0.4f);

        Date pvtDate = ModelUtils.getDateTimeAsObject(pvt.getPvtDate());
        if (pvtDate != null && pvt.getState() == KarteState.CLOSE_NONE) { // window open 前に呼ばれる
            String waitingTime = DurationFormatUtils.formatPeriod(pvtDate.getTime(), new Date().getTime(), "HH:mm");
            //statusPanel.setRightInfo(rdPrifix + " " + created + "  待ち時間 " + waitingTime);
            statusPanel.addSeparator();
            statusPanel.add("待ち時間 " + waitingTime);
        }
        statusPanel.setMargin(4);
//pns$

        // ChartMediator を生成する
        mediator = new ChartMediator(this);

        // Menu を生成する
        MenuFactory appMenu = new MenuFactory();
        appMenu.setMenuSupports(getContext().getMenuSupport(), mediator);
        appMenu.build(myMenuBar);
        mediator.registerActions(appMenu.getActionMap());
        myToolPanel = appMenu.getToolPanelProduct();
        myToolPanel.add(inspector.getBasicInfoInspector().getPanel(), 0);
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(24,16));
        spacer.setMinimumSize(new Dimension(24,16));
        spacer.setOpaque(false);
        myToolPanel.add(spacer, 1);

        //
        // このクラス固有のToolBarを生成する
        //
        ChartToolBar toolBar = new ChartToolBar(this);
        myToolPanel.add(toolBar);

        // Document プラグインのタブを生成する
        tabbedPane = loadDocuments();

        // 全体をレイアウトする
//      final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inspector.getPanel(), tabbedPane);
//      splitPane.setName("splitPane");

//      JPanel myPanel = new JPanel();
//      myPanel.setOpaque(true);

//      myPanel.setLayout(new BorderLayout());
//      myPanel.add(myToolPanel, BorderLayout.NORTH);
//      myPanel.add(splitPane, BorderLayout.CENTER);
//pns$
//      myPanel.add((JPanel) statusPanel, BorderLayout.SOUTH);
//      frame.setContentPane(myPanel);

        MainFrame.CommandPanel comPanel = frame.getCommandPanel();
        myToolPanel.setOpaque(false);
        comPanel.add(myToolPanel);
        comPanel.setBackgroundColor(Color.BLACK, 0f, 0.1f);

        final MainFrame.MainPanel mainPanel = frame.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(inspector.getPanel(), BorderLayout.WEST);

        // StateMgr を生成する
        stateMgr = new StateMgr();

        // BlockGlass を設定する
        blockGlass = new BlockGlass();
        frame.setGlassPane(blockGlass);

        // このチャートの Window にリスナを設定する
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // CloseBox の処理を行う
                processWindowClosing();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                // Window がオープンされた時の処理を行う
                ChartImpl.windowOpened(ChartImpl.this);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // Window がクローズされた時の処理を行う
                // stop で setVisible(false) を出した後に呼ばれる
                ChartImpl.windowClosed(ChartImpl.this);
            }

            @Override
            public void windowActivated(WindowEvent e) {

//pns^          マウスの場所に応じてフォーカス処理を行う
                Point p = frame.getMousePosition(true);
                Component c = null;
                String compName = null;
                if (p != null) c = frame.findComponentAt(p);

                // マウスが stamp にあればフォーカス移動しない
                // ドラッグ開始時にフォーカス移動があるとばたばたするので
                if (c instanceof ComponentHolder) {
                    //System.out.println("focus transition canceled");
                    return;
                }
                if (c != null) compName = c.getName();

                // マウスがメモにあれば，メモにフォーカス
                if (MemoInspector.NAME.equals(compName)) {
                    c.requestFocusInWindow();

                // マウスが DiagnosisInspector にあれば何もしない
                } else if (DiagnosisInspector.NAME.equals(compName)) {
                    return;

                } else {

//              それ以外は plugin の enter() で plugin ごとにフォーカス処理
                    //String key = String.valueOf(tabbedPane.getSelectedIndex());
                    String key = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                    ChartDocument plugin = providers.get(key);
                    if (plugin != null) plugin.enter();
                }
//pns$
            }
        });

        // Frame の大きさをストレージからロードする
        int x = ClientContext.getInt("chart.frameX");
        int y = ClientContext.getInt("chart.frameY");
        int width = ClientContext.getInt("chart.frameWidth");
        int height = ClientContext.getInt("chart.frameHeight");
        Rectangle bounds = PreferencesUtils.getRectangle(prefs, PN_FRAME, new Rectangle(x, y, width, height));

        // フレームの表示位置を決める J2SE 5.0
        boolean locByPlatform = Project.getPreferences().getBoolean(Project.LOCATION_BY_PLATFORM, false);

        if (locByPlatform) {
            frame.setLocationByPlatform(true);
            frame.setSize(bounds.width, bounds.height);

        } else {
            frame.setLocationByPlatform(false);
            frame.setBounds(bounds);
        }

        // MML 送信 Queue
        if (Project.getSendMML()) {
            mmlListener = (MmlMessageListener) getContext().getPlugin("sendMml");
        }

        // CLAIM 送信 Queue
        if (Project.getSendClaim()) {
            claimListener = (ClaimMessageListener) getContext().getPlugin("sendClaim");
        }

        frame.setVisible(true);

        // ちらつき防止
        mainPanel.setVisible(false);
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run() {
                mainPanel.setVisible(true);
            }
        });

        // IME off
        IMEControl.setImeOff(getFrame());

//pns^  splitPane の左パネルを最小状態で開く
//        splitPane.setVisible(false); // divisionLocation が動く際のちらつきを防ぐ
//        EventQueue.invokeLater(new Runnable(){
//            public void run() {
//                //splitPane.putClientProperty("Quaqua.SplitPane.style", "bar");
//                splitPane.setDividerSize(6);
//                splitPane.setDividerLocation(inspector.getPanel().getMinimumSize().width);
//                splitPane.setVisible(true);
//            }
//        });
//pns$
    }

    /**
     * MML送信リスナを返す。
     * @return MML送信リスナ
     */
    public MmlMessageListener getMMLListener() {
        return mmlListener;
    }

    /**
     * CLAIM送信リスナを返す。
     * @return CLAIM送信リスナ
     */
    public ClaimMessageListener getCLAIMListener() {
        return claimListener;
    }

    /**
     * メニューを制御する。
     */
    public void controlMenu() {
        stateMgr.controlMenu();
    }

    /**
     * ドキュメントタブを生成する。
     */
//pns    private JTabbedPane loadDocuments() {
    private PNSTabbedPane loadDocuments() {

        providers = new HashMap<String, ChartDocument>();

        PNSTabbedPane tab = new PNSTabbedPane();
        tab.getButtonPanel().setPadding(new Dimension(0,4));
        tab.getButtonPanel().setBackgroundColor(Color.BLACK, 0.1f, 0.2f);
        tab.getButtonPanel().setBottomLineAlpha(0.4f);

        ChartDocument[] plugin = new ChartDocument[5];
        plugin[0] = new DocumentBridgeImpl();
        plugin[1] = new DiagnosisDocument();
        plugin[2] = new LaboTestBean();
        plugin[3] = new CareMapDocument();
        plugin[4] = new PatientInfoDocument();

        for (int index = 0; index < plugin.length; index++) {

            if (index == 0 ) {
//          if (index == 0 || index == 1) { // DiagnosisInspector を作ったので傷病名も先に読むようにした → 検索からオープンしたとき，getDocumentHistory が終わってないので lastVisit がとれない！
                plugin[index].setContext(this);
                plugin[index].start();
            }

            tab.addTab(plugin[index].getTitle(), plugin[index].getUI());
            // tab のタイトルをキーにする
            providers.put(plugin[index].getTitle(), plugin[index]);
        }

        // ゼロ番目を選択しておき changeListener を機能させる
        tab.setSelectedIndex(0);
        //
        // tab に プラグインを遅延生成するためのの ChangeListener を追加する
        //
        tab.addChangeListener(EventHandler.create(ChangeListener.class, this, "tabChanged", ""));

        // getDiagnosisDocument() に loadDocuments が終わったことを通知する
        synchronized(loadDocumentsDone) {
            loadDocumentsDone[0] = true;
            loadDocumentsDone.notify();
        }
        return tab;
    }

    /**
     * ドキュメントタブにプラグインを遅延生成し追加する。
     */
    public void tabChanged(ChangeEvent e) {

        //
        // 選択されたタブ番号に対応するプラグインをテーブルから検索する
        //
        int index = tabbedPane.getSelectedIndex();
//      String key = String.valueOf(index);
        String key = tabbedPane.getTitleAt(index);
        ChartDocument plugin = providers.get(key);

        if (plugin.getContext() == null) {
            //
            // まだ生成されていないプラグインを生成する
            //
            plugin.setContext(ChartImpl.this);
            plugin.start();
            tabbedPane.setComponentAt(index, plugin.getUI());
//pns   これだと，start() した後，enter() が呼ばれない
        } // else {
        //
        // 既に生成済みプラグインの場合は enter() をコールする
        //
        plugin.enter();
    }

    /**
     * DiangosisInspector に病名 drop できるようにする
     * DiagnosisInspector に DiagnosisDocument を教えるためのメソッド
     * DiagnosisDocument ができあがってからオブジェクトを返す
     * @return
     */
    public DiagnosisDocument getDiagnosisDocument() {
        // loadDocuments されて，providers がセットされるまで待つ
        synchronized(loadDocumentsDone) {
            if (!loadDocumentsDone[0]) {
                try{ loadDocumentsDone.wait();}
                catch (InterruptedException e){}
            }
        }

        DiagnosisDocument doc = (DiagnosisDocument) providers.get("傷病名");
        if (doc.getContext() == null) {
            doc.setContext(ChartImpl.this);
            doc.start();
        }
        return doc;
    }

    /**
     * 新規カルテを作成する。
     */
    public void newKarte() {

        logger.debug("newKarte() in ChartImpl starts");

        // ReadOnly なら開かない
        if (isReadOnly()) return;

        // 新規カルテ作成はひとつだけ　masuda
        if (toFrontNewKarteIfPresent()) return;
        // masuda

        String dept = getPatientVisit().getDeptNoTokenize();
        String deptCode = getPatientVisit().getDepartmentCode();
        String insuranceUid = getPatientVisit().getInsuranceUid();

        // 新規ドキュメントのタイプ=2号カルテと可能なオプションを設定する
        String docType = IInfoModel.DOCTYPE_KARTE;
        Chart.NewKarteOption option = null;
        KarteViewer base = null;

        //ChartDocument bridgeOrViewer = providers.get("0");
        ChartDocument bridgeOrViewer = providers.get("参 照");

        if (bridgeOrViewer instanceof DocumentBridgeImpl) {
            // Chart画面のタブパネル
            logger.debug("bridgeOrViewer instanceof DocumentBridgeImpl");
            DocumentBridgeImpl bridge = (DocumentBridgeImpl) bridgeOrViewer;
            base = bridge.getBaseKarte();

        } else if (bridgeOrViewer instanceof KarteDocumentViewer) {
            logger.debug("bridgeOrViewer instanceof KarteDocumentViewer");
            KarteDocumentViewer viwer = (KarteDocumentViewer) bridgeOrViewer;
            base = viwer.getBaseKarte();
        } else {
            return;
        }

        if (base != null) {
            logger.debug("base != null");
            if (base.getDocType().equals(IInfoModel.DOCTYPE_KARTE)) {
                logger.debug("base.getDocType().equals(IInfoModel.DOCTYPE_KARTE");
                option = Chart.NewKarteOption.BROWSER_COPY_NEW;
            } else {
                // ベースがあても２号カルテでない場合
                logger.debug("base.getDocType().equals(IInfoModel.DOCTYPE_S_KARTE");
                option = Chart.NewKarteOption.BROWSER_NEW;
            }

        } else {
            // ベースのカルテがない場合
            logger.debug("base == null");
            option = Chart.NewKarteOption.BROWSER_NEW;
        }

        //
        // 新規カルテ作成時に確認ダイアログを表示するかどうか
        //
        NewKarteParams params = null;

        if (prefs.getBoolean(Project.KARTE_SHOW_CONFIRM_AT_NEW, true)) {

            // 新規カルテダイアログへパラメータを渡し、コピー新規のオプションを制御する
            logger.debug("show newKarteDialog");
            params = getNewKarteParams(docType, option, null, dept, deptCode, insuranceUid);

        } else {
            // 保険、作成モード、配置方法を手動で設定する
            params = new NewKarteParams(option);
            params.setDocType(docType);
            params.setDepartment(dept);
            params.setDepartmentCode(deptCode);

            // 保険
            PVTHealthInsuranceModel[] ins = getHealthInsurances();
            params.setPVTHealthInsurance(ins[0]);
            if (insuranceUid != null) {
                for (int i = 0; i < ins.length; i++) {
                    if (ins[i].getGUID() != null) {
                        if (insuranceUid.equals(ins[i].getGUID())) {
                            params.setPVTHealthInsurance(ins[i]);
                            break;
                        }
                    }
                }
            }

            // 作成モード
            switch (option) {

                case BROWSER_NEW:
                    params.setCreateMode(Chart.NewKarteMode.EMPTY_NEW);
                    break;

                case BROWSER_COPY_NEW:
                    int cMode = prefs.getInt(Project.KARTE_CREATE_MODE, 0);
                    if (cMode == 0) {
                        params.setCreateMode(Chart.NewKarteMode.EMPTY_NEW);
                    } else if (cMode == 1) {
                        params.setCreateMode(Chart.NewKarteMode.APPLY_RP);
                    } else if (cMode == 2) {
                        params.setCreateMode(Chart.NewKarteMode.ALL_COPY);
                    }
                    break;
            }

            // 配置方法
            params.setOpenFrame(prefs.getBoolean(Project.KARTE_PLACE_MODE, true));

        }

        // キャンセルした場合はリターンする
        if (params == null) {
            return;
        }

        logger.debug("returned newKarteDialog");
        DocumentModel editModel = null;
        KarteEditor editor = null;

        // Baseになるカルテがあるかどうかでモデルの生成が異なる
        if (params.getCreateMode() == Chart.NewKarteMode.EMPTY_NEW) {
            logger.debug("empty new is selected");
            editModel = getKarteModelToEdit(params);
        } else {
            logger.debug("copy new is selected");
            editModel = getKarteModelToEdit(base.getModel(), params);
        }

//pns^ 新規カルテにいろいろモジュールを入れる
        StampTreeModules stm = new StampTreeModules(this);
        // 初診・再診 stampTreeModule を自動入力する
        ModuleModel mm = stm.getBaseCharge();
        if (mm != null) editModel.addModule(mm);

        // 初期テキストスタンプ挿入
        if (stm.isShoshin()) {
            mm = stm.getTextStamp("テンプレート（初診）");
            if (mm != null) editModel.addModule(mm);
        } else {
            mm = stm.getTextStamp("テンプレート（再診）");
            if (mm != null) editModel.addModule(mm);
        }
//pns$
        editor = createEditor();
        editor.setModel(editModel);
        editor.setEditable(true);
        editor.setMode(KarteEditor.DOUBLE_MODE);

        if (params.isOpenFrame()) {
            final EditorFrame editorFrame = new EditorFrame();
            editorFrame.setChart(this);
            editorFrame.setKarteEditor(editor);
            editorFrame.start();
//            SwingUtilities.invokeLater(new Runnable(){
//                @Override
//                public void run() {
//                    // Quaqua 6.5 になってから，こうしないと，editor がフォーカスを取っても，その後 ChartImpl に制御がうつって，
//                    // せっかく取った focus を ChartImpl に取り返されてしまうようになった // ← 不要になった
//                    editorFrame.start();
//                }
//            });
        } else {
            editor.setContext(this);
            editor.initialize();
            editor.start();
            this.addChartDocument(editor, params);
        }
    }

    /**
     * EmptyNew 新規カルテのモデルを生成する。
     * @param params 作成パラメータセット
     * @return 新規カルテのモデル
     */
    public DocumentModel getKarteModelToEdit(NewKarteParams params) {

        // カルテモデルを生成する
        DocumentModel model = new DocumentModel();

        //
        // DocInfoを設定する
        //
        DocInfoModel docInfo = model.getDocInfo();

        // docId 文書ID
        docInfo.setDocId(GUIDGenerator.generate(docInfo));

        // 生成目的
        docInfo.setPurpose(PURPOSE_RECORD);

        // DocumentType
        docInfo.setDocType(params.getDocType());

//        //
//        // 診療科を設定する
//        // 受付情報から得ている
//        //
//        String dept = params.getDepartment();
//        docInfo.setDepartmentDesc(dept); // department
//
//        // 診療科コード
//        // 受付からとっていない場合....
//        String deptCode = params.getDepartmentCode();
//        if (deptCode == null) {
//            docInfo.setDepartment(MMLTable.getDepartmentCode(dept)); // dept.code
//        }
        docInfo.setDepartmentDesc(getPatientVisit().getDeptNoTokenize());
        docInfo.setDepartment(getPatientVisit().getDepartmentCode());

        // 健康保険を設定する
        PVTHealthInsuranceModel insurance = params.getPVTHealthInsurance();
        docInfo.setHealthInsurance(insurance.getInsuranceClassCode());
        docInfo.setHealthInsuranceDesc(insurance.toString());
        docInfo.setHealthInsuranceGUID(insurance.getGUID());

        // Versionを設定する
        VersionModel version = new VersionModel();
        version.initialize();
        docInfo.setVersionNumber(version.getVersionNumber());

        //
        // Document の Status を設定する
        // 新規カルテの場合は none
        //
        docInfo.setStatus(STATUS_NONE);

        return model;
    }

    /**
     * コピーして新規カルテを生成する場合のカルテモデルを生成する。
     * @param oldModel コピー元のカルテモデル
     * @param params 生成パラメータセット
     * @return 新規カルテのモデル
     */
    public DocumentModel getKarteModelToEdit(DocumentModel oldModel, NewKarteParams params) {

        //
        // 新規モデルを作成し、表示されているモデルの内容をコピーする
        //
        DocumentModel newModel = new DocumentModel();
        boolean applyRp = params.getCreateMode() == Chart.NewKarteMode.APPLY_RP ? true : false;
        copyModel(oldModel, newModel, applyRp);

        //
        // 新規カルテの DocInfo を設定する
        //
        DocInfoModel docInfo = newModel.getDocInfo();

        // 文書ID
        docInfo.setDocId(GUIDGenerator.generate(docInfo));

        // 生成目的
        docInfo.setPurpose(PURPOSE_RECORD);

        // DocumentType
        docInfo.setDocType(params.getDocType());

        //
        // 診療科を設定する 受付情報から設定する
        //
        String dept = params.getDepartment();
        docInfo.setDepartmentDesc(dept);

//        // 診療科コード
//        // 受付からとっていない場合....
//        String deptCode = params.getDepartmentCode();
//        if (deptCode == null) {
//            docInfo.setDepartment(MMLTable.getDepartmentCode(dept)); // dept.code
//        }
        docInfo.setDepartmentDesc(getPatientVisit().getDeptNoTokenize());
        docInfo.setDepartment(getPatientVisit().getDepartmentCode());

        // 健康保険を設定する
        PVTHealthInsuranceModel insurance = params.getPVTHealthInsurance();
        docInfo.setHealthInsurance(insurance.getInsuranceClassCode());
        //docInfo.setHealthInsuranceDesc(insurance.getInsuranceClass());
        docInfo.setHealthInsuranceDesc(insurance.toString());
        docInfo.setHealthInsuranceGUID(insurance.getGUID());

        // Versionを設定する
        VersionModel version = new VersionModel();
        version.initialize();
        docInfo.setVersionNumber(version.getVersionNumber());

        //
        // Document の Status を設定する
        // 新規カルテの場合は none
        //
        docInfo.setStatus(STATUS_NONE);

        return newModel;
    }

    /**
     * 修正の場合のカルテモデルを生成する。
     * @param oldModel 修正対象のカルテモデル
     * @return 新しい版のカルテモデル
     */
    public DocumentModel getKarteModelToEdit(DocumentModel oldModel) {

        // 修正対象の DocInfo を取得する
        DocInfoModel oldDocInfo = oldModel.getDocInfo();

        // 新しい版のモデルにモジュールと画像をコピーする
        DocumentModel newModel = new DocumentModel();
        copyModel(oldModel, newModel, false);

        //
        // 新しい版の DocInfo を設定する
        //
        DocInfoModel newInfo = newModel.getDocInfo();

        // 文書ID
        newInfo.setDocId(GUIDGenerator.generate(newInfo));

        // 新しい版の firstConfirmDate = 元になる版の firstConfirmDate
        newInfo.setFirstConfirmDate(oldDocInfo.getFirstConfirmDate());

        // docType = old one
        newInfo.setDocType(oldDocInfo.getDocType());

        // purpose = old one
        newInfo.setPurpose(oldDocInfo.getPurpose());

        //
        // タイトルも引き継ぐ
        //
        newInfo.setTitle(oldDocInfo.getTitle());

        //
        // 診療科を設定する
        // 元になる版の情報を利用する
        //
        newInfo.setDepartmentDesc(oldDocInfo.getDepartmentDesc());
        newInfo.setDepartment(oldDocInfo.getDepartment());

        //
        // 健康保険を設定する
        // 元になる版の情報を利用する
        //
        newInfo.setHealthInsuranceDesc(oldDocInfo.getHealthInsuranceDesc());
        newInfo.setHealthInsurance(oldDocInfo.getHealthInsurance());
        newInfo.setHealthInsuranceGUID(oldDocInfo.getHealthInsuranceGUID());

        //
        // 親文書IDを設定する
        //
        newInfo.setParentId(oldDocInfo.getDocId());
        newInfo.setParentIdRelation(PARENT_OLD_EDITION);

        //
        // old PK を設定する
        //
        newInfo.setParentPk(oldModel.getId());

        //
        // Versionを設定する
        // new = old + 1.0
        VersionModel newVersion = new VersionModel();
        newVersion.setVersionNumber(oldDocInfo.getVersionNumber());
        newVersion.incrementNumber(); // version number ++
        newInfo.setVersionNumber(newVersion.getVersionNumber());

        //
        // Document Status を設定する
        // 元になる版の status (Final | Temporal | Modified)
        //
        newInfo.setStatus(oldDocInfo.getStatus());

        return newModel;
    }

    /**
     * カルテエディタを生成する。
     * @return カルテエディタ
     */
    public KarteEditor createEditor() {
        KarteEditor editor = null;
        try {
            editor = new KarteEditor();
            editor.addMMLListner(mmlListener);
            editor.addCLAIMListner(claimListener);
        } catch (TooManyListenersException e) {
            logger.warn(e);
            editor = null;
        }
        return editor;
    }

    //
    // モデルをコピーする
    // ToDO 参照ではいけない
    // DocInfo の設定はない
    //
    private void copyModel(DocumentModel oldModel, DocumentModel newModel, boolean applyRp) {

        //
        // 前回処方を適用する場合
        //
        if (applyRp) {
            Collection<ModuleModel> modules = oldModel.getModules();
            if (modules != null) {
                Collection<ModuleModel> apply = new ArrayList<ModuleModel>(5);

                for (ModuleModel bean : modules) {
                    IInfoModel model = bean.getModel();
                    if (model instanceof ClaimBundle) {
                        //
                        // 処方かどうかを判定する
                        //
                        if (((ClaimBundle) model).getClassCode().startsWith("2")) {
                            apply.add(bean);
                        }
//pns^                  ついでに，処置もコピー
                        if (((ClaimBundle) model).getClassCode().startsWith("4")) {
                            apply.add(bean);
                        }
//pns$
                    }
                }

                if (!apply.isEmpty()) {
                    newModel.setModules(apply);
                }
            }

        } else {
            // 全てコピー
            newModel.setModules(oldModel.getModules());
            newModel.setSchema(oldModel.getSchema());
        }
    }

    /**
     * カルテ作成時にダアイログをオープンし、保険を選択させる。
     *
     * @return NewKarteParams
     */
    public NewKarteParams getNewKarteParams(String docType, Chart.NewKarteOption option, MainFrame f, String dept, String deptCode, String insuranceUid) {

        NewKarteParams params = new NewKarteParams(option);
        params.setDocType(docType);
        params.setDepartment(dept);
        params.setDepartmentCode(deptCode);

        // 患者の健康保険コレクション
        Collection<PVTHealthInsuranceModel> insurances = pvt.getPatient().getPvtHealthInsurances();

        // コレクションが null の場合は自費保険を追加する
        if (insurances == null || insurances.isEmpty()) {
            insurances = new ArrayList<PVTHealthInsuranceModel>(1);
            PVTHealthInsuranceModel model = new PVTHealthInsuranceModel();
            model.setInsuranceClass(INSURANCE_SELF);
            model.setInsuranceClassCode(INSURANCE_SELF_CODE);
            model.setInsuranceClassCodeSys(INSURANCE_SYS);
            insurances.add(model);
        }

        // 保険コレクションを配列に変換し、パラメータにセットする
        // ユーザがこの中の保険を選択する
        PVTHealthInsuranceModel[] insModels = insurances.toArray(new PVTHealthInsuranceModel[insurances.size()]);
        params.setInsurances(insModels);
        int index = 0;
        if (insuranceUid != null) {
            for (int i = 0; i < insModels.length; i++) {
                if (insModels[i].getGUID() != null) {
                    if (insModels[i].getGUID().equals(insuranceUid)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        params.setInitialSelectedInsurance(index);

        String text = option == Chart.NewKarteOption.BROWSER_MODIFY
//pns           ? resMap.getString("modifyKarteTitle")
//pns           : resMap.getString("newKarteTitle");
                ? ClientContext.getString("chart.modifyKarteTitle")
                : ClientContext.getString("chart.newKarteTitle");

        text = ClientContext.getFrameTitle(text);

        // モーダルダイアログを表示する
        MainFrame frame = f != null ? f : getFrame();
        NewKarteDialog od = new NewKarteDialog(frame, text);
        od.setValue(params);
        od.start();

        // 戻り値をリターンする
        params = (NewKarteParams) od.getValue();
        return params;
    }

    /**
     * 患者の健康保険を返す。
     * @return 患者の健康保険配列
     */
    @Override
    public PVTHealthInsuranceModel[] getHealthInsurances() {

        // 患者の健康保険
        Collection<PVTHealthInsuranceModel> insurances = pvt.getPatient().getPvtHealthInsurances();

        if (insurances == null || insurances.isEmpty()) {
            insurances = new ArrayList<PVTHealthInsuranceModel>(1);
            PVTHealthInsuranceModel model = new PVTHealthInsuranceModel();
            model.setInsuranceClass(INSURANCE_SELF);
            model.setInsuranceClassCode(INSURANCE_SELF_CODE);
            model.setInsuranceClassCodeSys(INSURANCE_SYS);
            insurances.add(model);
        }

        return insurances.toArray(new PVTHealthInsuranceModel[insurances.size()]);
    }

    /**
     * 選択された保険を特定する
     * UUID が見つかったらそれを返す。なかったら最初に見つかった保険を返す。
     * @param uuid 選択された保険のUUID
     * @return 選択された保険
     */
    @Override
    public PVTHealthInsuranceModel getHealthInsuranceToApply(String uuid) {

        // 患者の健康保険
        Collection<PVTHealthInsuranceModel> insurances = pvt.getPatient().getPvtHealthInsurances();
        // insurance model がなければ null を返す
        if (uuid == null || insurances == null || insurances.isEmpty()) return null;

        PVTHealthInsuranceModel ret = null;

        for (PVTHealthInsuranceModel hm : insurances) {
            if (ret == null) ret = hm;

            if (uuid.equals(hm.getGUID())) {
                ret = hm;
                //System.out.println("CharImpl: found uuid to apply = " + uuid);
                break;
            }
        }

        return ret;
    }

    /**
     * タブにドキュメントを追加する。
     * @param doc 追加するドキュメント
     * @param params 追加するドキュメントの情報を保持する NewKarteParams
     */
    public void addChartDocument(ChartDocument doc, NewKarteParams params) {
        String title = null;
        if (params.getPVTHealthInsurance() != null) {
            title = getTabTitle(params.getDepartment(), params.getPVTHealthInsurance().getInsuranceClass());
        } else {
            title = getTabTitle(params.getDepartment(), null);
        }
        tabbedPane.addTab(title, doc.getUI());
        int index = tabbedPane.getTabCount() - 1;
        providers.put(String.valueOf(index), doc);
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * タブにドキュメントを追加する。
     * @param title タブタイトル
     */
    public void addChartDocument(ChartDocument doc, String title) {
        tabbedPane.addTab(title, doc.getUI());
        int index = tabbedPane.getTabCount() - 1;
        providers.put(String.valueOf(index), doc);
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * 新規カルテ用のタブタイトルを作成する
     * @param insurance 保険名
     * @return タブタイトル
     */
    public String getTabTitle(String dept, String insurance) {
        String[] depts = dept.split("\\s*,\\s*");
        StringBuilder buf = new StringBuilder();
//pns   buf.append(resMap.getString("newKarteTabTitle"));
        buf.append(ClientContext.getString("chart.newKarteTabTitle"));
        if (insurance != null) {
            buf.append("(");
            buf.append(depts[0]);
            buf.append("・");
            buf.append(insurance);
            buf.append(")");
        }
        return buf.toString();
    }

    /**
     * 新規文書作成で選択されたプラグインを起動する。
     *
     * @param pluginClass 起動するプラグインのクラス名
     */
    private void invokePlugin(String pluginClass) {

        try {
            NChartDocument doc = (NChartDocument) Class.forName(
                    pluginClass,
                    true,
                    ClientContext.getPluginClassLoader()).newInstance();

            if (doc instanceof KarteEditor) {
                String dept = getPatientVisit().getDeptNoTokenize();
                String deptCode = getPatientVisit().getDepartmentCode();
                String insuranceUid = getPatientVisit().getInsuranceUid();
                Chart.NewKarteOption option = Chart.NewKarteOption.BROWSER_NEW;
                String docType = IInfoModel.DOCTYPE_S_KARTE;
                NewKarteParams params = new NewKarteParams(option);
                params.setDocType(docType);
                params.setDepartment(dept);
                params.setDepartmentCode(deptCode);

                //
                // 保険
                //
                PVTHealthInsuranceModel[] ins = getHealthInsurances();
                params.setPVTHealthInsurance(ins[0]);
                if (insuranceUid != null) {
                    for (int i = 0; i < ins.length; i++) {
                        if (ins[i].getGUID() != null) {
                            if (insuranceUid.equals(ins[i].getGUID())) {
                                params.setPVTHealthInsurance(ins[i]);
                                break;
                            }
                        }
                    }
                }

                DocumentModel editModel = getKarteModelToEdit(params);
                KarteEditor editor = (KarteEditor) doc;
                editor.setModel(editModel);
                editor.setEditable(true);
                editor.setContext(this);
                editor.setMode(KarteEditor.SINGLE_MODE);
                editor.initialize();
                editor.start();
                this.addChartDocument(editor, params);

            } else {
                doc.setContext(this);
                doc.start();
                addChartDocument(doc, doc.getTitle());
            }

        } catch (ClassNotFoundException e) { System.out.println("ChartImpl.java: " + e);
        } catch (InstantiationException e) { System.out.println("ChartImpl.java: " + e);
        } catch (IllegalAccessException e) { System.out.println("ChartImpl.java: " + e); }
    }

    /**
     * カルテ以外の文書を作成する。
     */
//pns^  自分的には使わないので削除した
/*    public void newDocument() {

        // 拡張ポイント新規文書のプラグインをリストアップし、
        // リストで選択させる
        ArrayList<NameValuePair> documents = new ArrayList<NameValuePair>(3);
        PluginLister<NChartDocument> lister = PluginLister.list(NChartDocument.class, ClientContext.getPluginClassLoader());
        LinkedHashMap<String, String> nproviders = lister.getProviders();
        if (nproviders != null) {
            Iterator<String> iter = nproviders.keySet().iterator();
            while (iter.hasNext()) {
                String cmd = iter.next();
                String clsName = nproviders.get(cmd);
                NameValuePair pair = new NameValuePair(cmd, clsName);
                documents.add(pair);
                logger.debug(cmd + " = " + clsName);
            }
        }

        if (documents.size() == 0) {
            logger.debug("No plugins");
            return;
        }

        // docs アイコンをラベルへInjectionする
        JLabel newDocsLabel = new JLabel();
        newDocsLabel.setName("newDocsLabel");
//pns   resMap.injectComponent(newDocsLabel);
        newDocsLabel.setIcon(GUIConst.ICON_DOCUMENT_GENERIC_32);

        final JList docList = new JList(documents.toArray());
//pns   docList.setCellRenderer(new StripeRenderer());

        JPanel panel = new JPanel(new BorderLayout(7, 0));
        panel.add(newDocsLabel,BorderLayout.WEST);
        panel.add(docList, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(6,6,5,5));
        JPanel content = new JPanel(new BorderLayout());
        content.add(panel, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createTitledBorder("作成する文書"));

        final JButton okButton = new JButton("了解");
        final JButton cancelButton = new JButton("取消し");
        Object[] options = new Object[]{okButton, cancelButton};

        JOptionPane jop = new JOptionPane(
                content,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                okButton);

        final JDialog dialog = jop.createDialog(getFrame(), ClientContext.getFrameTitle("新規文書作成"));
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                docList.requestFocusInWindow();
            }
        });

        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
                NameValuePair pair = (NameValuePair) docList.getSelectedValue();
                String clsName = pair.getValue();
                invokePlugin(clsName);
            }
        });
        okButton.setEnabled(false);

        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        docList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = docList.getSelectedIndex();
                    if (index >= 0) {
                        okButton.setEnabled(true);
                    }
                }
            }
        });

        dialog.setVisible(true);
    }
 */
//pns$

    /**
     * 全てのドキュメントを保存する。
     * @param dirtyList 未保存ドキュメントのリスト
     */
    private void saveAll(java.util.List<UnsavedDocument> dirtyList) {

        if (dirtyList == null || dirtyList.isEmpty()) {
            return;
        }

        for (UnsavedDocument undoc : dirtyList) {
            if (undoc.isNeedSave()) {
                //ChartDocument doc = providers.get(String.valueOf(undoc.getIndex()));
                ChartDocument doc = providers.get(tabbedPane.getTitleAt(undoc.getIndex()));
                if (doc != null && doc.isDirty()) {
                    tabbedPane.setSelectedIndex(undoc.getIndex());
                    doc.save();
                }
            }
        }
    }

    /**
     * ドキュメントのなかにdirtyのものがあるかどうかを返す。
     * @return dirtyの時true
     */
    private java.util.List<UnsavedDocument> dirtyList() {
        java.util.List<UnsavedDocument> ret = null;
        int count = tabbedPane.getTabCount();
        for (int i = 0; i < count; i++) {
            //ChartDocument doc = providers.get(String.valueOf(i));
            ChartDocument doc = providers.get(tabbedPane.getTitleAt(i));
            if (doc != null && doc.isDirty()) {
                if (ret == null) {
                    ret = new ArrayList<UnsavedDocument>(3);
                }
                ret.add(new UnsavedDocument(i, doc));
            }
        }
        return ret;
    }

    /**
     * CloseBox がクリックされた時の処理を行う。
     */
    public void processWindowClosing() {
        close();
    }

    /**
     * チャートウインドウを閉じる。
     */
    @Override
    public void close() {
        logger.info("close-1: pvt id = " + this.getPatientVisit().getPatientId() + ", state = " + getPatientVisit().getState());

//pns^  この患者の EditorFrame が開いたままなら，閉じる努力をする。EditorFame 保存がキャンセルされたらあきらめる。
        java.util.List<Chart> editorFrames = new ArrayList<Chart>(EditorFrame.getAllEditorFrames());
        if (editorFrames.size() > 0) {
            String patientId = this.getKarte().getPatient().getPatientId();
            for (Chart chart : editorFrames) {
                String id = chart.getKarte().getPatient().getPatientId();
                if (patientId.equals(id)) {
                    chart.close();

                    if (EditorFrame.getAllEditorFrames().contains(chart)) {
                        // EditorFrame が消えていないと言うことは，キャンセルされたと言うこと。この場合，chart 終了もキャンセル。
                        // logger.info("ChartImpl#close : canceled");
                        return;
                    }
                }
            }
        }
//pns$
        //
        // 未保存ドキュメント（病名等）がある場合はダイアログを表示し
        // 保存するかどうかを確認する
        //
        java.util.List<UnsavedDocument> dirtyList = dirtyList();

        if (dirtyList != null && dirtyList.size() > 0) {
ここでは state = 3
//pns       String saveAll = resMap.getString("unsavedtask.saveText");     // 保存;
//pns       String discard = resMap.getString("unsavedtask.discardText");  // 破棄;
//pns       String question = resMap.getString("unsavedtask.question");    // 未保存のドキュメントがあります。保存しますか ?
//pns       String title = resMap.getString("unsavedtask.title");          // 未保存処理
            String saveAll = ClientContext.getString("unsavedtask.saveText");     // 保存;
            String discard = ClientContext.getString("unsavedtask.discardText");  // 破棄;
            String question = ClientContext.getString("unsavedtask.question");    // 未保存のドキュメントがあります。保存しますか ?
            String title = ClientContext.getString("unsavedtask.title");          // 未保存処理
            String cancelText = (String) UIManager.get("OptionPane.cancelButtonText");

            Object[] message = new Object[dirtyList.size() + 1];
            message[0] = (Object) question;
            int index = 1;
            for (UnsavedDocument doc : dirtyList) {
                message[index++] = doc.getCheckBox();
            }

//pns^      すでに JSheet が出ている場合は，toFront してリターン
            if (MyJSheet.isAlreadyShown(getFrame())) {
                getFrame().toFront();
                return;
            }
//pns       int option = JOptionPane.showOptionDialog(
            int option = MyJSheet.showOptionDialog(
                    getFrame(),
                    message,
                    ClientContext.getFrameTitle(title),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{saveAll, discard, cancelText},
                    saveAll);

            switch (option) {
                case 0:
                    // save
                    saveAll(dirtyList);
                    // DiagnosisDocument が isValidOutcome でなければ，close を中止する
                    if (!getDiagnosisDocument().isValidOutcome()) break;
                    stop();
                    break;

                case 1:
                    // discard
                    stop();
                    break;

                case 2:
                    // cancel
                    break;
            }
        ここで　state = 2 になってる
        } else {
            stop();
        }
    }

    @Override
    public void stop() {

        if (providers != null) {
            for (Iterator<String> iter = providers.keySet().iterator(); iter.hasNext();) {
                ChartDocument doc = providers.get(iter.next());
                if (doc != null) {
                    doc.stop();
                }
            }
            providers.clear();
        }
        mediator.dispose();
        inspector.dispose();

        PreferencesUtils.putRectangle(prefs, PN_FRAME, getFrame().getBounds());

//pns^  DBTask が残っていると Chart にアクセスしてくるので，dispose する前に thread を shutdown する
//      Mar., 2010　メモや診断がサーバにセーブされない事態が発生！　ここのせい？？
//      DatabaseExecutor.restart();
//pns$
        getFrame().setVisible(false); // ここで windowClosed が呼ばれる
        getFrame().setJMenuBar(null);
        getFrame().dispose();
    }

    private abstract class ChartState {

        public ChartState() {
        }

        public abstract void controlMenu();
    }

    /**
     * ReadOnly ユーザの State クラス。
     */
    private final class ReadOnlyState extends ChartState {

        public ReadOnlyState() {
        }

        /**
         * 新規カルテ作成及び修正メニューを disable にする。
         */
        @Override
        public void controlMenu() {
            mediator.getAction(GUIConst.ACTION_NEW_KARTE).setEnabled(false);
            mediator.getAction(GUIConst.ACTION_MODIFY_KARTE).setEnabled(false);
        }
    }

    /**
     * 保険証がない場合の State クラス。
     */
    private final class NoInsuranceState extends ChartState {

        public NoInsuranceState() {
        }

        @Override
        public void controlMenu() {
            mediator.getAction(GUIConst.ACTION_NEW_KARTE).setEnabled(false);
        }
    }

    /**
     * 通常の State クラス。
     */
    private final class OrdinalyState extends ChartState {

        public OrdinalyState() {
        }

        @Override
        public void controlMenu() {
            mediator.getAction(GUIConst.ACTION_NEW_KARTE).setEnabled(true);
        }
    }

    /**
     * State Manager クラス。
     */
    private final class StateMgr {

        private ChartState readOnlyState = new ReadOnlyState();
        private ChartState noInsuranceState = new NoInsuranceState();
        private ChartState ordinalyState = new OrdinalyState();
        private ChartState currentState;

        public StateMgr() {
            if (isReadOnly()) {
                enterReadOnlyState();
            } else {
                enterOrdinalyState();
            }
        }

        public void enterReadOnlyState() {
            currentState = readOnlyState;
            currentState.controlMenu();
        }

        public void enterNoInsuranceState() {
            currentState = noInsuranceState;
            currentState.controlMenu();
        }

        public void enterOrdinalyState() {
            currentState = ordinalyState;
            currentState.controlMenu();
        }

        public void controlMenu() {
            currentState.controlMenu();
        }
    }

    /**** Chart Instance を管理するための static クラス **/
    /**
     * オープンしている全インスタンスを保持するリストを返す。
     * @return オープンしている ChartPlugin のリスト
     */
    public static ArrayList<ChartImpl> getAllChart() {
        return allCharts;
    }

    /**
     * チャートステートの束縛リスナを登録する。
     * @param prop 束縛プロパティ名
     * @param l 束縛リスナ
     */
    public static void addPropertyChangeListener(String prop,
            PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     * チャートステートの束縛リスナを削除する。
     * @param prop 束縛プロパティ名
     * @param l 束縛リスナ
     */
    public static void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * チャートウインドウのオープンを通知する。
     * CLOSE -> OPEN に変換する操作，ReadOnly の場合は WatingList に通知しない
     * PatientSearchImpl で開いた場合など，pvt.id=0 の場合は WatingList に通知しない
     * @param opened オープンした Chart(=this)
     */
    public static void windowOpened(ChartImpl opened) {

        // インスタンスを保持するリストへ追加する
        allCharts.add(opened);

        PatientVisitModel model = opened.getPatientVisit();
        int oldState = model.getState();
        // pvt status を変更する（close -> open)
        int newState = KarteState.toOpenState(oldState);
        model.setState(newState);

        if (!opened.isReadOnly() && model.getId() != 0) {
            boundSupport.firePropertyChange(ChartImpl.CHART_STATE, null, model);
        }
        // このあと，pvt.state は karteBean 読み込みがあればサーバデータでリストアされる
    }

    /**
     * チャートウインドウのクローズを通知する。
     * OPEN -> CLOSE に変更する操作   ReadOnly のときも WatingList に通知する
     * PatientSearchImpl で開いた場合など，pvt.id=0 の場合は WatingList に通知しない
     * @param closed クローズした Chart(=this)
     */
    public static void windowClosed(ChartImpl closed) {

        // pvt status を変更する（open -> close)
        PatientVisitModel model = closed.getPatientVisit();
        if (closed.isReadOnly()) {
            model.setState(KarteState.READ_ONLY);
        } else {
            int oldState = model.getState();
            boolean isEmpty = new DocumentPeeker(model).isKarteEmpty();
            int newState = KarteState.toClosedState(oldState, isEmpty);
            model.setState(newState);
        }
        // WatingListImpl に通知する
        // 書き込み時の ReadOnly 対応は WatingList 側で施行
        if (model.getId() != 0) {
            boundSupport.firePropertyChange(ChartImpl.CHART_STATE, null, model);
        }

        // 最後にインスタンスリストから取り除く
        boolean succeeded = allCharts.remove(closed);
        if (!succeeded) {
            // カルテが登録されていなかったと言うことで，ありえない
            throw new RuntimeException("Chart is lost!");
        }
    }

    /**
     * チャート状態の変化を通知する。
     * @param 変化のあった ChartPlugin
     */
    public static void fireChanged(ChartImpl changed) {
        PatientVisitModel model = changed.getPatientVisit();
        model.setState(changed.getChartState());
    }

//pns^
    /**
     * 開いている全ての chart を閉じる
     */
    //public static void closeAll() {
    //    for (ChartImpl chart : allCharts) {
    //        chart.close();
    //    }
    //}

    /**
     * Document が modify用に既に開かれていたら toFront する
     * masuda 先生の docAlreadyOpened のパクリ
     * @param DocumentModel doc
     * @return toFront できたら true
     */
    public boolean toFrontDocumentIfPresent(DocumentModel baseDocumentModel) {
        if (baseDocumentModel == null || baseDocumentModel.getDocInfo() == null) return false;
        long baseDocPk = baseDocumentModel.getDocInfo().getDocPk();
        List<Chart> editorFrames = EditorFrame.getAllEditorFrames();
        if (!editorFrames.isEmpty()) {
            for (Chart chart : editorFrames) {
                EditorFrame frame = (EditorFrame) chart;
                long parentDocPk = frame.getParentDocPk();
                if (baseDocPk == parentDocPk) {
                    // parentPkが同じEditorFrameがある場合はFrameをtoFrontする
                    chart.getFrame().setExtendedState(java.awt.Frame.NORMAL);
                    chart.getFrame().toFront();
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * NewKarte が既に開かれていたら toFront する
     * masuda 先生の newKarteAlreadyOpened のパクリ
     * @return toFront できたら true
     */
    private boolean toFrontNewKarteIfPresent() {
        List<Chart> editorFrames = EditorFrame.getAllEditorFrames();
        if (!editorFrames.isEmpty()) {
            String patientId = this.getKarte().getPatient().getPatientId();
            for (Chart chart : editorFrames) {
                // 新規カルテだとDocInfoのstatusは"N"
                EditorFrame ef = (EditorFrame) chart;
                String status = ef.getDocInfoStatus();
                String id = chart.getKarte().getPatient().getPatientId();
                if (patientId.equals(id) && IInfoModel.STATUS_NONE.equals(status)) {
                    // 新規カルテのEditorFrameがある場合はFrameをtoFrontする
                    chart.getFrame().setExtendedState(Frame.NORMAL);
                    chart.getFrame().toFront();
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * カルテを前に出す
     * @param pvt
     */
    public static void toFront(PatientVisitModel pvt) {
        if (pvt == null) return;
        toFront(pvt.getPatient());
    }

    public static void toFront(PatientModel patient) {
        if (patient == null) return;
        long ptId = patient.getId();
        if (patient != null) {
            for (ChartImpl chart : allCharts) {
                if (chart.getPatient().getId() == ptId) {
                    chart.getFrame().toFront();
                    return;
                }
            }
        }
    }
    /**
     * カルテがあるかどうか調べる
     * @param pvt
     */
    public static boolean isKarteOpened(PatientVisitModel pvt) {
        if (pvt == null) return false;
        return isKarteOpened(pvt.getPatient());
    }

    public static boolean isKarteOpened(PatientModel patient) {
        if (patient == null) return false;
        long ptId = patient.getId();
        for (ChartImpl chart : allCharts) {
            if (chart.getPatient().getId() == ptId) {
                return true;
            }
        }
        return false;
    }
//pns$
}

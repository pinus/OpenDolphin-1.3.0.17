package open.dolphin.client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import open.dolphin.helper.WindowSupport;
import open.dolphin.infomodel.*;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.MainFrame;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;
import open.dolphin.util.PreferencesUtils;
import org.apache.log4j.Logger;

/**
 * EditorFrame
 *
 * @author Kazushi Minagawa
 */
public class EditorFrame extends AbstractMainTool implements Chart {

    // このクラスの２つのモード（状態）でメニューの制御に使用する
    public enum EditorMode {BROWSER, EDITOR};

    // 全インスタンスを保持するリスト
    private static List<Chart> allEditorFrames = new ArrayList<Chart>(3);

    // このフレームの実のコンテキストチャート
    private Chart realChart;

    // このフレームに表示する KarteView オブジェクト
    private KarteViewer view;

    // このフレームに表示する KarteEditor オブジェクト
    private KarteEditor editor;

    // ToolBar パネル
    private JPanel myToolPanel;

    // スクローラコンポーネント
    private JScrollPane scroller;

    // Status パネル
    private StatusPanel statusPanel;

    // このフレームの動作モード
    private EditorMode mode;

    // WindowSupport オブジェクト
    private WindowSupport windowSupport;

    // Mediator オブジェクト
    private ChartMediator mediator;

    // Block GlassPane
    private BlockGlass blockGlass;

    // 親チャートの位置
    private Point parentLoc;

    // KarteEditor からの PropertyChange を受け取るリスナ
    private PropertyChangeListener editorListener;

    // Preferences と property name
    private Preferences prefs;
    private static final String PN_FRAME = "editorFrame.frame";

    //private JPanel content;
    // Logger
    private Logger logger;

    // masuda
    public KarteEditor getEditor(){
        return editor;
    }

    private DocumentModel getDocumentModel() {
        DocumentModel docModel = null;
        if (mode == null){
            return null;
        }

        switch (mode) {
            case BROWSER:
                docModel = view.getModel();
                break;
            case EDITOR:
                docModel = editor.getModel();
                break;
        }
        return docModel;
    }

    public String getDocInfoStatus() {
        DocumentModel docModel = getDocumentModel();
        String ret = null;
        if (docModel != null){
            ret = docModel.getDocInfo().getStatus();
        }
        return ret;
    }

    public long getParentDocPk() {
        long pk = 0;

        DocumentModel docModel = getDocumentModel();
        if (docModel != null) {
            switch (mode) {
                case BROWSER:
                    pk = docModel.getDocInfo().getDocPk();
                    break;
                case EDITOR:
                    pk = docModel.getDocInfo().getParentPk();
                    break;
            }
        }
        return pk;
    }
    // masuda

    /**
     * 全インスタンスを保持するリストを返す。
     * @return 全インスタンスを保持するリスト
     */
    public static List<Chart> getAllEditorFrames() {
        return allEditorFrames;
    }

    private static PageFormat pageFormat = null;
    static {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if (printJob != null && pageFormat == null) {
            // set default format
            pageFormat = printJob.defaultPage();
        }
    }

    /**
     * EditorFrame オブジェクトを生成する。
     */
    public EditorFrame() {
//pns^
        logger = ClientContext.getBootLogger();
        prefs = Preferences.userNodeForPackage(this.getClass());
//pns$
        allEditorFrames.add(this);
    }

    /**
     * IChart コンテキストを設定する。
     * @param chartCtx IChart コンテキスト
     */
    public void setChart(Chart chartCtx) {
        this.realChart = chartCtx;
        parentLoc = realChart.getFrame().getLocation();
        super.setContext(chartCtx.getContext());
    }

    public Chart getChart() {
        return realChart;
    }

    /**
     * 表示する KarteViewer オブジェクトを設定する。
     * @param view 表示する KarteView
     */
    public void setKarteViewer(KarteViewer view) {
        this.view = view;
    }

    /**
     * 編集する KarteEditor オブジェクトを設定する。
     * @param editor 編集する KarteEditor
     */
    public void setKarteEditor(KarteEditor editor) {
        this.editor = editor;
    }

    /**
     * 患者モデルを返す。
     * @return 患者モデル
     */
    @Override
    public PatientModel getPatient() {
        return realChart.getPatient();
    }

    /**
     * 対象としている KarteBean オブジェクトを返す。
     * @return KarteBean オブジェクト
     */
    @Override
    public KarteBean getKarte() {
        return realChart.getKarte();
    }

    /**
     * 対象となる KarteBean オブジェクトを設定する。
     * @param karte KarteBean オブジェクト
     */
    @Override
    public void setKarte(KarteBean karte) {
        realChart.setKarte(karte);
    }

    /**
     * 来院情報を返す。
     * @return 来院情報
     */
    @Override
    public PatientVisitModel getPatientVisit() {
        return realChart.getPatientVisit();
    }

    /**
     * 来院情報を設定する。
     * @param model 来院情報モデル
     */
    @Override
    public void setPatientVisit(PatientVisitModel model) {
        realChart.setPatientVisit(model);
    }

    /**
     * Chart state を返す。
     * @return Chart の state 属性
     */
    @Override
    public int getChartState() {
        return realChart.getChartState();
    }

    /**
     * Chart state を設定する。
     * @param state Chart の state
     */
    @Override
    public void setChartState(int state) {
        realChart.setChartState(state);
    }

    /**
     * ReadOnly かどうかを返す。
     * @return readOnly の時 true
     */
    @Override
    public boolean isReadOnly() {
        return realChart.isReadOnly();
    }

    /**
     * ReadOnly 属性を設定する。
     * @param readOnly の時 true
     */
    @Override
    public void setReadOnly(boolean b) {
        realChart.setReadOnly(b);
    }

    /**
     * このオブジェクトの JFrame を返す。
     * @return JFrame オブジェクト
     */
    @Override
    public MainFrame getFrame() {
        return windowSupport.getFrame();
    }

    /**
     * StatusPanel を返す。
     * @return StatusPanel
     */
    @Override
    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }

    /**
     * StatusPanel を設定する。
     * @param statusPanel StatusPanel オブジェクト
     */
    @Override
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * ChartMediator を返す。
     * @return ChartMediator
     */
    @Override
    public ChartMediator getChartMediator() {
        return mediator;
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
     * DocumentHistory を返す。
     * @return DocumentHistory
     */
    @Override
    public DocumentHistory getDocumentHistory() {
        return realChart.getDocumentHistory();
    }

    /**
     * 引数のタブ番号にあるドキュメントを表示する。
     * @param index 表示するドキュメントのタブ番号
     */
    @Override
    public void showDocument(int index) {
        realChart.showDocument(index);
    }

    /**
     * dirty かどうかを返す。
     * @return dirty の時 true
     */
    @Override
    public boolean isDirty() {
        return (mode == EditorMode.EDITOR) ? editor.isDirty() : false;
    }

    @Override
    public PVTHealthInsuranceModel[] getHealthInsurances() {
        return realChart.getHealthInsurances();
    }

   @Override
    public PVTHealthInsuranceModel getHealthInsuranceToApply(String uuid) {
        return realChart.getHealthInsuranceToApply(uuid);
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        initialize();
    }

    /**
     * 初期化する。
     */
    private void initialize() {

        // Frame を生成する
        // Frame のタイトルを
        // 患者氏名(カナ) : 患者ID に設定する
        String title = String.format("%s - %s（%s）: %s",
                ClientContext.getString("editorFrame.karteStr"),
                getPatient().getFullName(),
                getPatient().getKanaName().replace("　", " "),
                getPatient().getPatientId());

        windowSupport = WindowSupport.create(title);
        JMenuBar myMenuBar = windowSupport.getMenuBar();

        MainFrame frame = windowSupport.getFrame();
        frame.setName("editorFrame");
        frame.removeStatusPanel();

        statusPanel = new StatusPanel(); // dummy 表示はしない　AbstractChartDocument から呼ばれるので
        statusPanel.add("", "message");

        MainFrame.MainPanel mainPanel = frame.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0,0));
        MainFrame.CommandPanel comPanel = frame.getCommandPanel();

        //content = new JPanel(new BorderLayout());

        //
        // Mediator が変更になる
        //
        mediator = new ChartMediator(this);

        //
        //  MenuBar を生成する
        //
        MenuFactory appMenu = new MenuFactory();
        appMenu.setMenuSupports(realChart.getContext().getMenuSupport(), mediator);
        appMenu.build(myMenuBar);
        mediator.registerActions(appMenu.getActionMap());
        myToolPanel = appMenu.getToolPanelProduct();
        //content.add(myToolPanel, BorderLayout.NORTH);
        myToolPanel.setOpaque(false);
        comPanel.add(myToolPanel);
        comPanel.setBottomLineAlpha(0.4f);

        //
        // このクラス固有のToolBarを生成する
        //
        ChartToolBar toolBar = new ChartToolBar(this);
        myToolPanel.add(toolBar);

        //statusPanel = new StatusPanel();

        if (view != null) {
            mode = EditorMode.BROWSER;
            view.setContext(EditorFrame.this);
            view.start();
            scroller = new JScrollPane(view.getUI());
            mediator.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);

        } else if (editor != null) {
            mode = EditorMode.EDITOR;
            editor.setContext(EditorFrame.this);
            editor.initialize();
            editor.start();
//pns^      scroller = new JScrollPane(editor.getUI());
            scroller = new MyJScrollPane(editor.getUI());
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//pns$
            mediator.enabledAction(GUIConst.ACTION_NEW_KARTE, false);
            mediator.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);

//pns       KarteEditor で save が完了したら通知される
            editorListener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    stop();
                }
            };
            editor.getBoundSupport().addPropertyChangeListener(KarteEditor.SAVE_DONE, editorListener);
        }
        //content.add(scroller, BorderLayout.CENTER);
        mainPanel.add(scroller, BorderLayout.CENTER);

        //frame.getContentPane().setLayout(new BorderLayout(0, 7));
        //frame.getContentPane().add(content, BorderLayout.CENTER);
//pns   frame.getContentPane().add((JPanel) statusPanel, BorderLayout.SOUTH);
//pns   resMap.injectComponents(frame);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                processWindowClosing();
            }
        });

        blockGlass = new BlockGlass();
        frame.setGlassPane(blockGlass);

        // Frame の大きさをストレージからロードする
        int x = ClientContext.getInt("editorFrame.frameX");
        int y = ClientContext.getInt("editorFrame.frameY");
        int width = ClientContext.getInt("editorFrame.frameWidth");
        int height = ClientContext.getInt("editorFrame.frameHeight");

        Rectangle bounds = PreferencesUtils.getRectangle(prefs, PN_FRAME, new Rectangle(x,y,width,height));
        frame.setBounds(bounds);
        windowSupport.getFrame().setVisible(true);

        Runnable awt = new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.getUI().scrollRectToVisible(new Rectangle(0,0,view.getUI().getWidth(), 50));
                } else if (editor != null) {
                    editor.getUI().scrollRectToVisible(new Rectangle(0,0,editor.getUI().getWidth(), 50));
                }
            }
        };
        EventQueue.invokeLater(awt);
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void stop() {
        mediator.dispose();
        allEditorFrames.remove(this);
        editor.getBoundSupport().removePropertyChangeListener(editorListener);

        Rectangle bounds = getFrame().getBounds();
        PreferencesUtils.putRectangle(prefs, PN_FRAME, bounds);

        getFrame().setVisible(false);
        getFrame().dispose();
//pns
        realChart.getFrame().toFront();
    }

    /**
     * ウインドウの close box が押された時の処理を実行する。
     */
    public void processWindowClosing() {
        close();
    }

    /**
     * ウインドウオープン時の処理を行う。
     */
    public void processWindowOpened() {
    }

    /**
     * Focus ゲインを得た時の処理を行う。
     */
    public void processGainedFocus() {

        switch (mode) {
            case BROWSER:
                if (view != null) {
                    view.enter();
                }
                break;

            case EDITOR:
                if (editor != null) {
                    editor.enter();
                }
                break;
        }
    }

    /**
     * コンテンツを KarteView から KarteEditor に切り替える。
     */
/*    private void replaceView() {
        if (editor != null) {
            // Editor Frame の時、
            // 新規カルテとドキュメントは不可とする
            mediator.enabledAction(GUIConst.ACTION_NEW_KARTE, false);
            mediator.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);
            mode = EditorMode.EDITOR;
            content.remove(scroller);
            scroller = new JScrollPane(editor.getUI());
            content.add(scroller, BorderLayout.CENTER);
            getFrame().validate();
        }
    }
*/
    /**
     * 新規カルテを作成する。
     */
/*    public void newKarte() {
//pns   EditorFrame から newKarte したときに呼ばれる
        System.out.println("EditorFrame#newKarte()");
        logger.debug("newKarte() in EditorFrame starts");

        // 新規カルテ作成ダイアログを表示しパラメータを得る
        String docType = view.getModel().getDocInfo().getDocType();

        final ChartImpl chart = (ChartImpl) realChart;
        String dept = chart.getPatientVisit().getDepartment();
        String deptCode = chart.getPatientVisit().getDepartmentCode();
        String insuranceUid = chart.getPatientVisit().getInsuranceUid();

        NewKarteParams params = null;
        Preferences prefs = Project.getPreferences();

        if (prefs.getBoolean(Project.KARTE_SHOW_CONFIRM_AT_NEW, true)) {

            params = chart.getNewKarteParams(docType,Chart.NewKarteOption.EDITOR_COPY_NEW, getFrame(), dept, deptCode, insuranceUid);

        } else {
            //
            // 手動でパラメータを設定する
            //
            params = new NewKarteParams(Chart.NewKarteOption.EDITOR_COPY_NEW);
            params.setDocType(docType);
            params.setDepartment(dept);
            params.setDepartmentCode(deptCode);

            PVTHealthInsuranceModel[] ins = chart.getHealthInsurances();
            params.setPVTHealthInsurance(ins[0]);

            int cMode = prefs.getInt(Project.KARTE_CREATE_MODE, 0);
            if (cMode == 0) {
                params.setCreateMode(Chart.NewKarteMode.EMPTY_NEW);
            } else if (cMode == 1) {
                params.setCreateMode(Chart.NewKarteMode.APPLY_RP);
            } else if (cMode == 2) {
                params.setCreateMode(Chart.NewKarteMode.ALL_COPY);
            }
        }

        if (params == null) {
            return;
        }

        // 編集用のモデルを得る
        DocumentModel editModel = null;

        if (params.getCreateMode() == Chart.NewKarteMode.EMPTY_NEW) {
            editModel = chart.getKarteModelToEdit(params);
        } else {
            editModel = chart.getKarteModelToEdit(view.getModel(), params);
        }

        final DocumentModel theModel = editModel;

        Runnable r = new Runnable() {

            public void run() {

                editor = chart.createEditor();
                editor.setModel(theModel);
                editor.setEditable(true);
                editor.setContext(EditorFrame.this);
                editor.setMode(KarteEditor.DOUBLE_MODE);

                Runnable awt = new Runnable() {
                    public void run() {
                        editor.initialize();
                        editor.start();
                        replaceView();
                    }
                };

                EventQueue.invokeLater(awt);
            }
        };

        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
*/
    /**
     * カルテを修正する。
     */
/*    public void modifyKarte() {
//pns   EditorFrame から　modifyKarte したときに呼ばれる。
        System.out.println("EditorFrame#modifyKarte()");
        logger.debug("modifyKarte() in EditorFrame starts");

        Runnable r = new Runnable() {

            public void run() {

                ChartImpl chart = (ChartImpl)realChart;
                DocumentModel editModel = chart.getKarteModelToEdit(view.getModel());
                editor = chart.createEditor();
                editor.setModel(editModel);
                editor.setEditable(true);
                editor.setContext(EditorFrame.this);
                editor.setModify(true);
                String docType = editModel.getDocInfo().getDocType();
                int mode = docType.equals(IInfoModel.DOCTYPE_KARTE) ? KarteEditor.DOUBLE_MODE : KarteEditor.SINGLE_MODE;
                editor.setMode(mode);

                Runnable awt = new Runnable() {
                    public void run() {
                        editor.initialize();
                        editor.start();
                        replaceView();
                    }
                };

                EventQueue.invokeLater(awt);
            }
        };

        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
*/
    private PageFormat getPageFormat() {
        return realChart.getContext().getPageFormat();
    }

    /**
     * 印刷する。
     */
    public void print() {

        switch (mode) {

            case BROWSER:
                if (view != null) {
                    view.printPanel2(getPageFormat());
                }
                break;

            case EDITOR:
                if (editor != null) {
                    editor.printPanel2(getPageFormat());
                }
                break;
        }
    }
//pns^
    /**
     * クローズする。
     * キャンセル，破棄の処理は editor でまとめてすることにした by pns
     */
    @Override
    public void close() {
        if (mode == EditorMode.EDITOR) {
            if (editor.isDirty()) editor.save();
            else stop();
        } else stop();
    }

    /**
     * PatientVisitModel の EditorFrame を前に出す
     * @param pvt
     */
    public static void toFront(PatientVisitModel pvt) {
        if (pvt == null) return;
        toFront(pvt.getPatient());
    }

    /**
     * PatientModel の EditorFrame を前に出す
     * @param patient
     */
    public static void toFront(PatientModel patient) {
        if (patient == null) return;
        long ptId = patient.getId();
        for (Chart chart : allEditorFrames) {
            long id = chart.getPatient().getId();
            if (ptId == id) {
                chart.getFrame().setExtendedState(java.awt.Frame.NORMAL);
                chart.getFrame().toFront();
                return;
            }
        }
    }
//pns$
}

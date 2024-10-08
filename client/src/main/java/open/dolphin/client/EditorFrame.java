package open.dolphin.client;

import open.dolphin.helper.WindowHolder;
import open.dolphin.helper.WindowSupport;
import open.dolphin.infomodel.*;
import open.dolphin.inspector.DocumentHistory;
import open.dolphin.ui.PNSFrame;
import open.dolphin.ui.PNSScrollPane;
import open.dolphin.ui.StatusPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

/**
 * EditorFrame.
 * ChartImpl と同じく Chart interface をもつ. ChartImpl は realChart にセットされる.
 * EditorFrame Object は JFrame ではなく，実 Frame は WindowSupport で作る.
 *
 * @author Kazushi Minagawa
 */
public class EditorFrame extends AbstractMainTool implements Chart, WindowListener {
    private final Logger logger;

    // このフレームの実のコンテキストチャート
    private Chart realChart;
    // このフレームに表示する KarteViewer オブジェクト
    private KarteViewer2 view;
    // このフレームに表示する KarteEditor オブジェクト
    private KarteEditor editor;
    // スクローラコンポーネント
    private JScrollPane scroller;
    // Status パネル
    private StatusPanel statusPanel;
    // このフレームの動作モード
    private EditorMode mode;
    // WindowSupport オブジェクト
    private WindowSupport<EditorFrame> windowSupport;
    // Mediator オブジェクト
    private ChartMediator mediator;
    // ChartToolBar
    private ChartToolBar chartToolBar;

    /**
     * EditorFrame オブジェクトを生成する.
     */
    public EditorFrame() {
        logger = LoggerFactory.getLogger(EditorFrame.class);
    }

    /**
     * PatientVisitModel の EditorFrame を前に出す
     *
     * @param pvt PatientVisitModel
     */
    public static void toFront(PatientVisitModel pvt) {
        if (pvt == null) {
            return;
        }
        toFront(pvt.getPatient());
    }

    /**
     * PatientModel の EditorFrame を前に出す
     *
     * @param patient PatientModel
     */
    public static void toFront(PatientModel patient) {
        if (Objects.nonNull(patient)) {
            WindowHolder.allEditorFrames().stream()
                .filter(chart -> Objects.nonNull(chart.getPatient()) && chart.getPatient().getId() == patient.getId())
                .findAny().ifPresent(chart -> chart.getFrame().toFront());
        }
    }

    /**
     * このフレームの KarteEditor を返す.
     *
     * @return KarteEditor
     */
    public KarteEditor getEditor() {
        return editor;
    }

    /**
     * このフレームの DocumentModel を返す.
     *
     * @return DocumentModel
     */
    private DocumentModel getDocumentModel() {
        DocumentModel docModel = null;
        if (mode == null) {
            return null;
        }

        switch (mode) {
            case BROWSER -> docModel = view.getDocument();
            case EDITOR -> docModel = editor.getDocument();
        }
        return docModel;
    }

    /**
     * このフレームの DocumentModel の DocInfo status を返す.
     * @return DocInfoModel の status
     */
    public String getDocInfoStatus() {
        DocumentModel docModel = getDocumentModel();
        String ret = null;
        if (docModel != null) {
            ret = docModel.getDocInfo().getStatus();
        }
        return ret;
    }

    /**
     * このフレームの DocumentModel の primary key を返す.
     * @return primary key
     */
    public long getParentDocPk() {
        long pk = 0;

        DocumentModel docModel = getDocumentModel();
        if (docModel != null) {
            switch (mode) {
                case BROWSER -> pk = docModel.getDocInfo().getDocPk();
                case EDITOR -> pk = docModel.getDocInfo().getParentPk();
            }
        }
        return pk;
    }

    /**
     * このフレームの元になった ChartImpl を返す.
     *
     * @return ChartImpl
     */
    public Chart getChart() {
        return realChart;
    }

    /**
     * このフレームの元の ChartImpl コンテキストを設定する.
     *
     * @param chartCtx ChartImpl コンテキスト
     */
    public void setChart(Chart chartCtx) {
        this.realChart = chartCtx;
        setContext(chartCtx.getContext());
    }

    /**
     * 表示する KarteViewer オブジェクトを設定する.
     *
     * @param view 表示する KarteView
     */
    public void setKarteViewer(KarteViewer2 view) {
        this.view = view;
    }

    /**
     * 編集する KarteEditor オブジェクトを設定する.
     *
     * @param editor 編集する KarteEditor
     */
    public void setKarteEditor(KarteEditor editor) {
        this.editor = editor;
    }

    /**
     * 患者モデルを返す.
     *
     * @return 患者モデル
     */
    @Override
    public PatientModel getPatient() {
        return realChart.getPatient();
    }

    /**
     * 対象としている KarteBean オブジェクトを返す.
     *
     * @return KarteBean オブジェクト
     */
    @Override
    public KarteBean getKarte() {
        return realChart.getKarte();
    }

    /**
     * 対象となる KarteBean オブジェクトを設定する.
     *
     * @param karte KarteBean オブジェクト
     */
    @Override
    public void setKarte(KarteBean karte) {
        realChart.setKarte(karte);
    }

    /**
     * 来院情報を返す.
     *
     * @return 来院情報
     */
    @Override
    public PatientVisitModel getPatientVisit() {
        return realChart.getPatientVisit();
    }

    /**
     * 来院情報を設定する.
     *
     * @param model 来院情報モデル
     */
    @Override
    public void setPatientVisit(PatientVisitModel model) {
        realChart.setPatientVisit(model);
    }

    /**
     * Chart state を返す.
     *
     * @return Chart の state 属性
     */
    @Override
    public int getChartState() {
        return realChart.getChartState();
    }

    /**
     * Chart state を設定する.
     *
     * @param state Chart の state
     */
    @Override
    public void setChartState(int state) {
        realChart.setChartState(state);
    }

    /**
     * ReadOnly かどうかを返す.
     *
     * @return readOnly の時 true
     */
    @Override
    public boolean isReadOnly() {
        return realChart.isReadOnly();
    }

    /**
     * ReadOnly 属性を設定する.
     *
     * @param b readonly=true
     */
    @Override
    public void setReadOnly(boolean b) {
        realChart.setReadOnly(b);
    }

    /**
     * このオブジェクトの JFrame を返す.
     *
     * @return JFrame オブジェクト
     */
    @Override
    public PNSFrame getFrame() {
        return windowSupport.getFrame();
    }

    /**
     * StatusPanel を返す.
     *
     * @return StatusPanel
     */
    @Override
    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }

    /**
     * StatusPanel を設定する.
     *
     * @param statusPanel StatusPanel オブジェクト
     */
    @Override
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * ChartMediator を返す.
     *
     * @return ChartMediator
     */
    @Override
    public ChartMediator getChartMediator() {
        return mediator;
    }

    /**
     * Menu アクションを制御する.
     *
     * @param name   action name to enable
     * @param enable enable
     */
    @Override
    public void enabledAction(String name, boolean enable) {
        Action action = mediator.getAction(name);
        if (action != null) {
            action.setEnabled(enable);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * DocumentHistory を返す.
     *
     * @return DocumentHistory
     */
    @Override
    public DocumentHistory getDocumentHistory() {
        return realChart.getDocumentHistory();
    }

    /**
     * 引数のタブ番号にあるドキュメントを表示する.
     *
     * @param index 表示するドキュメントのタブ番号
     */
    @Override
    public void showDocument(int index) {
        realChart.showDocument(index);
    }

    /**
     * dirty かどうかを返す.
     *
     * @return EDITOR モードでかつ dirty の時 true
     */
    @Override
    public boolean isDirty() {
        return (mode == EditorMode.EDITOR) && editor.isDirty();
    }

    /**
     * 全部の PVTHealthInsuranceModel を返す.
     *
     * @return array of PVTHealthInsuranceModel
     */
    @Override
    public PVTHealthInsuranceModel[] getHealthInsurances() {
        return realChart.getHealthInsurances();
    }

    /**
     * UID に一致する PVTHealthInsuranceModel を返す.
     *
     * @return matched PVTHealthInsuranceModel
     */
    @Override
    public PVTHealthInsuranceModel getHealthInsuranceToApply(String uuid) {
        return realChart.getHealthInsuranceToApply(uuid);
    }

    /**
     * ToolBar を返す.
     *
     * @return ChartToolBar
     */
    public ChartToolBar getChartToolBar() {
        return chartToolBar;
    }

    /**
     * プログラムを開始する.
     */
    @Override
    public void start() {
        initialize();
    }

    /**
     * 初期化する.
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

        windowSupport = new WindowSupport<>(title, this);
        JMenuBar myMenuBar = windowSupport.getMenuBar();

        PNSFrame frame = getFrame();
        frame.setName("editorFrame");
        frame.getRootPane().putClientProperty(WindowSupport.MENUBAR_HEIGHT_OFFSET_PROP, 30);

        // FocusTraversalPolicy
        // Stamp を cut したときに，余計なフォーカス移動が起こらないようにする
        frame.setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getDefaultComponent(Container aContainer) {
                // 最初にフォーカスを取る component
                return editor.getSOAPane().getTextPane();
            }

            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                // 余計な focus 移動を行わない
                return aComponent;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                // 余計な focus 移動を行わない
                return aComponent;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return null;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return null;
            }
        });

        frame.removeStatusPanel();

        statusPanel = new StatusPanel(); // dummy 表示はしない　AbstractChartDocument から呼ばれるので
        statusPanel.add("", "message");

        PNSFrame.MainPanel mainPanel = frame.getMainPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        PNSFrame.CommandPanel comPanel = frame.getCommandPanel();

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

        //statusPanel = new StatusPanel();

        if (view != null) {
            mode = EditorMode.BROWSER;
            view.setContext(EditorFrame.this);
            view.start();
            scroller = new JScrollPane(view.getUI());
            mediator.enableAction(GUIConst.ACTION_NEW_DOCUMENT, false);

        } else if (editor != null) {
            mode = EditorMode.EDITOR;
            editor.setContext(EditorFrame.this);
            editor.initialize();
            editor.start();
            scroller = new PNSScrollPane(editor.getUI());
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            mediator.enableAction(GUIConst.ACTION_NEW_KARTE, false);
            mediator.enableAction(GUIConst.ACTION_NEW_DOCUMENT, false);

            // KarteEditor で save が完了したら通知される
            editor.addFinishListener(this::stop);
        }
        mainPanel.add(scroller, BorderLayout.CENTER);

        //
        // active window がリストの最初に来るように制御する
        //
        frame.addWindowListener(this);
        frame.setVisible(true);

        // 先頭を表示
        SwingUtilities.invokeLater(() -> {
            if (view != null) {
                view.getUI().scrollRectToVisible(new Rectangle(0, 0, view.getUI().getWidth(), 50));
            } else if (editor != null) {
                editor.getUI().scrollRectToVisible(new Rectangle(0, 0, editor.getUI().getWidth(), 50));
            }
        });

        //
        // このクラス固有のToolBarを生成する
        //
        chartToolBar = new ChartToolBar(this);
        comPanel.add(chartToolBar);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        // window のクローズボタンを押したときの対応
        logger.info("windowClosing");
        closeFrame();
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {
        // 順番処理，新しいものをトップに置く
        WindowHolder.toTop(windowSupport);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {}

    /**
     * プログラムを終了する.
     */
    @Override
    public void stop() {
        realChart.getFrame().toFront();
        realChart.enter();
        getFrame().removeWindowListener(this);
        mediator.dispose();
        editor.stop();
        windowSupport.dispose();
        realChart = null;
        setContext(null);
    }

    /**
     * クローズする.
     * キャンセル，破棄の処理は editor でまとめてすることにした.
     */
    private void closeFrame() {
        if (mode == EditorMode.EDITOR && editor.isDirty()) {
            // save が成功すると editor から stop() が呼ばれる. 失敗すると呼ばれない.
            editor.save();
        } else {
            stop();
        }
    }

    /**
     * メニューの「閉じる」で呼ばれる.
     */
    @Override
    public void close() {
        closeFrame();
    }

    // このクラスの２つのモード（状態）でメニューの制御に使用する
    public enum EditorMode {
        BROWSER, EDITOR
    }
}

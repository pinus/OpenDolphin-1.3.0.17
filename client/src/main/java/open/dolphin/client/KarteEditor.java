package open.dolphin.client;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import open.dolphin.dao.OrcaEntry;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.event.CompletionListener;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.*;
import open.dolphin.message.MMLHelper;
import open.dolphin.orcaapi.OrcaApi;
import open.dolphin.project.Project;
import open.dolphin.ui.PNSBorderFactory;
import open.dolphin.util.MMLDate;
import open.dolphin.util.ModelUtils;
import open.dolphin.helper.StringTool;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 2号カルテクラス.
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class KarteEditor extends AbstractChartDocument implements IInfoModel {
    private static final long serialVersionUID = 1L;
    // シングルモード
    public static final int SINGLE_MODE = 1;
    // ２号カルテモード
    public static final int DOUBLE_MODE = 2;
    // TimeStamp のカラー
    private static final Color TIMESTAMP_FORE = Color.BLUE;
    private static final int TIMESTAMP_FONT_SIZE = 12;
    private static final Font TIMESTAMP_FONT = new Font("Dialog", Font.PLAIN, TIMESTAMP_FONT_SIZE);
    private static final String DEFAULT_TITLE = "経過記録";

    // このエディタのモード
    private int mode = DOUBLE_MODE;
    // このエディタのモデル
    private DocumentModel model;
    // このエディタを構成するコンポーネント
    private JLabel timeStampLabel;
    // Timestamp
    private String timeStamp;
    // 健康保険Box
    private boolean insuranceVisible;
    // SOA Pane
    private KartePane soaPane;
    // P Pane
    private KartePane pPane;
    // 2号カルテ JPanel
    private PrintablePanel panel2;
    // タイムスタンプの foreground
    private final Color timeStampFore = TIMESTAMP_FORE;
    // タイムスタンプフォント
    private final Font timeStampFont = TIMESTAMP_FONT;
    // 編集可能かどうかのフラグ
    // このフラグで KartePane を初期化する
    private boolean editable;
    // 修正時に true
    private boolean modify;
    // MML送信リスナ
    private MmlMessageListener mmlListener;
    // MML送信フラグ
    private boolean sendMml;
    // CLAIM 送信フラグ
    private boolean sendClaim;
    // State Manager
    private StateMgr stateMgr;
    // ClaimSender
    private final ClaimSender claimSender = new ClaimSender();
    // EditorFrame に save 完了を知らせる
    private CompletionListener competionListener;
    // KarteEditor ノードの Preferences
    private Preferences prefs;
    // 一時保存
    private Autosave autosave;
    // dirty フラグ
    private boolean dirty;

    private final Logger logger = ClientContext.getBootLogger();

    public KarteEditor() {
        init();
    }

    private void init() {
        setTitle(DEFAULT_TITLE);
        prefs = Preferences.userNodeForPackage(KarteEditor.class);
        autosave = new Autosave(this);
    }

    /**
     * エディタ終了を知らせるリスナを登録.
     * EditorFrame に知らせる.
     * @param listener CompletionListener
     */
    public void addFinishListener(CompletionListener listener) {
        competionListener = listener;
    }

    public void selectAll() {
        //KarteEditor.getInputMap().remove(KeyStroke.getKeyStroke('A',java.awt.event.InputEvent.META_MASK));
        System.out.println("---- selectAll in KarteEditor.java ----");//TODO
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * DocumentModelを返す.
     * @return DocumentModel
     */
    public DocumentModel getModel() {
        return model;
    }

    /**
     * DocumentModelを設定する.
     * @param model DocumentModel
     */
    public void setModel(DocumentModel model) {
        this.model = model;
    }

    public int getActualHeight() {
        try {
            JTextPane pane = soaPane.getTextPane();
            int pos = pane.getDocument().getLength();
            Rectangle r = pane.modelToView(pos);
            int hsoa = r.y;

            if (pPane == null) {
                return hsoa;
            }

            pane = pPane.getTextPane();
            pos = pane.getDocument().getLength();
            r = pane.modelToView(pos);
            int hp = r.y;

            return Math.max(hsoa, hp);

        } catch (BadLocationException ex) {
            ex.printStackTrace(System.err);
        }
        return 0;
    }

    public void printPanel2(PageFormat format) {
        String name = getContext().getPatient().getFullName();
        panel2.printPanel(format, 1, true, name, getActualHeight()+30);
    }

    public void printPanel2(PageFormat format, int copies, boolean useDialog) {
        String name = getContext().getPatient().getFullName();
        panel2.printPanel(format, copies, useDialog, name, getActualHeight()+30);
    }

    @Override
    public void print() {
        PageFormat pageFormat = getContext().getContext().getPageFormat();
        this.printPanel2(pageFormat);
    }

    public void insertImage() {
        JFileChooser chooser = new JFileChooser();
        int selected = chooser.showOpenDialog(getContext().getFrame());
        if (selected == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            this.getSOAPane().insertImage(path);

        }
    }

    /**
     * SOAPaneを返す.
     * @return SOAPane
     */
    protected KartePane getSOAPane() {
        return soaPane;
    }

    /**
     * PPaneを返す.
     * @return PPane
     */
    public KartePane getPPane() {
        return pPane;
    }

    /**
     * 編集可能属性を設定する.
     * @param b 編集可能な時true
     */
    protected void setEditable(boolean b) {
        editable = b;
    }

    /**
     * MMLリスナを追加する.
     * @param listener MMLリスナリスナ
     * @throws TooManyListenersException Exception
     */
    public void addMMLListner(MmlMessageListener listener) throws TooManyListenersException {
        if (mmlListener != null) {
            throw new TooManyListenersException();
        }
        mmlListener = listener;
    }

    /**
     * MMLリスナを削除する.
     * @param listener MMLリスナリスナ
     */
    public void removeMMLListener(MmlMessageListener listener) {
        if (mmlListener != null && mmlListener == listener) {
            mmlListener = null;
        }
    }

    /**
     * CLAIMリスナを追加する.
     * @param listener CLAIMリスナ
     * @throws TooManyListenersException Exception
     */
    public void addCLAIMListner(ClaimMessageListener listener) throws TooManyListenersException {
        claimSender.addCLAIMListener(listener);
    }

    /**
     * CLAIMリスナを削除する.
     * @param listener 削除するCLAIMリスナ
     */
    public void removeCLAIMListener(ClaimMessageListener listener) {
        claimSender.removeCLAIMListener(listener);
    }

    /**
     * 修正属性を設定する.
     * @param b 修正する時true
     */
    protected void setModify(boolean b) {
        modify = b;
    }

    @Override
    public void enter() {
        super.enter();
        stateMgr.controlMenu();
        getContext().enabledAction(GUIConst.ACTION_SEND_CLAIM, true);

    }

    /**
     * KartePane から dirty の通知を受ける.
     * @param newDirty セットする dirty
     */
    @Override
    public void setDirty(boolean newDirty) {
        if (dirty ^ newDirty) {
            dirty = newDirty;
            stateMgr.setDirty(newDirty);
        }
        // autosave に dirty 情報を流す.
        autosave.setDirty(newDirty);
    }

    @Override
    public boolean isDirty() {
        return stateMgr.isDirty();
    }

    /**
     * 初期化する.
     */
    public void initialize() {

        if (getMode() == SINGLE_MODE) {
            initialize1();
        } else if (getMode() == DOUBLE_MODE) {
            initialize2();
        }
    }

    /**
     * シングルモードで初期化する.
     */
    private void initialize1() {

        stateMgr = new StateMgr();

        KartePanel1 kp1 = new KartePanel1();
        panel2 = kp1;

        // TimeStampLabel を生成する
        timeStampLabel = kp1.getTimeStampLabel();
        timeStampLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeStampLabel.setForeground(timeStampFore);
        timeStampLabel.setFont(timeStampFont);

        kp1.getTimeStampPanel().setBorder(PNSBorderFactory.createTitleBarBorder(new Insets(0,0,0,0)));

        // SOA Pane を生成する
        soaPane = new KartePane();
        soaPane.setTextPane(kp1.getSoaTextPane());
        soaPane.setParent(this);
        soaPane.setRole(ROLE_SOA);
        soaPane.getTextPane().setTransferHandler(new SOATransferHandler(soaPane));
        if (model != null) {
            // Schema 画像にファイル名を付けるのために必要
            String docId = model.getDocInfo().getDocId();
            soaPane.setDocId(docId);
        }

        JScrollPane scroller = new JScrollPane(kp1);
        getUI().setLayout(new BorderLayout());
        getUI().add(scroller, BorderLayout.CENTER);

        // 初期化の前にモデルがセットしてある.
        // Model を表示する
        displayModel();
    }

    /**
     * 2号カルテモードで初期化する.
     */
    private void initialize2() {

        stateMgr = new StateMgr();

        KartePanel kp2 = KartePanelFactory.createEditorPanel();
        panel2 = kp2;

        // TimeStampLabel を生成する
        timeStampLabel = kp2.getTimeStampLabel();
        timeStampLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeStampLabel.setForeground(timeStampFore);
        timeStampLabel.setFont(timeStampFont);

        kp2.getTimeStampPanel().setBorder(PNSBorderFactory.createTitleBarBorder(new Insets(0,0,0,0)));

        // SOA Pane を生成する
        soaPane = new KartePane();
        soaPane.setTextPane(kp2.getSoaTextPane());
        soaPane.setParent(this);
        soaPane.setRole(ROLE_SOA);
        soaPane.getTextPane().setTransferHandler(new SOATransferHandler(soaPane));
        if (model != null) {
            // Schema 画像にファイル名を付けるのために必要
            String docId = model.getDocInfo().getDocId();
            soaPane.setDocId(docId);
        }

        // P Pane を生成する
        pPane = new KartePane();
        pPane.setTextPane(kp2.getPTextPane());
        pPane.setParent(this);
        pPane.setRole(ROLE_P);
        pPane.getTextPane().setTransferHandler(new PTransferHandler(pPane));

        setUI(kp2);

        // 初期化の前にモデルがセットしてある.
        // Model を表示する
        displayModel();
    }

    @Override
    public void start() {
        if (getMode() == SINGLE_MODE) {
            start1();
        } else if (getMode() == DOUBLE_MODE) {
            start2();
        }
        // 自動一時保存スタート
        autosave.start();
        logger.info("autosave start");
    }

    /**
     * KarteEditor の終了処理. EditorFrame から呼ばれる.
     */
    @Override
    public void stop() {
        logger.info("autosave stop");
        autosave.stop();

        soaPane.clear();
        if (getMode() == DOUBLE_MODE) { pPane.clear(); }
    }

    /**
     * シングルモードを開始する. 初期化の後コールされる.
     */
    private void start1() {
        // モデル表示後にリスナ等を設定する
        ChartMediator mediator = getContext().getChartMediator();
        soaPane.init(editable, mediator);
        enter();
    }

    /**
     * ２号カルテモードを開始する. 初期化の後コールされる.
     */
    private void start2() {
        // モデル表示後にリスナ等を設定する
        ChartMediator mediator = getContext().getChartMediator();
        soaPane.init(editable, mediator);
        pPane.init(editable, mediator);

        SwingUtilities.invokeLater(() -> {
            // キャレットを先頭にリセット.
            getSOAPane().getTextPane().setCaretPosition(0);
            getPPane().getTextPane().setCaretPosition(0);
        });
        enter();
    }

    /**
     * DocumentModelを表示する.
     */
    private void displayModel() {

        // Timestamp を表示する
        String now = ModelUtils.getDateAsFormatString(new Date(), IInfoModel.KARTE_DATE_FORMAT);

        if (modify) {
            String firstConfirm  = ModelUtils.getDateAsFormatString(model.getDocInfo().getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
            timeStamp = String.format("%s%s [%s]", UPDATE_MARK, now, firstConfirm);
        } else {
            timeStamp = now;
        }

        // 内容を表示する
        if (model.getModules() != null) {
            KarteRenderer_2 renderer = new KarteRenderer_2(soaPane, pPane);
            renderer.render(model);
            soaPane.setLogicalStyle("default");
            pPane.setLogicalStyle("default");
        }

        // 健康保険を表示する
        PVTHealthInsuranceModel[] ins = null;

        // コンテキストが EditotFrame の場合と Chart の場合がある
        if (getContext() instanceof ChartImpl) {
            ins = getContext().getHealthInsurances();
        } else if (getContext() instanceof EditorFrame) {
            EditorFrame ef = (EditorFrame) getContext();
            ChartImpl chart = (ChartImpl) ef.getChart();
            ins = chart.getHealthInsurances();
        }

        // Model に設定してある健康保険を選択する
        String selecteIns = Arrays.stream(ins)
                .filter(i -> Objects.nonNull(i.getGUID()))
                .filter(i -> i.getGUID().equals(getModel().getDocInfo().getHealthInsuranceGUID()))
                .findAny().map(PVTHealthInsuranceModel::toString).orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append(timeStamp);
        if (selecteIns != null) {
            sb.append(" (");
            sb.append(selecteIns.trim());
            sb.append(")");
        }

        timeStampLabel.setText(sb.toString());
        timeStampLabel.addMouseListener(new PopupListener());

        insuranceVisible = true;
    }

    public void setInsuranceVisible(Boolean b) {

        boolean old = insuranceVisible;

        if (old != b) {

            insuranceVisible = b;

            StringBuilder sb = new StringBuilder();
            sb.append(timeStamp);

            if (b) {
                sb.append(" (");
                sb.append(getModel().getDocInfo().getHealthInsuranceDesc().trim());
                sb.append(")");
            }

            timeStampLabel.setText(sb.toString());
            timeStampLabel.revalidate();
        }
    }

    public boolean isInsuranceVisible() {
        return insuranceVisible;
    }

    private class PopupListener extends MouseAdapter {

        public PopupListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1) {
                boolean b = isInsuranceVisible();
                setInsuranceVisible(b);
            }
            e.consume();
        }
    }

    /**
     * 保存ダイアログを表示し保存時のパラメータを取得する.
     * @param joinAreaNetwork sendMML MML送信フラグ 送信するとき true
     * @return SaveParams
     */
    private SaveParams getSaveParams(boolean joinAreaNetwork) {

        // Title が設定されているか
        String text = model.getDocInfo().getTitle();
        if (text == null || text.equals("")) {

            if (Project.getPreferences().getBoolean("useTop15AsTitle", true)) {
                // SOAPane から最初の１５文字を文書タイトルとして取得する
                text = soaPane.getTitle();
            } else {
                text = Project.getPreferences().get("defaultKarteTitle", DEFAULT_TITLE);
            }

            if ((text == null) || text.equals("")) {
                text = DEFAULT_TITLE;
            }
        }

        SaveParams params;

        //
        // 新規カルテで保存の場合
        // 仮保存から修正がかかっている場合
        // 修正の場合
        //
        DocInfoModel docInfo = getModel().getDocInfo();

        if (!modify && docInfo.getStatus().equals(IInfoModel.STATUS_NONE)) {
            logger.debug("saveFromNew");
            if (sendClaim) {
                sendClaim = Project.getSendClaimSave();
            }

        } else if (modify && docInfo.getStatus().equals(IInfoModel.STATUS_TMP)) {
            logger.debug("saveFromTmp");
            if (sendClaim) {
                sendClaim = Project.getSendClaimSave();
            }

        } else if (modify) {
            logger.debug("saveFromModify");
            if (sendClaim) {
                // modify 時に既に中途終了データがあれば sendClaim = true にする
                OrcaMasterDao dao = SqlDaoFactory.createOrcaMasterDao();
                List<OrcaEntry> entries = dao.getWksryactEntries(getContext().getPatient().getPatientId());
                if (entries.isEmpty()) {
                    sendClaim = Project.getSendClaimModify();
                } else {
                    sendClaim = Project.getSendClaim();
                }
            }
        }

        // 確認ダイアログを表示するかどうか

            // ダイアログを表示し，アクセス権等の保存時のパラメータを取得する
            params = new SaveParams(joinAreaNetwork);
            params.setTitle(text);
            params.setDepartment(model.getDocInfo().getDepartmentDesc());

            // 印刷枚数をPreferenceから取得する
            int numPrint = prefs.getInt("karte.print.count", 0);
            params.setPrintCount(numPrint);

            // CLAIM 送信
            params.setDisableSendClaim(getMode() == SINGLE_MODE);
            params.setSendClaim(sendClaim);

            Window parent = SwingUtilities.getWindowAncestor(this.getUI());
            SaveDialog sd = new SaveDialog(parent);
            params.setAllowPatientRef(false);    // 患者の参照
            params.setAllowClinicRef(false);     // 診療履歴のある医療機関
            sd.setValue(params);
            sd.start();
            params = sd.getValue();

            // 印刷枚数を保存する
            if (params.getSelection() == SaveDialog.SAVE) {
                prefs.putInt("karte.print.count", params.getPrintCount());
            }

        return params;
    }

    @Override
    public void save() {

        try {
            // 何も書かれていない時はリターンする
            if (!stateMgr.isDirty()) {
                logger.debug("not dirty");
                return;
            }

            // MML送信用のマスタIDを取得する
            // ケース１ HANIWA 方式 facilityID + patientID
            // ケース２ HIGO 方式 地域ID を使用
            ID masterID = Project.getMasterId(getContext().getPatient().getPatientId());

            sendMml = Project.getSendMML() && masterID != null && mmlListener != null;

            // この段階での CLAIM 送信 = 診療行為送信かつclaimListener!=null
            sendClaim = (Project.getSendClaim() && claimSender.getListener() != null);

            // 保存ダイアログを表示し，パラメータを得る
            // 地域連携に参加もしくはMML送信を行う場合は患者及び診療歴のある施設への参照許可
            // パラメータが設定できるようにする
            // boolean karteKey = (Project.getJoinAreaNetwork() || sendMml) ? true : false;
            // 地域連携に参加する場合のみに変更する
            SaveParams params = getSaveParams(Project.getJoinAreaNetwork());

            // キャンセルの場合はリターンする
            int selection = params.getSelection();
            if (selection == SaveDialog.SAVE || selection == SaveDialog.TMP_SAVE) {
                save2(params);
            } else if (selection == SaveDialog.DISPOSE) {
                // save 前に EditorFrame に Termination を送って dispose する
                competionListener.completed();
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 保存処理の主な部分を実行する.
     */
    private void save2(final SaveParams params) {

        // 文書末の余分な改行文字を削除する masuda
        KarteStyledDocument doc = (KarteStyledDocument) soaPane.getTextPane().getDocument();
        removeExtraCR(doc);
        doc = (KarteStyledDocument) pPane.getTextPane().getDocument();
        removeExtraCR(doc);
        removeRepeatedCR(doc);

        composeModel(params);

        final DocumentDelegater ddl = new DocumentDelegater();
        final DocumentModel saveModel = model;
        final Chart chart = this.getContext();

        DBTask<String> task = new DBTask<String>(chart) {

            @Override
            protected String doInBackground() {
                logger.debug("KarteSaveTask doInBackground");
                String ret = null;

                ddl.putKarte(saveModel);

                if (ddl.isNoError()) {
                    if (sendClaim) {
                        if (Project.getProjectStub().isUseOrcaApi()) {
                            OrcaApi api = OrcaApi.getInstance();
                            api.setContext(getContext());
                            api.send(saveModel);
                        } else {
                            claimSender.send(saveModel);
                        }
                    }
                    if (sendMml) {
                        sendMml(saveModel);
                    }
                } else {
                    ret = ddl.getErrorMessage();
                }
                return ret;
            }

            @Override
            protected void succeeded(String errMsg) {
                logger.debug("KarteSaveTask succeeded");
                if (ddl.isNoError()) {

                    // 印刷
                    int copies = params.getPrintCount();
                    if (copies > 0) {
                        printPanel2(chart.getContext().getPageFormat(), copies, false);
                    }

                    // 状態遷移する
                    stateMgr.setSaved(true);

                    // Chart の状態を設定する
                    // Chart.setChartState しておけば，ChartImpl の fireChanged で PVT にも同じく反映される
                    // 今日のカルテをセーブした場合のみ chartState を変更する
                    // 今日受診していて，過去のカルテを修正しただけなのに診察完了になってしまうのを防ぐ

                    DocInfoModel docInfo = model.getDocInfo();
                    boolean isTodaysKarte = MMLDate.getDateTime("yyyy-MM-dd").equals(docInfo.getFirstConfirmDateTrimTime());
                    if (isTodaysKarte) {
                        if (docInfo.getStatus().equals(STATUS_TMP)) {
                            chart.setChartState(KarteState.OPEN_TEMP);
                        } else if (docInfo.getStatus().equals(STATUS_FINAL)) {
                            chart.setChartState(KarteState.OPEN_SAVE);
                        }
                    }
                    // 文書履歴の更新を通知する
                    chart.getDocumentHistory().update(docInfo.getFirstConfirmDateTrimTime());

                    // save が終了したことを EditorFrame に知らせる
                    competionListener.completed();

                } else {
                    // errMsg を処理する
                    // エラーを表示する
                    JFrame parent = chart.getFrame();
                    String title = ClientContext.getString("karte.task.saveTitle");
                    JOptionPane.showMessageDialog(parent,
                            errMsg,
                            ClientContext.getFrameTitle(title),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        task.execute();
    }

    /**
     * save および claim 送信のために DocumentModel を compose する
     * by pns
     * @param params SaveParams
     */
    private void composeModel(SaveParams params) {
        //
        // DocInfoに値を設定する
        //
        DocInfoModel docInfo = model.getDocInfo();

        // 現在時刻を ConfirmDate にする
        Date confirmed = new Date();
        docInfo.setConfirmDate(confirmed);
        logger.debug("composeModel confirmed = " + docInfo.getConfirmDate());

        //
        // 修正でない場合は FirstConfirmDate = ConfirmDate にする
        // 修正の場合は FirstConfirmDate は既に設定されている
        // 修正でない新規カルテは parentId = null である
        //
        if (docInfo.getParentId() == null) {
            docInfo.setFirstConfirmDate(confirmed);
        }
        logger.debug("docInfo.firstConfirmDate = " + docInfo.getFirstConfirmDate());

        //
        // Status 仮保存か確定保存かを設定する
        // final の時は CLAIM 送信するが前の状態に依存する
        //
        if (!params.isTmpSave()) {
            //
            // 編集が開始された時の state を取得する
            //
            String oldStatus = docInfo.getStatus();

            switch (oldStatus) {
                case STATUS_NONE:
                    //
                    // NONEから確定への遷移 newSave
                    //
                    sendClaim = params.isSendClaim();
                    logger.debug("NONEから確定 : " + sendClaim);
                    break;
                case STATUS_TMP:
                    //
                    // 仮保存から確定へ遷移する場合   saveFromTmp
                    //
                    sendClaim = params.isSendClaim();
                    logger.debug("仮保存から確定 : " + sendClaim);
                    break;
                default:
                    //
                    // 確定から確定（修正の場合に相当する）以前は sendClaim = false;
                    //
                    sendClaim = params.isSendClaim();
                    logger.debug("修正 : " + sendClaim);
                    break;
            }

            //
            // 保存時の state を final にセットする
            //
            docInfo.setStatus(STATUS_FINAL);

        } else {
            //
            // 仮保存の場合 CLAIM 送信しない
            //
            sendClaim = false;
            logger.debug("仮保存 : " + sendClaim);

            sendMml = false;
            docInfo.setStatus(STATUS_TMP);
        }
        // single mode では claim 送らない
        if (getMode() == SINGLE_MODE) {
            sendClaim = false;
            sendMml = false;
        }

        // titleを設定する
        docInfo.setTitle(params.getTitle());

        // デフォルトのアクセス権を設定をする TODO
        AccessRightModel ar = new AccessRightModel();
        ar.setPermission(PERMISSION_ALL);
        ar.setLicenseeCode(ACCES_RIGHT_CREATOR);
        ar.setLicenseeName(ACCES_RIGHT_CREATOR_DISP);
        ar.setLicenseeCodeType(ACCES_RIGHT_FACILITY_CODE);
        docInfo.addAccessRight(ar);

        // 患者のアクセス権を設定をする
        if (params.isAllowPatientRef()) {
            ar = new AccessRightModel();
            ar.setPermission(PERMISSION_READ);
            ar.setLicenseeCode(ACCES_RIGHT_PATIENT);
            ar.setLicenseeName(ACCES_RIGHT_PATIENT_DISP);
            ar.setLicenseeCodeType(ACCES_RIGHT_PERSON_CODE);
            docInfo.addAccessRight(ar);
        }

        // 診療履歴のある施設のアクセス権を設定をする
        if (params.isAllowClinicRef()) {
            ar = new AccessRightModel();
            ar.setPermission(PERMISSION_READ);
            ar.setLicenseeCode(ACCES_RIGHT_EXPERIENCE);
            ar.setLicenseeName(ACCES_RIGHT_EXPERIENCE_DISP);
            ar.setLicenseeCodeType(ACCES_RIGHT_EXPERIENCE_CODE);
            docInfo.addAccessRight(ar);
        }

        // ProgressCourseModule の ModuleInfo を保存しておく
        ModuleInfoBean soaProgressInfo = null;
        ModuleInfoBean pProgressInfo = null;
        ModuleInfoBean[] progressInfos = model.getModuleInfo(MODULE_PROGRESS_COURSE);

        if (progressInfos == null) {
            // 存在しない場合は新規に作成する
            soaProgressInfo = new ModuleInfoBean();
            soaProgressInfo.setStampName(MODULE_PROGRESS_COURSE);
            soaProgressInfo.setEntity(MODULE_PROGRESS_COURSE);
            soaProgressInfo.setStampRole(ROLE_SOA_SPEC);

            pProgressInfo = new ModuleInfoBean();
            pProgressInfo.setStampName(MODULE_PROGRESS_COURSE);
            pProgressInfo.setEntity(MODULE_PROGRESS_COURSE);
            pProgressInfo.setStampRole(ROLE_P_SPEC);

            logger.debug("ModuleInfoBean[] progressInfo created");

        } else {
            if (progressInfos[0].getStampRole().equals(ROLE_SOA_SPEC)) {
                soaProgressInfo = progressInfos[0];
                pProgressInfo = progressInfos[1];
            } else if (progressInfos[1].getStampRole().equals(ROLE_SOA_SPEC)) {
                soaProgressInfo = progressInfos[1];
                pProgressInfo = progressInfos[0];
            }
        }

        //
        // モデルのモジュールをヌルに設定する
        // エディタの画面をダンプして生成したモジュールを設定する
        //
        model.clearModules();
        model.clearSchema();
        logger.debug("model.clearModules(), model.clearSchema()");

        //
        // SOAPane をダンプし model に追加する
        //
        KartePaneDumper_2 dumper = new KartePaneDumper_2();
        KarteStyledDocument doc = (KarteStyledDocument) soaPane.getTextPane().getDocument();

        dumper.dump(doc);
        dumper.getModule().forEach(model::addModule);

        // ProgressCourse SOA を生成する
        ProgressCourse soaPc = new ProgressCourse();
        soaPc.setFreeText(dumper.getSpec());
        ModuleModel soaProgressModule = new ModuleModel();
        soaProgressModule.setModuleInfo(soaProgressInfo);
        soaProgressModule.setModel(soaPc);
        model.addModule(soaProgressModule);

        //
        // Schema を追加する
        //
        dumper.getSchema().forEach(model::addSchema);

        //
        // PPane をダンプし model に追加する
        //
        if (getMode() == DOUBLE_MODE) {
            KarteStyledDocument pdoc = (KarteStyledDocument) pPane.getTextPane().getDocument();
            dumper.dump(pdoc);
            dumper.getModule().forEach(model::addModule);

            // ProgressCourse P を生成する
            ProgressCourse pProgressCourse = new ProgressCourse();
            pProgressCourse.setFreeText(dumper.getSpec());
            ModuleModel pProgressModule = new ModuleModel();
            pProgressModule.setModuleInfo(pProgressInfo);
            pProgressModule.setModel(pProgressCourse);
            model.addModule(pProgressModule);
        }

        // FLAGを設定する
        // image があるかどうか
        boolean flag = model.getSchema() != null;
        docInfo.setHasImage(flag);
        logger.debug("hasImage = " + flag);

        // RP があるかどうか
        flag = model.getModule(ENTITY_MED_ORDER) != null;
        docInfo.setHasRp(flag);
        logger.debug("hasRp = " + flag);

        // 処置があるかどうか
        flag = model.getModule(ENTITY_TREATMENT) != null;
        docInfo.setHasTreatment(flag);
        logger.debug("hasTreatment = " + flag);

        // LaboTest があるかどうか
        flag = model.getModule(ENTITY_LABO_TEST) != null;
        docInfo.setHasLaboTest(flag);
        logger.debug("hasLaboTest = " + flag);

        //
        // EJB3.0 Model の関係を構築する
        //
        // confirmed, firstConfirmed は設定済み
        KarteBean karte = getContext().getKarte();
        model.setKarte(karte);                          // karte
        model.setCreator(Project.getUserModel());       // 記録者
        model.setRecorded(docInfo.getConfirmDate());    // 記録日

        // Moduleとの関係を設定する
        Collection<ModuleModel> moduleBeans = model.getModules();
        int number = 0;
        int totalSize = 0;
        for (ModuleModel bean : moduleBeans) {

            bean.setId(0L);                             // unsaved-value
            bean.setKarte(karte);                       // Karte
            bean.setCreator(Project.getUserModel());    // 記録者
            bean.setDocument(model);                    // Document
            bean.setConfirmed(docInfo.getConfirmDate());            // 確定日
            bean.setFirstConfirmed(docInfo.getFirstConfirmDate());  // 適合開始日
            bean.setRecorded(docInfo.getConfirmDate());             // 記録日
            bean.setStatus(STATUS_FINAL);                           // status

            // 全角を Kill する
            if (bean.getModel() instanceof BundleMed) {
                BundleMed med = (BundleMed) bean.getModel();
                ClaimItem[] items = med.getClaimItem();
                if (items != null && items.length > 0) {
                    for (ClaimItem item : items) {
                        String num = item.getNumber();
                        if (num != null) {
                            num = StringTool.toHankakuNumber(num);
                            item.setNumber(num);
                        }
                    }
                }
                String bNum = med.getBundleNumber();
                if (bNum != null) {
                    bNum = StringTool.toHankakuNumber(bNum);
                    med.setBundleNumber(bNum);
                }
            }
            else if (bean.getModel() instanceof ClaimBundle) {
                ClaimBundle bundle = (ClaimBundle) bean.getModel();
                ClaimItem[] items = bundle.getClaimItem();
                if (items != null && items.length > 0) {
                    for (ClaimItem item : items) {
                        String num = item.getNumber();
                        if (num != null) {
                            num = StringTool.toHankakuNumber(num);
                            item.setNumber(num);
                        }
                    }
                }
                String bNum = bundle.getBundleNumber();
                if (bNum != null) {
                    bNum = StringTool.toHankakuNumber(bNum);
                    bundle.setBundleNumber(bNum);
                }
            }

            bean.setBeanBytes(ModelUtils.xmlEncode(bean.getModel()));

            // ModuleInfo を設定する
            // Name, Role, Entity は設定されている
            ModuleInfoBean mInfo = bean.getModuleInfo();
            mInfo.setStampNumber(number++);

            int size = bean.getBeanBytes().length / 1024;
            logger.debug("stamp size(KB) = " + size);
            totalSize += size;
        }
        logger.debug("stamp total size(KB) = " + totalSize);

        // 画像との関係を設定する
        number = 0;
        Collection<SchemaModel> imagesimages = model.getSchema();
        if (imagesimages != null && imagesimages.size() > 0) {
            for (SchemaModel bean : imagesimages) {
                bean.setId(0L);                                         // unsaved
                bean.setKarte(karte);                                   // Karte
                bean.setCreator(Project.getUserModel());                // Creator
                bean.setDocument(model);                                // Document
                bean.setConfirmed(docInfo.getConfirmDate());            // 確定日
                bean.setFirstConfirmed(docInfo.getFirstConfirmDate());  // 適合開始日
                bean.setRecorded(docInfo.getConfirmDate());             // 記録日
                bean.setStatus(STATUS_FINAL);                           // Status
                bean.setImageNumber(number);

                ExtRefModel ref = bean.getExtRef();
                String href = String.format("%s-%d.jpg", model.getDocInfo().getDocId(), number);
                ref.setHref(href);

                number++;
            }
        }
    }

    /**
     * Save 時ではなくメニューから CLAIM 送信する.
     * EditorFrame で編集中の場合はここが呼ばれる.
     */
    public void sendClaim() {
        logger.fatal("sendClaim() in KarteEditor called.");
        /*
        ToDO Edit 中の内容が送られるようにしたい
        String message;
        int messageType;

        if (! Project.getSendClaim()) {
            message = "CLAIM を送信しない設定になっています";
            messageType = JOptionPane.ERROR_MESSAGE;

        } else {

            model.setKarte(getContext().getKarte());
            model.getDocInfo().setConfirmDate(new Date());
            if (getMode() == DOUBLE_MODE) {
                if (Project.getProjectStub().isUseOrcaApi()) {
                    OrcaApi api = OrcaApi.getInstance();
                    api.setContext(getContext());
                    api.send(model);
                } else {
                    claimSender.send(model);
                }
            }
            message = "ORCA に送信しました";
            messageType = JOptionPane.PLAIN_MESSAGE;
        }

        Frame parent = getContext().getFrame();
        if (JSheet.isAlreadyShown(parent)) {
            parent.toFront();
            return;
        }
        JSheet.showMessageSheet(parent, message, messageType);
        */
    }

    /**
     * MML送信を行う.
     */
    private void sendMml(DocumentModel sendModel) {

        Chart chart = (KarteEditor.this).getContext();

        // MML Message を生成する
        MMLHelper mb = new MMLHelper();
        mb.setDocument(sendModel);
        mb.setUser(Project.getUserModel());
        mb.setPatientId(chart.getPatient().getPatientId());
        mb.buildText();

        try {
            VelocityContext context = ClientContext.getVelocityContext();
            context.put("mmlHelper", mb);

            // このスタンプのテンプレートファイルを得る
            String templateFile = "mml2.3Helper.vm";

            // Merge する
            StringWriter sw = new StringWriter();
            BufferedReader reader;
            try (BufferedWriter bw = new BufferedWriter(sw)) {
                InputStream instream = ClientContext.getTemplateAsStream(templateFile);
                reader = new BufferedReader(new InputStreamReader(instream, StandardCharsets.UTF_8));
                Velocity.evaluate(context, bw, "mml", reader);
                bw.flush();
            }
            reader.close();
            String mml = sw.toString();
            //System.out.println(mml);
            System.out.println("KarteEditor.sendMml mml=" + mml);

            // debug出力を行う
            if (ClientContext.getMmlLogger() != null) {
                ClientContext.getMmlLogger().debug(mml);
            }

            if (sendMml && mmlListener != null) {
                MmlMessageEvent mevt = new MmlMessageEvent(this);
                mevt.setGroupId(mb.getDocId());
                mevt.setMmlInstance(mml);
                if (mb.getSchema() != null) {
                    mevt.setSchema(mb.getSchema());
                }
                mmlListener.mmlMessageEvent(mevt);
            }

        } catch (IOException | ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * このエディタの状態インターフェース.
     */
    private interface EditorState {
        public abstract boolean isDirty();
        public abstract void controlMenu();
    }

    /**
     * No dirty 状態クラス.
     */
    private final class NoDirtyState implements EditorState {

        @Override
        public void controlMenu() {
            Chart chart = getContext();
            chart.enabledAction(GUIConst.ACTION_SAVE, false); // 保存
            chart.enabledAction(GUIConst.ACTION_PRINT, false); // 印刷
            chart.enabledAction(GUIConst.ACTION_CUT, false);
            chart.enabledAction(GUIConst.ACTION_COPY, false);
            chart.enabledAction(GUIConst.ACTION_PASTE, false);
            chart.enabledAction(GUIConst.ACTION_UNDO, false);
            chart.enabledAction(GUIConst.ACTION_REDO, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_TEXT, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_STAMP, false);
            chart.enabledAction(GUIConst.ACTION_SELECT_INSURANCE, !modify);

            chart.enabledAction(GUIConst.ACTION_FIND_FIRST, false);
            chart.enabledAction(GUIConst.ACTION_FIND_NEXT, false);
            chart.enabledAction(GUIConst.ACTION_FIND_PREVIOUS, false);
        }

        @Override
        public boolean isDirty() {
            return false;
        }
    }

    /**
     * Dirty 状態クラス.
     */
    private final class DirtyState implements EditorState {

        @Override
        public void controlMenu() {
            Chart chart = getContext();
            chart.enabledAction(GUIConst.ACTION_SAVE, true);
            chart.enabledAction(GUIConst.ACTION_PRINT, true);
            chart.enabledAction(GUIConst.ACTION_SELECT_INSURANCE, !modify);

            chart.enabledAction(GUIConst.ACTION_FIND_FIRST, false);
            chart.enabledAction(GUIConst.ACTION_FIND_NEXT, false);
        }

        @Override
        public boolean isDirty() {
            return true;
        }
    }

    /**
     * EmptyNew 状態クラス.
     */
    private final class SavedState implements EditorState {

        @Override
        public void controlMenu() {
            Chart chart = getContext();
            chart.enabledAction(GUIConst.ACTION_SAVE, false);
            chart.enabledAction(GUIConst.ACTION_PRINT, true);
            chart.enabledAction(GUIConst.ACTION_CUT, false);
            chart.enabledAction(GUIConst.ACTION_COPY, false);
            chart.enabledAction(GUIConst.ACTION_PASTE, false);
            chart.enabledAction(GUIConst.ACTION_UNDO, false);
            chart.enabledAction(GUIConst.ACTION_REDO, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_TEXT, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            chart.enabledAction(GUIConst.ACTION_INSERT_STAMP, false);
            chart.enabledAction(GUIConst.ACTION_SELECT_INSURANCE, false);

            chart.enabledAction(GUIConst.ACTION_FIND_FIRST, false);
            chart.enabledAction(GUIConst.ACTION_FIND_NEXT, false);
        }

        @Override
        public boolean isDirty() {
            return false;
        }
    }

    /**
     * 状態マネージャ.
     */
    private final class StateMgr {

        private final EditorState noDirtyState = new NoDirtyState();
        private final EditorState dirtyState = new DirtyState();
        private final EditorState savedState = new SavedState();
        private EditorState currentState;

        public StateMgr() {
            currentState = noDirtyState;
        }

        public boolean isDirty() {
            return currentState.isDirty();
        }

        public void setDirty(boolean dirty) {
            currentState = dirty ? dirtyState : noDirtyState;
            currentState.controlMenu();
        }

        public void setSaved(boolean saved) {
            if (saved) {
                currentState = savedState;
                currentState.controlMenu();
            }
        }

        public void controlMenu() {
            currentState.controlMenu();
        }
    }

    /**
     * 文頭・文末の無駄な改行文字を削除する.
     * original by masuda-sensei
     * @param kd KarteStyledDocument
     */
    private void removeExtraCR(KarteStyledDocument kd) {
        // これが一番速い！ 20個の改行削除に2msec!!
        try {
            int len = kd.getLength();
            int pos;
            // 改行文字以外が出てくるまで文頭からスキャン
            for (pos = 0; pos < len - 1; pos++) {
                if (!"\n".equals(kd.getText(pos, 1))) {
                    break;
                }
            }
            if (pos > 0) {
                kd.remove(0, pos);
            }

            len = kd.getLength();
            // 改行文字以外が出てくるまで文書末からスキャン
            for (pos = len - 1; pos >= 0; --pos) {
                if (!"\n".equals(kd.getText(pos, 1))) {
                    break;
                }
            }
            ++pos;  // 一文字戻す
            if (len - pos > 0) {
                kd.remove(pos, len - pos);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * 3個以上連続する改行を2個にする.
     * つまり，２つ以上連続する空行を１つにする.
     * @param kd KarteStyledDocument
     */
    private void removeRepeatedCR(KarteStyledDocument kd) {
        int pos = 0;
        int crPos = 0;

        while(pos < kd.getLength()) {
            try {
                if (crPos == 0 && "\n".equals(kd.getText(pos,1))) {
                    crPos = pos;
                }
                if (crPos != 0 &&  !"\n".equals(kd.getText(pos,1))) {
                    int len = pos - crPos;
                    if (len > 1) {
                        kd.remove(crPos+1, len-1);
                        pos -= (len - 1);
                    }
                    crPos = 0;
                }
                pos ++;

            } catch (BadLocationException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}

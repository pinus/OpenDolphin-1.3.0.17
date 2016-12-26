package open.dolphin.client;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TooManyListenersException;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import open.dolphin.dao.OrcaEntry;
import open.dolphin.dao.OrcaMasterDao;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.*;
import open.dolphin.message.MMLHelper;
import open.dolphin.orcaapi.OrcaApi;
import open.dolphin.project.Project;
import open.dolphin.ui.MyBorderFactory;
import open.dolphin.ui.MyJSheet;
import open.dolphin.util.MMLDate;
import open.dolphin.util.StringTool;
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
    private static final String UPDATE_TAB_TITLE = "更新";
    // このエディタのモード
    private int mode = 2;
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
    public static String SAVE_DONE = "saveDoneProp";
    private final PropertyChangeSupport boundSupport = new PropertyChangeSupport(new Object());

    private final Logger logger = ClientContext.getBootLogger();

    public PropertyChangeSupport getBoundSupport() {
        return boundSupport;
    }

    public KarteEditor() {
        setTitle(DEFAULT_TITLE);
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
            ex.printStackTrace();
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

        } else if (selected == JFileChooser.CANCEL_OPTION) {
            return;
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
    protected KartePane getPPane() {
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
     * @throws TooManyListenersException
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

    @Override
    public void setDirty(boolean dirty) {
        if (getMode() == SINGLE_MODE) {
            stateMgr.setDirty(soaPane.isDirty());
        } else {
            boolean bdirty = soaPane.isDirty() || pPane.isDirty();
            stateMgr.setDirty(bdirty);
        }
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

        kp1.getTimeStampPanel().setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));

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

        kp2.getTimeStampPanel().setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));

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
    }

    @Override
    public void stop() {
        // リソース解放した方がいい？
        //soaPane.clear();
        //if (getMode() == DOUBLE_MODE) pPane.clear();
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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // キャレットを先頭にリセット.
                getSOAPane().getTextPane().setCaretPosition(0);
                getPPane().getTextPane().setCaretPosition(0);
                getSOAPane().getTextPane().requestFocusInWindow();
            }
        });
        enter();
    }

    /**
     * DocumentModelを表示する.
     */
    private void displayModel() {

        // Timestamp を表示する
        Date now = new Date();
        timeStamp = ModelUtils.getDateAsFormatString(now, IInfoModel.KARTE_DATE_FORMAT);

        // 修正の場合
        if (modify) {
            // 更新: YYYY-MM-DDTHH:MM:SS (firstConfirmDate)
            StringBuilder buf = new StringBuilder();
            buf.append(UPDATE_TAB_TITLE);
            buf.append(": ");
            buf.append(timeStamp);
            buf.append(" [");
            buf.append(ModelUtils.getDateAsFormatString(model.getDocInfo().getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT));
            buf.append("]");
            timeStamp = buf.toString();
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
            ins = ((ChartImpl) getContext()).getHealthInsurances();
        } else if (getContext() instanceof EditorFrame) {
            EditorFrame ef = (EditorFrame) getContext();
            ChartImpl chart = (ChartImpl) ef.getChart();
            ins = chart.getHealthInsurances();
        }

        // Model に設定してある健康保険を選択する
        String selecteIns = null;
        String insGUID = getModel().getDocInfo().getHealthInsuranceGUID();
        if (insGUID != null) {
            for (int i = 0; i < ins.length; i++) {
                String GUID = ins[i].getGUID();
                if (GUID != null && GUID.equals(insGUID)) {
                    selecteIns = ins[i].toString();
                    break;
                }
            }
        }

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
     * @params sendMML MML送信フラグ 送信するとき true
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

        SaveParams params = null;

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
                ArrayList<OrcaEntry> entries = dao.getWksryactEntries(getContext().getPatient().getPatientId());
                if (entries.isEmpty()) {
                    sendClaim = Project.getSendClaimModify();
                } else {
                    sendClaim = Project.getSendClaim();
                }
            }
        }

        // 確認ダイアログを表示するかどうか
//        if (Project.getPreferences().getBoolean(Project.KARTE_SHOW_CONFIRM_AT_SAVE, true)) {

            // ダイアログを表示し，アクセス権等の保存時のパラメータを取得する
            params = new SaveParams(joinAreaNetwork);
            params.setTitle(text);
            params.setDepartment(model.getDocInfo().getDepartmentDesc());

            // 印刷枚数をPreferenceから取得する
            Preferences prefs = Preferences.userNodeForPackage(this.getClass());
            int numPrint = prefs.getInt("karte.print.count", 0);
            params.setPrintCount(numPrint);

            // CLAIM 送信
            params.setDisableSendClaim(getMode() == SINGLE_MODE);
            params.setSendClaim(sendClaim);

            Window parent = SwingUtilities.getWindowAncestor(this.getUI());
            SaveDialog2 sd = new SaveDialog2(parent);
            params.setAllowPatientRef(false);    // 患者の参照
            params.setAllowClinicRef(false);     // 診療履歴のある医療機関
            sd.setValue(params);
            sd.start();
            params = sd.getValue();

            // 印刷枚数を保存する
            if (params.getSelection() == SaveDialog2.SAVE) {
                prefs.putInt("karte.print.count", params.getPrintCount());
            }

  /*      } else {

            //
            // 確認ダイアログを表示しない
            //
            params = new SaveParams(false);
            params.setTitle(text);
            params.setDepartment(model.getDocInfo().getDepartmentDesc());
            params.setPrintCount(Project.getPreferences().getInt(Project.KARTE_PRINT_COUNT, 0));

            //
            // 仮保存が指定されている端末の場合
            //
            int sMode = Project.getPreferences().getInt(Project.KARTE_SAVE_ACTION, 0);
            boolean tmpSave = sMode == 1 ? true : false;
            params.setTmpSave(tmpSave);
            if (tmpSave) {
                params.setSendClaim(false);
            } else {
                //
                // 保存が実行される端末の場合
                //
                params.setSendClaim(sendClaim);
            }

            //
            // 患者参照，施設参照不可
            //
            params.setAllowClinicRef(false);
            params.setAllowPatientRef(false);
        }
*/

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

            sendMml = (Project.getSendMML() && masterID != null && mmlListener != null)
                    ? true
                    : false;

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
            if (selection == SaveDialog2.SAVE || selection == SaveDialog2.TMP_SAVE) {
                save2(params);
            }
            else if (selection == SaveDialog2.DISPOSE) {
                // save 前に EditorFrame に SAVE_DONE を送って dispose する
                boundSupport.firePropertyChange(KarteEditor.SAVE_DONE, false, true);
            }

        } catch (Exception e) {
            System.out.println("KarteEditor.java: " + e);
        }
    }

    /**
     * 保存処理の主な部分を実行する.
     */
    private void save2(final SaveParams params) throws Exception {

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

        DBTask task = new DBTask<String>(chart) {

            @Override
            protected String doInBackground() throws Exception {
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
                    chart.getDocumentHistory().getDocumentHistory(docInfo.getFirstConfirmDateTrimTime());

                    // save が終了したことを EditorFrame に知らせる
                    boundSupport.firePropertyChange(KarteEditor.SAVE_DONE, false, true);

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
     * @param params
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

            if (oldStatus.equals(STATUS_NONE)) {
                //
                // NONEから確定への遷移 newSave
                //
                sendClaim = params.isSendClaim();
                logger.debug("NONEから確定 : " + sendClaim);

            } else if (oldStatus.equals(STATUS_TMP)) {
                //
                // 仮保存から確定へ遷移する場合   saveFromTmp
                // 同日の場合だけ CLIAM 送信する
                //
                //String first = ModelUtils.getDateAsString(docInfo.getFirstConfirmDate());
                //String cd = ModelUtils.getDateAsString(docInfo.getConfirmDate());
                //if (first.equals(cd)) {
                    //sendClaim = params.isSendClaim();
                //} else {
                    //sendClaim = false;
                //}
                sendClaim = params.isSendClaim();
                logger.debug("仮保存から確定 : " + sendClaim);

            } else {
                //
                // 確定から確定（修正の場合に相当する）以前は sendClaim = false;
                //
                sendClaim = params.isSendClaim();

                logger.debug("修正 : " + sendClaim);
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
        ModuleModel[] soa = dumper.getModule();
        if (soa != null && soa.length > 0) {
            logger.debug("soaPane dumped, number of SOA modules = " + soa.length);
            model.addModule(soa);
        } else {
            logger.debug("soaPane dumped, no module");
        }
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
        int maxImageWidth = ClientContext.getInt("image.max.width");
        int maxImageHeight = ClientContext.getInt("image.max.height");
        Dimension maxSImageSize = new Dimension(maxImageWidth, maxImageHeight);
        SchemaModel[] schemas = dumper.getSchema();
        if (schemas != null && schemas.length > 0) {
            // 保存のため Icon を JPEG に変換する
            for (SchemaModel schema : schemas) {
                ImageIcon icon = schema.getIcon();
                icon = adjustImageSize(icon, maxSImageSize);
                byte[] jpegByte = getJPEGByte(icon.getImage());
                schema.setJpegByte(jpegByte);
                schema.setIcon(null);
                model.addSchema(schema);
            }
            logger.debug("schema dumped, number of SchemaModel = " + schemas.length);
        } else {
            logger.debug("zero schema dumped");
        }

        //
        // PPane をダンプし model に追加する
        //
        if (getMode() == DOUBLE_MODE) {
            KartePaneDumper_2 pdumper = new KartePaneDumper_2();
            KarteStyledDocument pdoc = (KarteStyledDocument) pPane.getTextPane().getDocument();
            pdumper.dump(pdoc);
            ModuleModel[] plan = pdumper.getModule();

            if (plan != null && plan.length > 0) {
                model.addModule(plan);
                logger.debug("p dumped, number of p = " + plan.length);
            } else {
                sendClaim = false;
                logger.debug("p dumped, number of p = 0");
            }

            // ProgressCourse P を生成する
            ProgressCourse pProgressCourse = new ProgressCourse();
            pProgressCourse.setFreeText(pdumper.getSpec());
            ModuleModel pProgressModule = new ModuleModel();
            pProgressModule.setModuleInfo(pProgressInfo);
            pProgressModule.setModel(pProgressCourse);
            model.addModule(pProgressModule);
        }

        // FLAGを設定する
        // image があるかどうか
        boolean flag = model.getSchema() != null ? true : false;
        docInfo.setHasImage(flag);
        logger.debug("hasImage = " + flag);

        // RP があるかどうか
        flag = model.getModule(ENTITY_MED_ORDER) != null ? true : false;
        docInfo.setHasRp(flag);
        logger.debug("hasRp = " + flag);

        // 処置があるかどうか
        flag = model.getModule(ENTITY_TREATMENT) != null ? true : false;
        docInfo.setHasTreatment(flag);
        logger.debug("hasTreatment = " + flag);

        // LaboTest があるかどうか
        flag = model.getModule(ENTITY_LABO_TEST) != null ? true : false;
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
        totalSize = 0;

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
                StringBuilder sb = new StringBuilder();
                sb.append(model.getDocInfo().getDocId());
                sb.append("-");
                sb.append(number);
                sb.append(".jpg");
                ref.setHref(sb.toString());

                int size = bean.getJpegByte().length / 1024;
                logger.debug("schema size(KB) = " + size);
                totalSize += size;
                number++;
            }
            logger.debug("total schema size(KB) = " + totalSize);
        }
    }

    /**
     * Courtesy of Junzo SATO
     * ImageIO by pns
     */
    private byte[] getJPEGByte(Image image) {

        byte[] ret = null;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        try {
            JPanel myPanel = getUI();
            Dimension d = new Dimension(image.getWidth(myPanel), image.getHeight(myPanel));
            BufferedImage bf = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_BGR);
            Graphics g = bf.getGraphics();
            g.setColor(Color.white);
            g.drawImage(image, 0, 0, d.width, d.height, myPanel);

            ImageIO.write(bf, "png", bo);

            bo.flush();
            ret = bo.toByteArray();

            g.dispose();

        } catch (IOException e) {
            System.out.println("KarteEditor.java: "+ e);
        } finally {
            try {
                bo.close();
            } catch (IOException e2) {
                System.out.println("KarteEditor.java: "+ e2);
            }
        }
        return ret;
    }

    private ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {

        if ((icon.getIconHeight() > dim.height) ||
                (icon.getIconWidth() > dim.width)) {
            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / dim.height;
            float wRatio = (float) icon.getIconWidth() / dim.width;
            int h,
             w;
            if (hRatio > wRatio) {
                h = dim.height;
                w = (int) (icon.getIconWidth() / hRatio);
            } else {
                w = dim.width;
                h = (int) (icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            return icon;
        }
    }

    /**
     * Save 時ではなくメニューから CLAIM 送信する.
     * EditorFrame で編集中の場合はここが呼ばれる.
     */
    public void sendClaim() {
        logger.fatal("sendClaim() in KarteEdito called.");
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
        if (MyJSheet.isAlreadyShown(parent)) {
            parent.toFront();
            return;
        }
        MyJSheet.showMessageSheet(parent, message, messageType);
        */
    }

    /**
     * MML送信を行う
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
            BufferedWriter bw = new BufferedWriter(sw);
            InputStream instream = ClientContext.getTemplateAsStream(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            Velocity.evaluate(context, bw, "mml", reader);
            bw.flush();
            bw.close();
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

        } catch (IOException ex) {
            System.out.println("KarteEditor.java: " + ex);
        } catch (ParseErrorException ex) {
            System.out.println("KarteEditor.java: " + ex);
        } catch (MethodInvocationException ex) {
            System.out.println("KarteEditor.java: " + ex);
        } catch (ResourceNotFoundException ex) {
            System.out.println("KarteEditor.java: " + ex);
        }
    }

    /**
     * このエディタの抽象状態クラス
     */
    private abstract class EditorState {

        public EditorState() {
        }

        public abstract boolean isDirty();

        public abstract void controlMenu();
    }

    /**
     * No dirty 状態クラス
     */
    private final class NoDirtyState extends EditorState {

        public NoDirtyState() {
        }

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
     * Dirty 状態クラス
     */
    private final class DirtyState extends EditorState {

        public DirtyState() {
        }

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
     * EmptyNew 状態クラス
     */
    private final class SavedState extends EditorState {

        public SavedState() {
        }

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
     * 状態マネージャ
     */
    private final class StateMgr {

        private EditorState noDirtyState = new NoDirtyState();
        private EditorState dirtyState = new DirtyState();
        private EditorState savedState = new SavedState();
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

    private void removeExtraCR(KarteStyledDocument kd) {

        // 文書末の余分な改行文字を削除する masuda
/*
        // すっきりしている 20個の改行削除に7msec
        int pos = kd.getLength() - 1;
        try {
            while ("\n".equals(kd.getText(pos, 1))) {
                kd.remove(pos, 1);
                --pos;
            }
        } catch (Exception ex) {
        }
*/
/*
        // いっそのことこれでもいい. 20個の改行削除に7msec でも…
        try {
            while ("\n".equals(kd.getText(kd.getLength() - 1, 1))) {
                kd.remove(kd.getLength() - 1, 1);
            }
        } catch (Exception ex) {
        }
*/
        // これが一番速い！ 20個の改行削除に2msec!!
        int len = kd.getLength();
        try {
            int pos;
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
            System.out.println("KarteEditor.java: " + ex);
        }
    }

    /**
     * ２個以上連続する改行を１個にする
     * @param kd
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
                System.out.println("KarteEditor.java: " + ex);
            }
        }
    }
}

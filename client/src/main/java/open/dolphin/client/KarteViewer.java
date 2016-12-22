package open.dolphin.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.ui.MyBorderFactory;

/**
 * シングルドキュメントのビュワークラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class KarteViewer extends AbstractChartDocument implements Comparable {

    // 選択されている時のボーダ色
    private static final Color SELECTED_COLOR = new Color(255, 0, 153);
    // 選択された状態のボーダ
    private static final Border SELECTED_BORDER = MyBorderFactory.createSelectedBorder();
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    // 選択されていない時のボーダ色
    private static final Color NOT_SELECTED_COLOR = new Color(227, 250, 207);
    // 選択されていない状態のボーダ
    private static final Border NOT_SELECTED_BORDER = MyBorderFactory.createClearBorder();
    // タイムスタンプの foreground カラー
    private static final Color TIMESTAMP_FORE = Color.BLUE;
    // タイムスタンプのフォントサイズ
    private static final int TIMESTAMP_FONT_SIZE = 12;
    // タイムスタンプフォント
    private static final Font TIMESTAMP_FONT = new Font("Dialog", Font.PLAIN, TIMESTAMP_FONT_SIZE);
    private static final Font TIMESTAMP_FONT_BOLD = new Font("Dialog", Font.BOLD, TIMESTAMP_FONT_SIZE);
    // タイムスタンプパネル FlowLayout のマージン
    private static final int TIMESTAMP_SPACING = 7;
    // 仮保存中のドキュメントを表す文字
    protected static final String UNDER_TMP_SAVE = " - 仮保存中";

    // インスタンス変数
    // この view のモデル
    protected DocumentModel model;
    // タイムスタンプラベル
    protected JLabel timeStampLabel;
    // SOA Pane
    protected KartePane soaPane;
    // 2号カルテパネル
    protected Panel2 panel2;
    // タイムスタンプの foreground カラー
    protected Color timeStampFore = TIMESTAMP_FORE;
    // タイムスタンプのフォント
    protected Font timeStampFont = TIMESTAMP_FONT;
    protected Font timeStampFontBold = TIMESTAMP_FONT_BOLD;
    protected int timeStampSpacing = TIMESTAMP_SPACING;
    protected int timeStampPanelHeight;
    protected boolean avoidEnter;

    // 選択されているかどうかのフラグ
    protected boolean selected;

    public KarteViewer() {}

    public String getDocType() {
        if (model != null) {
            String docType = model.getDocInfo().getDocType();
            return docType;
        }
        return null;
    }

    public void setAvoidEnter(boolean b) {
        avoidEnter = b;
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    // Junzo SATO
    public void printPanel2(final PageFormat format) {
        String name = getContext().getPatient().getFullName();
        panel2.printPanel(format, 1, true, name, panel2.getPreferredSize().height + 30);
    }

    public void printPanel2(final PageFormat format, final int copies,
            final boolean useDialog) {
        String name = getContext().getPatient().getFullName();
        panel2.printPanel(format, copies, useDialog, name, panel2.getPreferredSize().height + 30);
    }

    @Override
    public void print() {
        PageFormat pageFormat = getContext().getContext().getPageFormat();
        this.printPanel2(pageFormat);
    }

    /**
     * SOA Pane を返す.
     * @return soaPane
     */
    public KartePane getSOAPane() {
        return soaPane;
    }

    /**
     * コンテナからコールされる enter() メソッドで
     * メニューを制御する.
     */
    @Override
    public void enter() {

        if (avoidEnter) {
            return;
        }
        super.enter();

        // ReadOnly 属性
        boolean canEdit = getContext().isReadOnly() ? false : true;

        // 仮保存かどうか
        boolean tmp = model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP) ? true : false;

        // 新規カルテ作成が可能な条件
        boolean newOk = canEdit && (!tmp) ? true : false;

        ChartMediator mediator = getContext().getChartMediator();
        mediator.getAction(GUIConst.ACTION_NEW_KARTE).setEnabled(newOk);        // 新規カルテ
        mediator.getAction(GUIConst.ACTION_PRINT).setEnabled(true);             // 印刷
        mediator.getAction(GUIConst.ACTION_MODIFY_KARTE).setEnabled(canEdit);   // 修正
    }

    /**
     * シングルカルテで初期化する.
     */
    private void initialize() {

        KartePanel1 kp1 = new KartePanel1();
        panel2 = kp1;

        // TimeStampLabel を生成する
        timeStampLabel = kp1.getTimeStampLabel();
        timeStampLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeStampLabel.setForeground(timeStampFore);
        timeStampLabel.setFont(timeStampFont);
        timeStampPanelHeight = kp1.getTimeStampPanelHeight();
        kp1.getTimeStampPanel().setBorder(MyBorderFactory.createTitleBorder(new Insets(0,0,0,0)));

        // SOA Pane を生成する
        soaPane = new KartePane();
        soaPane.setTextPane(kp1.getSoaTextPane());
        soaPane.setRole(IInfoModel.ROLE_SOA);
        if (model != null) {
            // Schema 画像にファイル名を付けるのために必要
            String docId = model.getDocInfo().getDocId();
            soaPane.setDocId(docId);
        }

        setUI(kp1);
    }

    /**
     * プログラムを開始する.
     */
    @Override
    public void start() {

        // Creates GUI
        this.initialize();
        // Model を表示する
        if (this.getModel() != null) {
            // 確定日を分かりやすい表現に変える
            String timeStamp = ModelUtils.getDateAsFormatString(
                    model.getDocInfo().getFirstConfirmDate(),
                    IInfoModel.KARTE_DATE_FORMAT);

            if (model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP)) {
                StringBuilder sb = new StringBuilder();
                sb.append(timeStamp);
                sb.append(UNDER_TMP_SAVE);
                timeStamp = sb.toString();
            }
            timeStampLabel.setText(timeStamp);
            KarteRenderer_2 renderer = new KarteRenderer_2(soaPane, null);
            renderer.render(model);
        }

        // モデル表示後にリスナ等を設定する
        ChartMediator mediator = getContext().getChartMediator();
        soaPane.init(false, mediator);
        enter();
    }

    @Override
    public void stop() {
        soaPane.clear();
    }

    /**
     * 表示するモデルを設定する.
     * @param model 表示するDocumentModel
     */
    public void setModel(DocumentModel model) {
        this.model = model;
    }

    /**
     * 表示するモデルを返す.
     * @return 表示するDocumentModel
     */
    public DocumentModel getModel() {
        return model;
    }

    /**
     * 選択状態を設定する.
     * 選択状態によりViewのボーダの色を変える.
     * @param selected 選択された時 true
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            timeStampLabel.setFont(timeStampFontBold);
        } else {
            timeStampLabel.setFont(timeStampFont);
        }
    }

    /**
     * 選択されているかどうかを返す.
     * @return 選択されている時 true
     */
    public boolean isSelected() {
        return selected;
    }

    public void addMouseListener(MouseListener ml) {
        soaPane.getTextPane().addMouseListener(ml);
    }

    @Override
    public int hashCode() {
        return getModel().getDocInfo().getDocId().hashCode() + 72;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            DocInfoModel otheInfo = ((KarteViewer) other).getModel()
            .getDocInfo();
            return getModel().getDocInfo().equals(otheInfo);
        }
        return false;
    }

    @Override
    public int compareTo(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            DocInfoModel otheInfo = ((KarteViewer) other).getModel()
            .getDocInfo();
            return getModel().getDocInfo().compareTo(otheInfo);
        }
        return -1;
    }
}

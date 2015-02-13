package open.dolphin.client;

import java.awt.event.MouseListener;
import javax.swing.SwingConstants;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;

/**
 * 2号カルテクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class KarteViewer2 extends KarteViewer {

    // P Pane
    private KartePane pPane;

    public KarteViewer2() {}

    /**
     * P Pane を返す。
     * @return pPane
     */
    public KartePane getPPane() {
        return pPane;
    }

    /**
     * ２号カルテで初期化する。
     */
    private void initialize() {

        KartePanel kp2 = KartePanelFactory.createViewerPanel();
        panel2 = kp2;

        // TimeStampLabel を生成する
        timeStampLabel = kp2.getTimeStampLabel();
        timeStampLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeStampLabel.setForeground(timeStampFore);
        timeStampLabel.setFont(timeStampFont);

        // SOA Pane を生成する
        soaPane = new KartePane();
        soaPane.setTextPane(kp2.getSoaTextPane());
        soaPane.setRole(IInfoModel.ROLE_SOA);
        if (model != null) {
            // Schema 画像にファイル名を付けるのために必要
            String docId = model.getDocInfo().getDocId();
            soaPane.setDocId(docId);
        }

        // P Pane を生成する
        pPane = new KartePane();
        pPane.setTextPane(kp2.getPTextPane());
        pPane.setRole(IInfoModel.ROLE_P);

        setUI(kp2);
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        //
        // Creates GUI
        //
        this.initialize();

        // Model を表示する
        if (this.getModel() != null) {
            //
            // 確定日を分かりやすい表現に変える
            //
            StringBuilder timeStamp = new StringBuilder();
            timeStamp.append(ModelUtils.getDateAsFormatString(
                    model.getDocInfo().getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT));

            if (model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP)) {
                timeStamp.append(UNDER_TMP_SAVE);
            }
            // timeStamp にカルテ作成者を入れる
            String drName = model.getCreator().getCommonName();
            timeStamp.append("　記載医師: ");
            timeStamp.append(drName);

            timeStampLabel.setText(timeStamp.toString());
            KarteRenderer_2 renderer = new KarteRenderer_2(soaPane, pPane);
            renderer.render(model);
        }

        // モデル表示後にリスナ等を設定する
        ChartMediator mediator = getContext().getChartMediator();
        soaPane.init(false, mediator);
        pPane.init(false, mediator);
        enter();
    }

    @Override
    public void stop() {
        soaPane.clear();
        pPane.clear();
    }

    @Override
    public void addMouseListener(MouseListener ml) {
        soaPane.getTextPane().addMouseListener(ml);
        pPane.getTextPane().addMouseListener(ml);
    }
}

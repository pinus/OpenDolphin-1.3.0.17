package open.dolphin.inspector;

import java.awt.*;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.client.CompositeArea;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.project.Project;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJScrollPane;
import org.apache.log4j.Logger;

/**
 * 患者のメモを表示し編集するクラス.  modified by pns
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class MemoInspector {

    private JPanel memoPanel;
    private CompositeArea memoArea;
    private PatientMemoModel patientMemoModel;
    private final ChartImpl context;
    private final Logger logger;
    private String oldText = "";
    public static final String NAME = "memoInspector";

    private boolean shouldAlert = false;
    private static final Color[] ALERT_LINE_COLOR = {new Color(255,100,100), new Color(255,130,130), new Color(255,180,180)};
    private static final Color ALERT_BACK_COLOR = new Color(255,240,240);

    /**
     * MemoInspectorオブジェクトを生成する.
     * @param context
     */
    public MemoInspector(ChartImpl context) {
        this.context = context;
        logger = ClientContext.getBootLogger();
        initComponents();
        update();
        // Undo
        memoArea.getDocument().addUndoableEditListener(memoArea);
    }

    /**
     * レイアウト用のパネルを返す.
     * @return レイアウトパネル
     */
    public JPanel getPanel() {
        return memoPanel;
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponents() {

        memoArea = new CompositeArea(5, 10);
        memoArea.setParent(context);
        memoArea.setLineWrap(true);
        memoArea.setMargin(new java.awt.Insets(3, 3, 2, 2));
        memoArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        memoArea.setName(NAME);
        IMEControl.setImeOnIfFocused(memoArea);
        // isReadOnly対応
        memoArea.setEnabled(!context.isReadOnly());

        AttentiveViewport vport = new AttentiveViewport();
        vport.setView(memoArea);

        MyJScrollPane pane = new MyJScrollPane();
        pane.setViewport(vport);
        pane.putClientProperty("JComponent.sizeVariant", "small");

        memoPanel = new JPanel(new BorderLayout());
        memoPanel.add(pane, BorderLayout.CENTER);
    }

    /**
     * 患者メモを表示する.
     */
    private void update() {
        //List list = context.getKarte().getEntryCollection("patientMemo");
        patientMemoModel = context.getKarte().getPatientMemo();
        if (patientMemoModel != null) {
            memoArea.setText(patientMemoModel.getMemo());
            oldText = memoArea.getText();

            // 注意事項がある場合，赤い枠で注意を促す
            shouldAlert = containsContraindication();
            memoPanel.repaint();
        }
    }

    /**
     * 患者メモを更新する.
     */
    public void updateMemo() {
        // メモ内容に変更がなければ何もしない
        if (oldText.equals(memoArea.getText().trim())) { return; }

        if (patientMemoModel == null) {
            patientMemoModel =  new PatientMemoModel();
        }
        // 上書き更新
        Date confirmed = new Date();
        patientMemoModel.setKarte(context.getKarte());
        patientMemoModel.setCreator(Project.getUserModel());
        patientMemoModel.setConfirmed(confirmed);
        patientMemoModel.setRecorded(confirmed);
        patientMemoModel.setStarted(confirmed);
        patientMemoModel.setStatus(IInfoModel.STATUS_FINAL);
        patientMemoModel.setMemo(memoArea.getText().trim());

        DBTask task = new DBTask<Void>(context) {

            @Override
            protected Void doInBackground() throws Exception {
                logger.debug("updateMemo doInBackground");
                DocumentDelegater ddl = new DocumentDelegater();
                ddl.updatePatientMemo(patientMemoModel);
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                logger.debug("updateMemo succeeded");
            }
        };

        task.execute();
    }

    /**
     * メモ内容に禁忌等の注意事項があるかどうか
     * @return
     */
    public boolean containsContraindication() {
        String text = memoArea.getText();
        return text.contains("禁")
                || text.contains("注意")
                || text.contains("発疹")
                || text.contains("皮疹")
                || text.contains("薬疹")
                || text.contains("アレルギ")
                || text.contains("ショック")
                || text.contains("アナフィラ");
    }

    private class AttentiveViewport extends JViewport {
        private static final long serialVersionUID = 1L;

        public void setView(JComponent c) {
            c.setOpaque(false);
            super.setView(c);
        }

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics g = graphics.create();
            if (shouldAlert) {
                g.setColor(ALERT_BACK_COLOR);
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 3, 3);

                for (int i=0; i<3; i++) {
                g.setColor(ALERT_LINE_COLOR[i]);
                g.drawRoundRect(i, i, getWidth()-1-2*i, getHeight()-1-2*i, 3-i, 3-i);
                }
            }
            g.dispose();
        }
    }
}

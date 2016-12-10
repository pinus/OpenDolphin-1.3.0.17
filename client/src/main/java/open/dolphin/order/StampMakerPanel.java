package open.dolphin.order;

import open.dolphin.order.stampeditor.StampEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.client.StampTree;
import open.dolphin.client.StampTreeNode;
import open.dolphin.delegater.StampDelegater;
import open.dolphin.helper.Task;
import open.dolphin.infomodel.*;
import open.dolphin.ui.HorizontalPanel;

/**
 * StampMakerPanel.
 * StampBox の startStampMake() から呼ばれ，左側に表示されるパネル
 * StampEditorProxyPanel と ButtonPanel からなる
 * @author pns
 */
public class StampMakerPanel extends JPanel implements PropertyChangeListener, TreeSelectionListener {
    private static final long serialVersionUID = 1L;

    public static final String EDITOR_VALUE_PROP = "editorSetProp";

    // StampEditorProxyPanel
    private StampEditorProxyPanel editorPanel;
    // StampBoxPlugin とデータをやりとりするためのボタン
    private JButton rightArrowBtn;
    private JButton leftArrowBtn;
    // StampBoxPlugin から addPropertyChangeListener される
    private final PropertyChangeSupport boundSupport = new PropertyChangeSupport(this);
    // StampBoxPlugin で選択されているノード
    private StampTreeNode selectedNode;

    public StampMakerPanel() {
        initComponent();
    }

    /**
     * GUI コンポーネントを生成する。
     */
    private void initComponent() {

        editorPanel = new StampEditorProxyPanel(this);

         // 編集したスタンプをボックスへ登録する右向きボタンを生成する
        rightArrowBtn = new JButton(GUIConst.ICON_GO_NEXT_16);
        rightArrowBtn.addActionListener(new ToStampBoxPlugin());
        rightArrowBtn.setEnabled(false);

        // スタンプボックスのスタンプをセットテーブルへ取り込む左向きのボタンを生成する
        leftArrowBtn = new JButton(GUIConst.ICON_GO_PREVIOUS_16);
        leftArrowBtn.addActionListener(new FromStampBoxPlugin());
        leftArrowBtn.setEnabled(false);

        // エディタパネルの PreferedSize を設定する
        editorPanel.setPreferredSize(new Dimension(GUIConst.DEFAULT_EDITOR_WIDTH, GUIConst.DEFAULT_EDITOR_HEIGHT));

        // ボタンパネルを作成
        JPanel btnPanel = new HorizontalPanel();
        BoxLayout box = new BoxLayout(btnPanel, BoxLayout.Y_AXIS);
        btnPanel.setLayout(box);
        btnPanel.add(Box.createVerticalStrut(100));
        btnPanel.add(rightArrowBtn);
        btnPanel.add(leftArrowBtn);
        btnPanel.add(Box.createVerticalGlue());

        // 配置する
        this.setLayout(new BorderLayout(0, 0));
        this.add(editorPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.EAST);

        // 全体の PreferedSize を設定する
        this.setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * StampBoxPlugin のタブが切り替えられた時、対応するエディタを show する。
     * @param entity するエディタのエンティティ名
     */
    public void show(String entity) {
        editorPanel.show(entity);
    }

    /**
     * StampMakerPanel を閉じてリソースを解放する
     */
    public void close() {
        editorPanel.close();
        //for(PropertyChangeListener listener : boundSupport.getPropertyChangeListeners()) {
        //    boundSupport.removePropertyChangeListener(listener);
        //}
    }

    /**
     * プロパティチェンジリスナを登録する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.addPropertyChangeListener(prop, listener);
    }

    /**
     * プロパティチェンジリスナを削除する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    public void remopvePropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(prop, listener);
    }

    /**
     * 編集中のスタンプの有効/無効の属性通知を受け，右向きボタンを制御する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(StampEditor.VALID_DATA_PROP)) {
            boolean state = (Boolean) e.getNewValue();
            rightArrowBtn.setEnabled(state);
        }
    }

    /**
     * スタンプツリーで選択が変わると呼ばれる
     * StampBoxPlugin で，全 StampTree に addTreeSelectionListener(StampMakerPanel) されている
     * 選択されたスタンプに応じて左ボタンを制御する。
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        StampTree tree = (StampTree) e.getSource();
        StampTreeNode node =(StampTreeNode) tree.getLastSelectedPathComponent();

        // ノードが葉でない時のみ enabled にする
        // またその時以外は選択ノード属性をnullにする
        if (node != null && node.isLeaf()) {

            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();

            if (info.isSerialized()) {
                leftArrowBtn.setEnabled(true);
                selectedNode = node;
            } else {
                leftArrowBtn.setEnabled(false);
                selectedNode = null;
            }
        }
    }

    /**
     * 編集したスタンプをボックスへ通知するためのアクションリスナ。
     * 右向きボタンのリスナでエディタの編集値をgetして StampBoxPlugin のリスナに伝える
     * fire で StampBoxPlugin#EditorValueListener が呼ばれる
     */
    private class ToStampBoxPlugin implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boundSupport.firePropertyChange(EDITOR_VALUE_PROP, null, editorPanel.getValue());
        }
    }

    /**
     * スタンプボックスで選択されているスタンプをエディタへ取り込んで編集するための左向きボタンのリスナ。
     */
    private class FromStampBoxPlugin implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 無効なデータは読まない
            if (selectedNode == null || !(selectedNode.getUserObject() instanceof ModuleInfoBean)) return;

            final ModuleInfoBean stampInfo = (ModuleInfoBean) selectedNode.getUserObject();

            final StampDelegater sdl = new StampDelegater();
            int maxEstimation = 30*1000;
            int delay = 200;
            String message = "スタンプ箱";
            String note = "検索しています...";
            Component c = SwingUtilities.getWindowAncestor(editorPanel);

            Task task = new Task<StampModel>(c, message, note, maxEstimation) {
                @Override
                protected StampModel doInBackground() throws Exception {
                    return sdl.getStamp(stampInfo.getStampId());
                }
                @Override
                protected void succeeded(StampModel stampModel) {
                    if (sdl.isNoError() && stampModel != null) {
                        IInfoModel stamp = stampModel.getStamp();
                        if (stamp != null) {
                            // diagnosis の場合
                            if (IInfoModel.ENTITY_DIAGNOSIS.equals(stampModel.getEntity())) {
                                editorPanel.setValue((RegisteredDiagnosisModel) stamp);

                            } else {
                                ModuleModel module = new ModuleModel();
                                module.setModel(stamp);
                                module.setModuleInfo(stampInfo);
                                editorPanel.setValue(module);
                                editorPanel.enter(); // SearchField にフォーカス
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editorPanel),
                                (stampModel == null)? "実体のないスタンプです。削除してください。" : sdl.getErrorMessage(),
                                ClientContext.getFrameTitle("Stamp取得"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            };
            //task.setMillisToPopup(delay);
            task.execute();
        }
    }
}

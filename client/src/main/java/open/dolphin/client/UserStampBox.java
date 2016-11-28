package open.dolphin.client;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.util.StampTreeUtils;

/**
 * UserStampBox
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class UserStampBox extends AbstractStampBox {
    private static final long serialVersionUID = 1L;

    private static final String BOX_INFO = "個人用スタンプ箱";

    /** パススタンプのタブ番号 */
    private int pathIndex;

    /** ORCA セットのタブ番号 */
    private int orcaIndex;

    /** タブの順番をセットする HashMap */
    private final HashMap<String, Integer> tabMap = new HashMap<>();

    /** 好みの順番 */
    private final String[] orderedTabName = {
        "処 方", "傷病名", "初診・再診", "指導・在宅", "テキスト",
        "処 置", "手 術", "生体検査", "生理検査", "細菌検査", "検体検査",
        "注 射", "パ ス", "汎 用", "放射線", "ORCA", "その他"
    };

    public UserStampBox() {
        super();

        // tabMap に順番情報をセット
        for (int i=0; i< orderedTabName.length; i++) {
            tabMap.put(orderedTabName[i], i);
        }
    }

    /**
     * StampBox を構築する。
     */
    @Override
    protected void buildStampBox() {

        // Build stampTree
        String treeXml = getStampTreeModel().getTreeXml();
        List<StampTree> userTrees = StampTreeUtils.xmlDecode(treeXml);

        // StampTreeへ設定するPopupMenuとTransferHandlerを生成する
        StampTreeTransferHandler transferHandler = new StampTreeTransferHandler();

        // userTrees を順番に並べ替える
        Collections.sort(userTrees,
                (o1, o2) -> tabMap.get(o1.getTreeName()) - tabMap.get(o2.getTreeName()));

        // StampBox(TabbedPane) へリスト順に格納する
        // 一つのtabへ一つのtreeが対応
        int index = 0;
        for (final StampTree stampTree : userTrees) {
            stampTree.setUserTree(true);
            stampTree.setTransferHandler(transferHandler);
            stampTree.setStampBox(getContext());
            StampTreePanel treePanel = new StampTreePanel(stampTree);
            this.addTab(stampTree.getTreeName(), treePanel);

            // Path、ORCA のタブ番号を保存する
            if (stampTree.getEntity().equals(IInfoModel.ENTITY_PATH)) {
                pathIndex = index;
            } else if (stampTree.getEntity().equals(IInfoModel.ENTITY_ORCA)) {
                orcaIndex = index;
            }

            stampTree.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e) { showPopup(e);}

                @Override
                public void mouseReleased(MouseEvent e) { showPopup(e);}

                private void showPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {

                        StampTreePopupMenu popup = new StampTreePopupMenu(stampTree);

                        if (IInfoModel.ENTITY_DIAGNOSIS.equals(stampTree.getTreeInfo().getEntity())) {
                            // 傷病名の popup に，カルテに病名を挿入する特別メニューを加える
                            List<ChartImpl> allCharts = ChartImpl.getAllChart();
                            if (! allCharts.isEmpty()) {
                                popup.insert(new JPopupMenu.Separator(), 0);
                                allCharts.forEach(chart -> {
                                    popup.insert(new SendDiagnosisAction(chart, stampTree), 0);
                                });
                            }

                        } else {
                            // それ以外の popup でも PPane にスタンプを挿入できるようにする
                            List<Chart> allFrames = EditorFrame.getAllEditorFrames();
                            if (! allFrames.isEmpty()) {
                                popup.insert(new JPopupMenu.Separator(), 0);
                                allFrames.forEach(chart -> {
                                    popup.insert(new SendStampAction(chart, stampTree), 0);
                                });
                            }
                        }
                        popup.show(stampTree, e.getX(), e.getY());
                    }
                }
            });

            index++;
        }
    }

    /**
     * 引数のタブ番号に対応するStampTreeにエディタから発行があるかどうかを返す。
     * @param index タブ番号
     * @return エディタから発行がある場合に true
     */
    @Override
    public boolean hasEditor(int index) {
        return (index != pathIndex && index != orcaIndex);
    }

    /**
     * StampEditor が無いタブを enable/disable 切換
     * @param b
     */
    @Override
    public void setHasNoEditorEnabled(boolean b) {
        this.setEnabledAt(pathIndex, b);
        this.setEnabledAt(orcaIndex, b);
    }

    @Override
    public String getInfo() {
        return BOX_INFO;
    }

    /**
     * AbstractAction which sends Stamps to a Chart
     */
    private abstract class SendStampBase extends AbstractAction {
        public final Chart chart;
        public final StampTree tree;

        public SendStampBase(Chart chart, StampTree tree) {
            this.chart = chart;
            this.tree = tree;
            putValue(Action.NAME, getName(chart));
            putValue(Action.SMALL_ICON, GUIConst.ICON_ARROW_UP_LEFT_16);
        }
        /**
         * アクションの名前（メニューに表示される文字列）
         * @param chart
         * @return
         */
        public String getName(Chart chart) {
            String id = chart.getKarte().getPatient().getPatientId();
            String patient = chart.getKarte().getPatient().getFullName();
            return String.format("%s : %s", patient, id);
        }
        /**
         * 選択されたスタンプをリストにする
         * @return
         */
        public List<ModuleInfoBean> getStampList(StampTreeNode selected) {

            List<ModuleInfoBean> stampList = new ArrayList<>();

            if (selected.isLeaf()) {
                stampList.add(selected.getStampInfo());

            } else {
                // ノードの葉を列挙する
                Enumeration e = selected.preorderEnumeration();
                while (e.hasMoreElements()) {
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    if (node.isLeaf()) {
                        ModuleInfoBean stampInfo = node.getStampInfo();
                        stampList.add(stampInfo);
                    }
                }
            }
            return stampList;
        }
    }

    /**
     * DiagnosisDocument に StampTree から Diagnosis を送る Action
     */
    private class SendDiagnosisAction extends SendStampBase {
        private final DiagnosisDocument doc;

        public SendDiagnosisAction(Chart chart, StampTree tree) {
            super(chart, tree);
            doc = ((ChartImpl)chart).getDiagnosisDocument();
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            StampTreeNode selected = tree.getSelectedNode();
            if (selected == null) { return; }
            List<ModuleInfoBean> stampList = getStampList(selected);
            doc.importStampList(stampList, 0);
        }
    }

    /**
     * Action which sends selected Stamps to PPane
     */
    private class SendStampAction extends SendStampBase {
        private final KartePane pane;

        public SendStampAction(Chart chart, StampTree tree) {
            super(chart, tree);
            pane = ((EditorFrame) chart).getEditor().getPPane();
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            StampTreeNode selected = tree.getSelectedNode();
            if (selected == null) { return; }
            List<ModuleInfoBean> stampList = getStampList(selected);
            // caret を最後に送ってから import する
            JTextPane textPane = pane.getTextPane();
            KarteStyledDocument doc = pane.getDocument();
            textPane.setCaretPosition(doc.getLength());
            pane.stampInfoDropped(stampList);
        }
    }
}

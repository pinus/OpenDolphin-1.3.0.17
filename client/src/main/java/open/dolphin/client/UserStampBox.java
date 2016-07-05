package open.dolphin.client;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import open.dolphin.infomodel.IInfoModel;
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
        Collections.sort(userTrees, new Comparator<StampTree>(){
            @Override
            public int compare(StampTree o1, StampTree o2) {
                return tabMap.get(o1.getTreeName()) - tabMap.get(o2.getTreeName());
            }
        });

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

            final StampTreePopupMenu popup = new StampTreePopupMenu(stampTree);
            stampTree.addMouseListener(new MouseAdapter(){
                private final StampTree thisTree = stampTree;

                @Override
                public void mousePressed(MouseEvent e) { showPopup(e);}

                @Override
                public void mouseReleased(MouseEvent e) { showPopup(e);}

                private void showPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        // 傷病名 Tree に特別メニューを加える
                        if (IInfoModel.ENTITY_DIAGNOSIS.equals(thisTree.getTreeInfo().getEntity())) {
                            StampTreePopupMenu diagPop = new StampTreePopupMenu(thisTree);
                            List<ChartImpl> allCharts = ChartImpl.getAllChart();
                            if (! allCharts.isEmpty()) {
                                diagPop.addSeparator();
                                for (ChartImpl chart : allCharts) {
                                    String title = chart.getFrame().getTitle();
                                    diagPop.add(title);

                                    DiagnosisDocument doc = chart.getDiagnosisDocument();

                                    Action a = new AbstractAction() {
                                        {
                                            putValue(Action.NAME, title);
                                            putValue(Action.SMALL_ICON, GUIConst.ICON_EMPTY_22);
                                        }
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                        }

                                    };
                                }
                            }

                            diagPop.show(stampTree, e.getX(), e.getY());

                        } else {
                            popup.show(stampTree, e.getX(), e.getY());
                        }
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
}

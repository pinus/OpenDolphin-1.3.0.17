package open.dolphin.client;

import java.util.List;
import open.dolphin.util.StampTreeUtils;

/**
 * AspStampBox
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class AspStampBox extends AbstractStampBox {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildStampBox() {

        // Build stampTree
        String treeXml = getStampTreeModel().getTreeXml();
        List<StampTree> aspTrees = StampTreeUtils.xmlDecode(treeXml, new AspStampTreeBuilder());

        // StampTreeに設定するポップアップメニューとトランスファーハンドラーを生成する
        AspStampTreeTransferHandler transferHandler = new AspStampTreeTransferHandler();

        // StampBox(TabbedPane) へリスト順に格納する
        for (StampTree stampTree : aspTrees) {
            stampTree.setTransferHandler(transferHandler);
            stampTree.setUserTree(false);
            stampTree.setStampBox(getContext());
            StampTreePanel treePanel = new StampTreePanel(stampTree);
            this.addTab(stampTree.getTreeName(), treePanel);
        }
    }
}

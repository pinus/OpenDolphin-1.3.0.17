package open.dolphin.stampbox;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.StampTreeBean;
import open.dolphin.ui.PNSTabbedPane;

/**
 * AbstractStampBox.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class AbstractStampBox extends PNSTabbedPane {
    private static final long serialVersionUID = 1L;

    private StampTreeBean stampBean;
    private StampBoxPlugin context;

    public AbstractStampBox() {
    }

    /**
     * StampBox を構築する
     */
    protected abstract void buildStampBox();

    public StampBoxPlugin getContext() {
        return context;
    }

    public void setContext(StampBoxPlugin plugin) {
        context = plugin;
    }

    public StampTreeBean getStampTreeModel() {
        return stampBean;
    }

    public void setStampTreeModel(StampTreeBean bean) {
        this.stampBean = bean;
    }

    /**
     * 引数のカテゴリに対応するTreeを返す.
     * @param entity
     * @return カテゴリにマッチするStampTree
     */
    public StampTree getStampTree(String entity) {
        int count = this.getTabCount();
        boolean found = false;
        StampTree tree = null;
        for (int i = 0; i < count; i++) {
            StampTreePanel panel = (StampTreePanel) this.getComponentAt(i);
            tree = panel.getTree();
            if (entity.equals(tree.getEntity())) {
                found = true;
                break;
            }
        }

        return found ? tree : null;
    }

    public StampTree getStampTree(int index) {
        if (index >=0 && index < this.getTabCount()) {
            StampTreePanel panel = (StampTreePanel) this.getComponentAt(index);
            return panel.getTree();
        }
        return null;
    }

    public boolean hasEditor(int index) {
        return false;
    }

    public void setHasNoEditorEnabled(boolean b) {
    }

    /**
     * スタンプボックスに含まれる全treeのTreeInfoリストを返す.
     * @return TreeInfoのリスト
     */
    public List<TreeInfo> getAllTreeInfos() {
        List<TreeInfo> ret = new ArrayList<>();
        int cnt = this.getTabCount();
        for (int i = 0; i < cnt; i++) {
            //StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTreePanel tp = (StampTreePanel) this.getComponentAt(i);
            StampTree tree = tp.getTree();
            TreeInfo info = tree.getTreeInfo();
            ret.add(info);
        }
        return ret;
    }

    /**
     * スタンプボックスに含まれる全treeを返す.
     * @return StampTreeのリスト
     */
    public List<StampTree> getAllTrees() {
        List<StampTree> ret = new ArrayList<>();
        int cnt = this.getTabCount();
        for (int i = 0; i < cnt; i++) {
            //StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTreePanel tp = (StampTreePanel) this.getComponentAt(i);
            StampTree tree = tp.getTree();
            ret.add(tree);
        }
        return ret;
    }

    /**
     * スタンプボックスに含まれる病名以外のStampTreeを返す.
     * @return StampTreeのリスト
     */
    public List<StampTree> getAllPTrees() {

        List<StampTree> ret = new ArrayList<>();
        int cnt = this.getTabCount();

        for (int i = 0; i < cnt; i++) {
            //StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTreePanel tp = (StampTreePanel) this.getComponentAt(i);
            StampTree tree = tp.getTree();

            // 病名StampTree はスキップする
            if (! tree.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS)){
                ret.add(tree);
            }
        }
        return ret;
    }

    /**
     * 引数のエンティティ配下にある全てのスタンプを返す.
     * これはメニュー等で使用する.
     * @param entity Treeのエンティティ
     * @return 全てのスタンプのリスト
     */
    public List<ModuleInfoBean> getAllStamps(String entity) {

        StampTree tree = getStampTree(entity);
        if (tree != null) {
            List<ModuleInfoBean> ret = new ArrayList<>();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            Enumeration e = rootNode.preorderEnumeration();
            while (e.hasMoreElements()) {
                StampTreeNode node = (StampTreeNode) e.nextElement();
                if (node.isLeaf()) {
                    ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                    ret.add(info);
                }
            }
            return ret;
        }
        return null;
    }

    public List<String> getEntities() {
        List<String> ret = new ArrayList<>();
        getAllTreeInfos().forEach(info -> ret.add(info.getEntity()));
        return ret;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(stampBean.getName());
        sb.append(" ");
        sb.append(stampBean.getPartyName());
        if (sb.length() > 16) {
            sb.setLength(12);
            sb.append("...");
        }
        return sb.toString();
    }
}

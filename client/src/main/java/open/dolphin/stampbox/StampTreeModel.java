package open.dolphin.stampbox;

import open.dolphin.infomodel.ModuleInfoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

/**
 * スタンプツリーのモデルクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampTreeModel extends DefaultTreeModel {
    private static final long serialVersionUID = -2227174337081687786L;
    private Logger logger = LoggerFactory.getLogger(StampTreeModel.class);
    private UndoManager undoManager = new UndoManager();

    public StampTreeModel(TreeNode node) {
        super(node);
    }

    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
        logger.info("child = " + newChild + ", parent = " + parent + ", index = " + index);
        super.insertNodeInto(newChild, parent, index);
    }

    public void removeNodeFromParent(MutableTreeNode node) {
        logger.info("parent = " + node.getParent() + ", index = " + node.getParent().getIndex(node));
        super.removeNodeFromParent(node);
    }

    /**
     * ノード名の変更をインターセプトして処理する.
     *
     * @param path     tree path
     * @param newValue stamp name
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

        // 変更ノードを取得する
        StampTreeNode node = (StampTreeNode) path.getLastPathComponent();

        // Debug
        //String oldString = node.toString ();
        String newString = (String) newValue;
        //logger.info(oldString + " -> " + newString);

        // 葉ノードの場合は StampInfo の name を変更する.
        // そうでない場合は新しい文字列を userObject に設定する.
        if (node.isLeaf()) {
            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
            info.setStampName(newString);

        } else {
            node.setUserObject(newString);
        }

        // リスナへ通知する
        nodeChanged(node);
    }

    public void undo() {
        logger.info("undo");
        if (undoManager.canUndo()) { undoManager.undo(); }
    }

    public void redo() {
        logger.info("redo");
        if (undoManager.canRedo()) { undoManager.redo(); }
    }

    private class InsertEdit extends AbstractUndoableEdit {

    }

    private class RemoveEdit extends AbstractUndoableEdit {

    }
}

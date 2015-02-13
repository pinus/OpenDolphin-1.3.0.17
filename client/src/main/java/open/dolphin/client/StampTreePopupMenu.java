package open.dolphin.client;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import open.dolphin.helper.ActionManager;
import open.dolphin.helper.ActionManager.Action;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.ui.MyJPopupMenu;
import open.dolphin.ui.MyJSheet;

/**
 * StampTree に PopupMenu を表示する　StampTreePopupAdapter から切換
 * @author pns
 */
public class StampTreePopupMenu extends MyJPopupMenu {
    private static final long serialVersionUID = 1L;

    private StampTree tree;

    public StampTreePopupMenu(StampTree tree) {
        super();
        this.tree = tree;
        buildPopupMenu();
    }

    private void buildPopupMenu() {
        ActionManager m = new ActionManager(this);

        add(m.getMenuItem("createNewFolder", "新規フォルダ", GUIConst.ICON_FOLDER_NEW_16));
        add(m.getMenuItem("deleteNode", "削　除", GUIConst.ICON_REMOVE_16));
        add(m.getMenuItem("renameNode", "名前を変更", GUIConst.ICON_EMPTY_16));
        addSeparator();

        add(m.getMenuItem("collapseAll", "フォルダを全て閉じる", GUIConst.ICON_TREE_COLLAPSED_16));
        add(m.getMenuItem("expandAll", "フォルダを全て展開する", GUIConst.ICON_TREE_EXPANDED_16));
    }

    @Action
    public void createNewFolder() {
        tree.createNewFolder();
    }
    @Action
    public void deleteNode() {
        int ans = MyJSheet.showConfirmDialog(SwingUtilities.getWindowAncestor(tree),
                "本当に削除しますか", "スタンプ削除", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.OK_OPTION) {
            if (isEditable()) tree.deleteNode();
            else Toolkit.getDefaultToolkit().beep();
        }
    }
    @Action
    public void renameNode() {
        if (isEditable()) tree.renameNode();
        else Toolkit.getDefaultToolkit().beep();
    }
    @Action
    public void expandAll() {
        tree.expandAll();
    }
    @Action
    public void collapseAll() {
        tree.collapseAll();
    }

    private boolean isEditable() {
        StampTreeNode node = tree.getSelectedNode();
        if (node.isLeaf()) {
            // Leaf なので StampInfo 　を得る
            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
            return info.isEditable();
        }
        return true;
    }
}
